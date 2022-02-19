package com.skdziwak.factoriolang;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skdziwak.factoriolang.blueprint.Blueprint;
import com.skdziwak.factoriolang.blueprint.BlueprintEncoder;
import com.skdziwak.factoriolang.blueprint.CompilationStateToBlueprintConverter;
import com.skdziwak.factoriolang.bytecode.ProgramToBytecodeConverter;
import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.parser.ProgramParser;
import com.skdziwak.factoriolang.simulator.HardwareSimulator;
import com.skdziwak.factoriolang.tree.Program;
import org.apache.commons.cli.*;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    private static final int DEFAULT_SIMULATION_LENGTH_LIMIT = 10_000;

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("s", false, "Compile to signals");
        options.addOption("j", false, "Compile to json");
        options.addOption("b", false, "Compile to blueprint");
        options.addOption("bc", false, "Compile to bytecode");
        options.addOption("h", false, "Compile to humanized assembly");
        options.addOption("sim", false, "Compile and simulate");
        options.addOption("sl", true, "Simulation length limit (default: " + DEFAULT_SIMULATION_LENGTH_LIMIT + ")");
        options.addOption("no", false, "No optimisations");
        options.addOption("o", true, "Output file");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine parse = parser.parse(options, args);
            List<String> argList = parse.getArgList();
            if (argList.isEmpty()) {
                printHelp(options);
                return;
            }
            StringBuilder sourceCode = new StringBuilder();
            try {
                for (String sourcePath : argList) {
                    FileReader fileReader = new FileReader(sourcePath);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    IOUtils.copy(fileReader, outputStream);
                    fileReader.close();
                    outputStream.close();

                    sourceCode.append(outputStream.toString(StandardCharsets.UTF_8));
                    sourceCode.append("\n\n");
                }
            } catch (IOException ex) {
                System.err.println("Unable to load one of files: " + ex.getMessage());
                return;
            }

            CompilationState compilationState = new CompilationState();

            if (parse.hasOption("no")) {
                compilationState.noOptimisations();
            }

            Program program = ProgramParser.parseProgram(sourceCode.toString());
            program.preCompile(compilationState);
            program.compile(compilationState);
            program.postCompile(compilationState);

            String output = null;

            if (parse.hasOption("sim")) {
                output = "Program:\n";
                output += AssemblyHumanizer.humanizeCompilationState(compilationState);
                output += "\n\nExecution log:\n";

                HardwareSimulator simulator = new HardwareSimulator();
                if (parse.hasOption("sl")) {
                    output += simulator.simulate(compilationState.getInstructions(), Integer.parseInt(parse.getOptionValue("sl")));
                } else {
                    output += simulator.simulate(compilationState.getInstructions(), DEFAULT_SIMULATION_LENGTH_LIMIT);
                }
            } else if (parse.hasOption("bc")) {
                output = ProgramToBytecodeConverter.generateByteCode(compilationState);
            } else if (parse.hasOption("s")) {
                output = compilationState.getStatesString();
            } else if (parse.hasOption("b")) {
                Blueprint blueprint = CompilationStateToBlueprintConverter.convert(compilationState);
                try {
                    output = BlueprintEncoder.encode(blueprint);
                } catch (IOException e) {
                    System.err.println("Unable to encode blueprint: " + e.getMessage());
                    return;
                }
            } else if (parse.hasOption("j")) {
                Blueprint blueprint = CompilationStateToBlueprintConverter.convert(compilationState);

                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    output = objectMapper.writeValueAsString(blueprint);
                } catch (JsonProcessingException e) {
                    System.err.println("Unable to encode json: " + e.getMessage());
                    return;
                }
            } else if (parse.hasOption("h")) {
                output = AssemblyHumanizer.humanizeCompilationState(compilationState);
            }

            if (output == null) {
                printHelp(options);
            } else {
                if (parse.hasOption("o")) {
                    try {
                        FileWriter writer = new FileWriter(parse.getOptionValue("o"));
                        writer.write(output);
                        writer.close();
                    } catch (IOException ex) {
                        System.err.println("Unable to save file: " + ex.getMessage());
                    }
                } else {
                    System.out.println(output);
                }
            }
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            printHelp(options);
        }
    }

    private static void printHelp(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("java -jar factorio-compiler.jar [SOURCE_FILES] [OPTIONS]", options);
    }
}

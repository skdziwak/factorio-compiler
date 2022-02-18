package com.skdziwak.factoriolang.compilation;

import com.skdziwak.factoriolang.constants.InstructionType;
import com.skdziwak.factoriolang.constants.MathOperator;
import com.skdziwak.factoriolang.tree.functions.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompilationState {
    private final List<Instruction> instructions = new ArrayList<>();
    private final Map<String, Integer> variableAddresses = new HashMap<>();
    private int nextFreeMemoryAddress = 2;
    private final Map<String, Function> functions = new HashMap<>();
    private FunctionContext functionContext;
    private boolean optimize = true;
    private boolean skipNextOptimisation = false;

    public CompilationState() {
        Instruction mallocInitValueSet = new Instruction(InstructionType.SET_REGISTER, 1).setSignalC(() -> this.nextFreeMemoryAddress);
        Instruction copyMallocInitValueToRam = new Instruction(InstructionType.COPY_REG_TO_RAM, 1, 1);
        this.instructions.add(mallocInitValueSet);
        this.instructions.add(copyMallocInitValueToRam);
    }

    public void noOptimisations() {
        this.optimize = false;
    }

    private boolean checkOptimize() {
        return optimize && !skipNextOptimisation;
    }

    private void consumeNextOptimisationSkip() {
        skipNextOptimisation = false;
    }

    public void copyRegister(int reg1, int reg2) {
        if (reg1 != reg2 || !checkOptimize()) {
            Instruction instruction = new Instruction(InstructionType.COPY_REG_TO_REG, reg1, reg2);
            addInstruction(instruction);
        }
        consumeNextOptimisationSkip();
    }

    public void math(MathOperator mathOperator) {
        Instruction instruction = new Instruction(InstructionType.MATH, mathOperator.sig);
        addInstruction(instruction);
    }

    public void setRegister(int reg, int value) {
        Instruction instruction = new Instruction(InstructionType.SET_REGISTER, reg, value);
        addInstruction(instruction);
    }

    public void pushReg(int reg) {
        Instruction instruction = new Instruction(InstructionType.PUSH_REG_TO_STACK, reg);
        addInstruction(instruction);
    }

    public void popReg(int reg) {
        Instruction lastInstruction = instructions.get(instructions.size() - 1);
        if (lastInstruction.getSignalA() == InstructionType.PUSH_REG_TO_STACK.getSignal() && lastInstruction.getSignalB() == reg && optimize) {
            instructions.remove(instructions.size() - 1);
        } else {
            Instruction instruction = new Instruction(InstructionType.POP_STACK_TO_REG, reg);
            addInstruction(instruction);
        }
        consumeNextOptimisationSkip();
    }

    public void copyRegisterToRAM(int reg, int ram) {
        Instruction instruction = new Instruction(InstructionType.COPY_REG_TO_RAM, reg, ram);
        addInstruction(instruction);
    }

    public void copyRAMtoRegister(int ram, int reg) {
        Instruction instruction = new Instruction(InstructionType.COPY_RAM_TO_REG, ram, reg);
        addInstruction(instruction);
    }

    public void copyInput(int in, int reg) {
        Instruction instruction = new Instruction(InstructionType.COPY_INPUT_TO_REG, in, reg);
        addInstruction(instruction);
    }

    public void copyOutput(int reg, int out) {
        Instruction instruction = new Instruction(InstructionType.COPY_REG_TO_OUTPUT, reg, out);
        addInstruction(instruction);
    }

    public void addFunction(Function function) {
        functions.put(function.getIdentifier(), function);
    }

    public Function getFunction(String identifier) {
        return functions.get(identifier);
    }

    public int declareVariable(String name) {
        if (this.variableAddresses.containsKey(name)) {
            throw new CompilationException("Variable " + name + " is already defined in global scope!");
        } else {
            int addr = nextFreeMemoryAddress++;
            this.variableAddresses.put(name, addr);
            return addr;
        }
    }

    public int getVariableAddress(String name) {
        if (this.variableAddresses.containsKey(name)) {
            return this.variableAddresses.get(name);
        } else {
            throw new CompilationException("Variable " + name + " was not declared.");
        }
    }

    public void addInstruction(Instruction instruction) {
        this.instructions.add(instruction);
        consumeNextOptimisationSkip();
    }

    public String getStatesString() {
        return this.instructions.stream().map(String::valueOf).collect(Collectors.joining("\n"));
    }

    public int getNextIndex() {
        this.skipNextOptimisation = true;
        return this.instructions.size();
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public FunctionContext getFunctionContext() {
        return functionContext;
    }

    public void setFunctionContext(FunctionContext functionContext) {
        this.functionContext = functionContext;
    }
}

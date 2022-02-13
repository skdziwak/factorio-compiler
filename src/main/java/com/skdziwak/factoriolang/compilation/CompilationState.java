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
    private int nextFreeMemoryAddress = 1;
    private final Map<String, Function> functions = new HashMap<>();
    private FunctionContext functionContext;
    private boolean optimize = true;

    public void noOptimisations() {
        this.optimize = false;
    }

    public void copyRegister(int reg1, int reg2) {
        if (reg1 != reg2 || !optimize) {
            Instruction instruction = new Instruction(InstructionType.COPY_REG_TO_REG, reg1, reg2);
            instructions.add(instruction);
        }
    }

    public void math(MathOperator mathOperator) {
        Instruction instruction = new Instruction(InstructionType.MATH, mathOperator.sig);
        instructions.add(instruction);
    }

    public void setRegister(int reg, int value) {
        Instruction instruction = new Instruction(InstructionType.SET_REGISTER, reg, value);
        instructions.add(instruction);
    }

    public void pushReg(int reg) {
        Instruction instruction = new Instruction(InstructionType.PUSH_REG_TO_STACK, reg);
        instructions.add(instruction);
    }

    public void popReg(int reg) {
        Instruction lastInstruction = instructions.get(instructions.size() - 1);
        if (lastInstruction.getSignalA() == InstructionType.PUSH_REG_TO_STACK.getSignal() && lastInstruction.getSignalB() == reg && optimize) {
            instructions.remove(instructions.size() - 1);
        } else {
            Instruction instruction = new Instruction(InstructionType.POP_STACK_TO_REG, reg);
            instructions.add(instruction);
        }
    }

    public void copyRegisterToRAM(int reg, int ram) {
        Instruction instruction = new Instruction(InstructionType.COPY_REG_TO_RAM, reg, ram);
        instructions.add(instruction);
    }

    public void copyRAMtoRegister(int ram, int reg) {
        Instruction instruction = new Instruction(InstructionType.COPY_RAM_TO_REG, ram, reg);
        instructions.add(instruction);
    }

    public void copyInput(int in, int reg) {
        Instruction instruction = new Instruction(InstructionType.COPY_INPUT_TO_REG, in, reg);
        instructions.add(instruction);
    }

    public void copyOutput(int reg, int out) {
        Instruction instruction = new Instruction(InstructionType.COPY_REG_TO_OUTPUT, reg, out);
        instructions.add(instruction);
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
    }

    public String getStatesString() {
        return this.instructions.stream().map(String::valueOf).collect(Collectors.joining("\n"));
    }

    public int size() {
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

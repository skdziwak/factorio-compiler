package com.skdziwak.factoriolang.compilation;

import com.skdziwak.factoriolang.FactorioConstants;
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
            Instruction instruction = new Instruction(FactorioConstants.COPY_REG_TO_REG, reg1, reg1);
            instructions.add(instruction);
        }
    }

    public void math(Operator operator) {
        Instruction instruction = new Instruction(FactorioConstants.MATH, operator.sig);
        instructions.add(instruction);
    }

    public void setRegister(int reg, int value) {
        Instruction instruction = new Instruction(FactorioConstants.SET_REGISTER, reg, value);
        instructions.add(instruction);
    }

    public void pushReg(int reg) {
        Instruction instruction = new Instruction(FactorioConstants.PUSH_REG_TO_STACK, reg);
        instructions.add(instruction);
    }

    public void popReg(int reg) {
        Instruction lastInstruction = instructions.get(instructions.size() - 1);
        if (lastInstruction.getSignalA() == FactorioConstants.PUSH_REG_TO_STACK && lastInstruction.getSignalB() == reg && optimize) {
            instructions.remove(instructions.size() - 1);
        } else {
            Instruction instruction = new Instruction(FactorioConstants.POP_STACK_TO_REG, reg);
            instructions.add(instruction);
        }
    }

    public void copyRegisterToRAM(int reg, int ram) {
        Instruction instruction = new Instruction(FactorioConstants.COPY_REG_TO_RAM, reg, ram);
        instructions.add(instruction);
    }

    public void copyRAMtoRegister(int ram, int reg) {
        Instruction instruction = new Instruction(FactorioConstants.COPY_RAM_TO_REG, ram, reg);
        instructions.add(instruction);
    }

    public void copyInput(int in, int reg) {
        Instruction instruction = new Instruction(FactorioConstants.COPY_INPUT_TO_REG, in, reg);
        instructions.add(instruction);
    }

    public void copyOutput(int reg, int out) {
        Instruction instruction = new Instruction(FactorioConstants.COPY_REG_TO_OUTPUT, reg, out);
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

    public enum Operator {
        ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4), MODULO(5),
        EQUALS(6), NOT_EQUALS(7), GREATER(8), LOWER(9), GREATER_EQUAL(10), LOWER_EQUAL(11),
        SHIFT_LEFT(12), SHIFT_RIGHT(13), AND(14), OR(15);
        public final int sig;
        Operator(int sig) {
            this.sig = sig;
        }
    }
}

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
            Instruction instruction = new Instruction();
            instruction.signalA = FactorioConstants.COPY_REG_TO_REG;
            instruction.signalB = reg1;
            instruction.signalC = reg2;
            instructions.add(instruction);
        }
    }

    public void math(Operator operator) {
        Instruction instruction = new Instruction();
        instruction.signalA = FactorioConstants.MATH;
        instruction.signalB = operator.sig;
        instructions.add(instruction);
    }

    public void setRegister(int reg, int value) {
        Instruction instruction = new Instruction();
        instruction.signalS = reg;
        instruction.signalI = value;
        instructions.add(instruction);
    }

    public void pushReg(int reg) {
        Instruction instruction = new Instruction();
        instruction.signalA = FactorioConstants.PUSH_REG_TO_STACK;
        instruction.signalB = reg;
        instructions.add(instruction);
    }

    public void popReg(int reg) {
        Instruction lastInstruction = instructions.get(instructions.size() - 1);
        if (lastInstruction.signalA == FactorioConstants.PUSH_REG_TO_STACK && lastInstruction.signalB == reg && optimize) {
            instructions.remove(instructions.size() - 1);
        } else {
            Instruction instruction = new Instruction();
            instruction.signalA = FactorioConstants.POP_STACK_TO_REG;
            instruction.signalB = reg;
            instructions.add(instruction);
        }
    }

    public void copyRegisterToRAM(int reg, int ram) {
        Instruction instruction = new Instruction();
        instruction.signalA = FactorioConstants.COPY_REG_TO_RAM;
        instruction.signalB = reg;
        instruction.signalC = ram;
        instructions.add(instruction);
    }

    public void copyRAMtoRegister(int ram, int reg) {
        Instruction instruction = new Instruction();
        instruction.signalA = FactorioConstants.COPY_RAM_TO_REG;
        instruction.signalB = ram;
        instruction.signalC = reg;
        instructions.add(instruction);
    }

    public void copyInput(int in, int reg) {
        Instruction instruction = new Instruction();
        instruction.signalA = FactorioConstants.COPY_INPUT_TO_REG;
        instruction.signalB = in;
        instruction.signalC = reg;
        instructions.add(instruction);
    }

    public void copyOutput(int reg, int out) {
        Instruction instruction = new Instruction();
        instruction.signalA = FactorioConstants.COPY_REG_TO_OUTPUT;
        instruction.signalB = reg;
        instruction.signalC = out;
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

    public void addState(Instruction instruction) {
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

    public static class Instruction {
        public int signalA = 0;
        public int signalB = 0;
        public int signalC = 0;
        public int signalS = 0;
        public int signalI = 0;

        @Override
        public String toString() {
            return "State{" +
                    "signalA=" + signalA +
                    ", signalB=" + signalB +
                    ", signalC=" + signalC +
                    ", signalS=" + signalS +
                    ", signalI=" + signalI +
                    '}';
        }

        public Map<String, Integer> toMap() {
            Map<String, Integer> map = new HashMap<>();
            if (signalA != 0) map.put("signal-A", signalA);
            if (signalB != 0) map.put("signal-B", signalB);
            if (signalC != 0) map.put("signal-C", signalC);
            if (signalS != 0) map.put("signal-S", signalS);
            if (signalI != 0) map.put("signal-I", signalI);
            return map;
        }
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

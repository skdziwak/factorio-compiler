package com.skdziwak.factoriolang.compilation;

import com.skdziwak.factoriolang.FactorioConstants;
import com.skdziwak.factoriolang.tree.functions.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompilationState {
    private final List<State> program = new ArrayList<>();
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
            State state = new State();
            state.signalA = FactorioConstants.COPY_REG_TO_REG;
            state.signalB = reg1;
            state.signalC = reg2;
            program.add(state);
        }
    }

    public void math(Operator operator) {
        State state = new State();
        state.signalA = FactorioConstants.MATH;
        state.signalB = operator.sig;
        program.add(state);
    }

    public void setRegister(int reg, int value) {
        State state = new State();
        state.signalS = reg;
        state.signalI = value;
        program.add(state);
    }

    public void pushReg(int reg) {
        State state = new State();
        state.signalA = FactorioConstants.PUSH_REG_TO_STACK;
        state.signalB = reg;
        program.add(state);
    }

    public void popReg(int reg) {
        State lastState = program.get(program.size() - 1);
        if (lastState.signalA == FactorioConstants.PUSH_REG_TO_STACK && lastState.signalB == reg && optimize) {
            program.remove(program.size() - 1);
        } else {
            State state = new State();
            state.signalA = FactorioConstants.POP_STACK_TO_REG;
            state.signalB = reg;
            program.add(state);
        }
    }

    public void copyRegisterToRAM(int reg, int ram) {
        State state = new State();
        state.signalA = FactorioConstants.COPY_REG_TO_RAM;
        state.signalB = reg;
        state.signalC = ram;
        program.add(state);
    }

    public void copyRAMtoRegister(int ram, int reg) {
        State state = new State();
        state.signalA = FactorioConstants.COPY_RAM_TO_REG;
        state.signalB = ram;
        state.signalC = reg;
        program.add(state);
    }

    public void copyInput(int in, int reg) {
        State state = new State();
        state.signalA = FactorioConstants.COPY_INPUT_TO_REG;
        state.signalB = in;
        state.signalC = reg;
        program.add(state);
    }

    public void copyOutput(int reg, int out) {
        State state = new State();
        state.signalA = FactorioConstants.COPY_REG_TO_OUTPUT;
        state.signalB = reg;
        state.signalC = out;
        program.add(state);
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

    public void addState(State state) {
        this.program.add(state);
    }

    public String getStatesString() {
        return this.program.stream().map(String::valueOf).collect(Collectors.joining("\n"));
    }

    public int size() {
        return this.program.size();
    }

    public List<State> getProgram() {
        return program;
    }

    public FunctionContext getFunctionContext() {
        return functionContext;
    }

    public void setFunctionContext(FunctionContext functionContext) {
        this.functionContext = functionContext;
    }

    public static class State {
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

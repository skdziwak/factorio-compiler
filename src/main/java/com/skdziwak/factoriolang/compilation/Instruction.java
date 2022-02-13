package com.skdziwak.factoriolang.compilation;

import java.util.HashMap;
import java.util.Map;

public class Instruction {
    private IntegerProvider signalA;
    private IntegerProvider signalB;
    private IntegerProvider signalC;

    public Instruction(int signalA) {
        this.signalA = new ConstantInteger(signalA);
        this.signalB = new ConstantInteger(0);
        this.signalC = new ConstantInteger(0);
    }

    public Instruction(int signalA, int signalB) {
        this.signalA = new ConstantInteger(signalA);
        this.signalB = new ConstantInteger(signalB);
        this.signalC = new ConstantInteger(0);
    }

    public Instruction(int signalA, int signalB, int signalC) {
        this.signalA = new ConstantInteger(signalA);
        this.signalB = new ConstantInteger(signalB);
        this.signalC = new ConstantInteger(signalC);
    }

    public int getSignalA() {
        return signalA.getValue();
    }

    public Instruction setSignalA(int signalA) {
        this.signalA = new ConstantInteger(signalA);
        return this;
    }

    public int getSignalB() {
        return signalB.getValue();
    }

    public Instruction setSignalB(int signalB) {
        this.signalB = new ConstantInteger(signalB);
        return this;
    }

    public int getSignalC() {
        return signalC.getValue();
    }

    public Instruction setSignalC(int signalC) {
        this.signalC = new ConstantInteger(signalC);
        return this;
    }

    public Instruction setSignalA(IntegerProvider signalA) {
        this.signalA = signalA;
        return this;
    }

    public Instruction setSignalB(IntegerProvider signalB) {
        this.signalB = signalB;
        return this;
    }

    public Instruction setSignalC(IntegerProvider signalC) {
        this.signalC = signalC;
        return this;
    }

    @Override
    public String toString() {
        return "State{" +
                "signalA=" + getSignalA() +
                ", signalB=" + getSignalB() +
                ", signalC=" + getSignalC() +
                '}';
    }

    public Map<String, Integer> toMap() {
        Map<String, Integer> map = new HashMap<>();
        int a = getSignalA();
        int b = getSignalB();
        int c = getSignalC();
        if (a != 0) map.put("signal-A", a);
        if (b != 0) map.put("signal-B", b);
        if (c != 0) map.put("signal-C", c);
        return map;
    }

    public interface IntegerProvider {
        int getValue();
    }

    private record ConstantInteger(Integer value) implements IntegerProvider {
        @Override
        public int getValue() {
            return value;
        }
    }
}

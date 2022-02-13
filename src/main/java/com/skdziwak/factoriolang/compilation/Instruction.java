package com.skdziwak.factoriolang.compilation;

import java.util.HashMap;
import java.util.Map;

public class Instruction {
    private int signalA;
    private int signalB = 0;
    private int signalC = 0;

    public Instruction(int signalA) {
        this.signalA = signalA;
    }

    public Instruction(int signalA, int signalB) {
        this.signalA = signalA;
        this.signalB = signalB;
    }

    public Instruction(int signalA, int signalB, int signalC) {
        this.signalA = signalA;
        this.signalB = signalB;
        this.signalC = signalC;
    }

    public int getSignalA() {
        return signalA;
    }

    public Instruction setSignalA(int signalA) {
        this.signalA = signalA;
        return this;
    }

    public int getSignalB() {
        return signalB;
    }

    public Instruction setSignalB(int signalB) {
        this.signalB = signalB;
        return this;
    }

    public int getSignalC() {
        return signalC;
    }

    public Instruction setSignalC(int signalC) {
        this.signalC = signalC;
        return this;
    }

    @Override
    public String toString() {
        return "State{" +
                "signalA=" + signalA +
                ", signalB=" + signalB +
                ", signalC=" + signalC +
                '}';
    }

    public Map<String, Integer> toMap() {
        Map<String, Integer> map = new HashMap<>();
        if (signalA != 0) map.put("signal-A", getSignalA());
        if (signalB != 0) map.put("signal-B", getSignalB());
        if (signalC != 0) map.put("signal-C", getSignalC());
        return map;
    }
}

package com.skdziwak.factoriolang.bytecode;

import com.skdziwak.factoriolang.compilation.CompilationState;

import java.util.stream.Collectors;

public class ProgramToBytecodeConverter {
    public static String generateByteCode(CompilationState compilationState) {
        return compilationState.getInstructions()
                .stream().map(inst -> inst.getSignalA() + "," + inst.getSignalB() + "," + inst.getSignalC())
                .collect(Collectors.joining(","));
    }
}

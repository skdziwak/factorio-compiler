package com.skdziwak.factoriolang;

import com.skdziwak.factoriolang.compilation.CompilationState;

import java.util.Arrays;

public final class AssemblyHumanizer {
    public static String humanizeCompilationState(CompilationState compilationState) {
        StringBuilder result = new StringBuilder();

        result.append("Program length: ").append(compilationState.size()).append("\n");
        result.append("Instructions: ").append("\n");
        int i = 1;
        for (CompilationState.State state : compilationState.getProgram()) {
            result.append("\t[").append(i).append("] ");
            if (i < 100) result.append(" ");
            if (i < 10) result.append(" ");
            result.append(humanizeState(state, i)).append("\n");
            i++;
        }

        return result.toString();
    }

    private static String humanizeState(CompilationState.State state, int index) {
        switch (state.signalA) {
            case 1:
                return "Copy register " + state.signalB + " to register " + state.signalC;
            case 2:
                return "Math operation: (register 1) <- (register 1) " + humanizeOperation(state.signalB) + " (register 2)";
            case 3:
                return "Push register " + state.signalB + " to stack";
            case 4:
                return "Pop stack to register " + state.signalB;
            case 5:
                return "Copy register " + state.signalB + " to RAM(" + state.signalC + ")";
            case 6:
                return "Copy RAM(" + state.signalB + ") to register " + state.signalC;
            case 7:
                return "Jump to " + (index + state.signalB + 1);
            case 8:
                return "Jump to " + (index + state.signalB + 1) + " if register 1 is equal to 0";
            case 9:
                return "Jump with dynamic offset from register 8";
            case 10:
                return "Jump with dynamic offset from register 8 if register 1 is equal to 0";
        }
        if (state.signalS != 0) {
            return "Assign value " + state.signalI + " to register " + state.signalS;
        }
        return "[UNKNOWN OPERATION]";
    }

    private static String humanizeOperation(Integer operator) {
        return Arrays.stream(CompilationState.Operator.values()).filter(op -> op.sig == operator)
                .findFirst().orElseThrow(() -> new HumanizerException("No such operator " + operator))
                .toString();
    }

    private static class HumanizerException extends RuntimeException {
        public HumanizerException(String message) {
            super(message);
        }
    }
}

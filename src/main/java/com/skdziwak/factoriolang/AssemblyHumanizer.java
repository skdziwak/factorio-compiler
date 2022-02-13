package com.skdziwak.factoriolang;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.Instruction;

import java.util.Arrays;

public final class AssemblyHumanizer {
    public static String humanizeCompilationState(CompilationState compilationState) {
        StringBuilder result = new StringBuilder();

        result.append("Program length: ").append(compilationState.size()).append("\n");
        result.append("Instructions: ").append("\n");
        int i = 1;
        for (Instruction instruction : compilationState.getInstructions()) {
            result.append("\t[").append(i).append("] ");
            if (i < 100) result.append(" ");
            if (i < 10) result.append(" ");
            result.append(humanizeState(instruction, i)).append("\n");
            i++;
        }

        return result.toString();
    }

    private static String humanizeState(Instruction instruction, int index) {
        switch (instruction.getSignalA()) {
            case 1:
                return "Copy register " + instruction.getSignalB() + " to register " + instruction.getSignalC();
            case 2:
                return "Math operation: (register 1) <- (register 1) " + humanizeOperation(instruction.getSignalB()) + " (register 2)";
            case 3:
                return "Push register " + instruction.getSignalB() + " to stack";
            case 4:
                return "Pop stack to register " + instruction.getSignalB();
            case 5:
                return "Copy register " + instruction.getSignalB() + " to RAM(" + instruction.getSignalC() + ")";
            case 6:
                return "Copy RAM(" + instruction.getSignalB() + ") to register " + instruction.getSignalC();
            case 7:
                return "Jump to " + (index + instruction.getSignalB() + 1);
            case 8:
                return "Jump to " + (index + instruction.getSignalB() + 1) + " if register 1 is equal to 0";
            case 9:
                return "Jump with dynamic offset from register 8";
            case 10:
                return "Jump with dynamic offset from register 8 if register 1 is equal to 0";
            case 13:
                return "Assign value " + instruction.getSignalC() + " to register " + instruction.getSignalB();
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

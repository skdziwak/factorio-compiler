package com.skdziwak.factoriolang;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.Instruction;
import com.skdziwak.factoriolang.constants.MathOperator;

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

    public static String humanizeState(Instruction instruction, int index) {
        return switch (instruction.getSignalA()) {
            case 1 -> "Copy register " + instruction.getSignalB() + " to register " + instruction.getSignalC();
            case 2 -> "Math operation: (register 1) <- (register 1) " + humanizeOperation(instruction.getSignalB()) + " (register 2)";
            case 3 -> "Push register " + instruction.getSignalB() + " to stack";
            case 4 -> "Pop stack to register " + instruction.getSignalB();
            case 5 -> "Copy register " + instruction.getSignalB() + " to RAM(" + instruction.getSignalC() + ")";
            case 6 -> "Copy RAM(" + instruction.getSignalB() + ") to register " + instruction.getSignalC();
            case 7 -> "Jump to " + (index + instruction.getSignalB() + 1);
            case 8 -> "Jump to " + (index + instruction.getSignalB() + 1) + " if register 1 is equal to 0";
            case 9 -> "Jump with dynamic offset from register 8";
            case 10 -> "Jump with dynamic offset from register 8 if register 1 is equal to 0";
            case 13 -> "Assign value " + instruction.getSignalC() + " to register " + instruction.getSignalB();
            default -> "[UNKNOWN OPERATION]";
        };
    }

    private static String humanizeOperation(Integer operator) {
        return Arrays.stream(MathOperator.values()).filter(op -> op.sig == operator)
                .findFirst().orElseThrow(() -> new HumanizerException("No such operator " + operator))
                .toString();
    }

    private static class HumanizerException extends RuntimeException {
        public HumanizerException(String message) {
            super(message);
        }
    }
}

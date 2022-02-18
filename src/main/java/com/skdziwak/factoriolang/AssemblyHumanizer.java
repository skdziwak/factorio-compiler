package com.skdziwak.factoriolang;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.Instruction;
import com.skdziwak.factoriolang.constants.InstructionType;
import com.skdziwak.factoriolang.constants.MathOperator;

import java.util.Arrays;

public final class AssemblyHumanizer {
    public static String humanizeCompilationState(CompilationState compilationState) {
        StringBuilder result = new StringBuilder();

        result.append("Program length: ").append(compilationState.getNextIndex()).append("\n");
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
        return switch (InstructionType.bySignal(instruction.getSignalA())) {
            case COPY_REG_TO_REG -> "Copy register " + instruction.getSignalB() + " to register " + instruction.getSignalC();
            case MATH -> "Math operation: (register 1) <- (register 1) " + humanizeOperation(instruction.getSignalB()) + " (register 2)";
            case PUSH_REG_TO_STACK -> "Push register " + instruction.getSignalB() + " to stack";
            case POP_STACK_TO_REG -> "Pop stack to register " + instruction.getSignalB();
            case COPY_REG_TO_RAM -> "Copy register " + instruction.getSignalB() + " to RAM(" + instruction.getSignalC() + ")";
            case COPY_RAM_TO_REG -> "Copy RAM(" + instruction.getSignalB() + ") to register " + instruction.getSignalC();
            case JUMP_CONSTANT_OFFSET -> "Jump to " + (index + instruction.getSignalB() + 1);
            case CONDITIONAL_JUMP_CONSTANT_OFFSET -> "Jump to " + (index + instruction.getSignalB() + 1) + " if register 1 is equal to 0";
            case JUMP_DYNAMIC_OFFSET -> "Jump with dynamic offset from register 8";
            case CONDITIONAL_JUMP_DYNAMIC_OFFSET -> "Jump with dynamic offset from register 8 if register 1 is equal to 0";
            case SET_REGISTER -> "Assign value " + instruction.getSignalC() + " to register " + instruction.getSignalB();
            case COPY_REG_TO_RAM_DYNAMICALLY -> "Copy register " + instruction.getSignalB() + " to RAM(register " + instruction.getSignalC() + ")";
            case COPY_RAM_TO_REG_DYNAMICALLY -> "Copy RAM(register " + instruction.getSignalB() + ") to register " + instruction.getSignalC();
            case COPY_REG_TO_OUTPUT -> "Copy register " + instruction.getSignalB() + " to output " + instruction.getSignalC();
            case COPY_INPUT_TO_REG -> "Copy input " + instruction.getSignalB() + " to register " + instruction.getSignalC();
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

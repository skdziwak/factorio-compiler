package com.skdziwak.factoriolang.constants;

import java.util.Arrays;

public enum InstructionType {
    COPY_REG_TO_REG(1),
    MATH(2),
    PUSH_REG_TO_STACK(3),
    POP_STACK_TO_REG(4),
    COPY_REG_TO_RAM(5),
    COPY_RAM_TO_REG(6),
    JUMP_CONSTANT_OFFSET(7),
    CONDITIONAL_JUMP_CONSTANT_OFFSET(8),
    JUMP_DYNAMIC_OFFSET(9),
    CONDITIONAL_JUMP_DYNAMIC_OFFSET(10),
    COPY_INPUT_TO_REG(11),
    COPY_REG_TO_OUTPUT(12),
    SET_REGISTER(13);

    private final int signal;

    InstructionType(int signal) {
        this.signal = signal;
    }

    public int getSignal() {
        return signal;
    }

    public static InstructionType bySignal(int signal) {
        return Arrays.stream(InstructionType.values()).filter(t -> t.signal == signal).findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid InstructionType"));
    }
}

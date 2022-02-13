package com.skdziwak.factoriolang;

public final class FactorioConstants {
    // INSTRUCTIONS
    public static final int
            COPY_REG_TO_REG = 1,
            MATH = 2,
            PUSH_REG_TO_STACK = 3,
            POP_STACK_TO_REG = 4,
            COPY_REG_TO_RAM = 5,
            COPY_RAM_TO_REG = 6,
            JUMP_CONSTANT_OFFSET = 7,
            CONDITIONAL_JUMP_CONSTANT_OFFSET = 8,
            JUMP_DYNAMIC_OFFSET = 9,
            CONDITIONAL_JUMP_DYNAMIC_OFFSET = 10,
            COPY_INPUT_TO_REG = 11,
            COPY_REG_TO_OUTPUT = 12,
            SET_REGISTER = 13;

    // MATH
    public static final int
            ADD = 1,
            SUBTRACT = 2,
            MULTIPLY = 3,
            DIVIDE = 4,
            MODULO = 5,
            EQUALS = 6,
            NOT_EQUALS = 7,
            GREATER = 8,
            LOWER = 9,
            GREATER_EQUAL = 10,
            LOWER_EQUAL = 11,
            SHIFT_LEFT = 12,
            SHIFT_RIGHT = 13,
            AND_OPERATOR = 14,
            OR_OPERATOR = 15;

    // CPU PARAMS
    public static final int
            STACK_SIZE = 32,
            RAM_SIZE = 256,
            REGISTERS_COUNT = 8;
    public static final int[] FUNCTION_ARG_REGISTERS = new int[] {3, 4, 5, 6, 7};
}

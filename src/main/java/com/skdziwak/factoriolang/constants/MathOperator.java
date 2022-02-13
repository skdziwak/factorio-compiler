package com.skdziwak.factoriolang.constants;

public enum MathOperator {
    ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4), MODULO(5),
    EQUALS(6), NOT_EQUALS(7), GREATER(8), LOWER(9), GREATER_EQUAL(10), LOWER_EQUAL(11),
    SHIFT_LEFT(12), SHIFT_RIGHT(13), AND(14), OR(15);
    public final int sig;

    MathOperator(int sig) {
        this.sig = sig;
    }

    public int getSignal() {
        return this.sig;
    }
}

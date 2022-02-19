package com.skdziwak.factoriolang.tree.statements;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.Instruction;
import com.skdziwak.factoriolang.constants.InstructionType;
import com.skdziwak.factoriolang.constants.MathOperator;
import com.skdziwak.factoriolang.tree.Expression;
import com.skdziwak.factoriolang.tree.Statement;

public class OutputArrayAssignmentStatement extends Statement {
    private final int start, end;
    private final Expression addressExpression;

    public OutputArrayAssignmentStatement(int start, int end, Expression addressExpression) {
        this.start = start;
        this.end = end;
        this.addressExpression = addressExpression;
    }

    @Override
    public void compile(CompilationState state) {
        int size = end - start + 1;
        addressExpression.compile(state);
        state.popReg(1);
        state.setRegister(2, 1);
        for (int i = 0 ; i < size ; i++) {
            Instruction instruction = new Instruction(InstructionType.COPY_RAM_TO_REG_DYNAMICALLY, 1, 4);
            state.addInstruction(instruction);
            state.copyOutput(4, start + i);
            if (i + 1 != size) {
                state.math(MathOperator.ADD);
            }
        }
    }
}

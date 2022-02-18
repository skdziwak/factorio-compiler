package com.skdziwak.factoriolang.parser.visitors;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.Instruction;
import com.skdziwak.factoriolang.constants.InstructionType;
import com.skdziwak.factoriolang.tree.Expression;

public class DereferenceExpression extends Expression {
    private final Expression addressExpression;

    public DereferenceExpression(Expression addressExpression) {
        this.addressExpression = addressExpression;
    }

    @Override
    public void compile(CompilationState state) {
        addressExpression.compile(state);
        state.popReg(1);
        Instruction instruction = new Instruction(InstructionType.COPY_RAM_TO_REG_DYNAMICALLY, 1, 2);
        state.addInstruction(instruction);
        state.pushReg(2);
    }
}

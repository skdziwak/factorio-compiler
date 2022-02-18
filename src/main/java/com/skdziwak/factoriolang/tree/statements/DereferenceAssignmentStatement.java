package com.skdziwak.factoriolang.tree.statements;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.Instruction;
import com.skdziwak.factoriolang.constants.InstructionType;
import com.skdziwak.factoriolang.tree.Expression;
import com.skdziwak.factoriolang.tree.Statement;

public class DereferenceAssignmentStatement extends Statement {
    private final Expression addressExpression;
    private final Expression valueExpression;

    public DereferenceAssignmentStatement(Expression addressExpression, Expression valueExpression) {
        this.addressExpression = addressExpression;
        this.valueExpression = valueExpression;
    }

    @Override
    public void compile(CompilationState state) {
        addressExpression.compile(state);
        state.popReg(2);
        valueExpression.compile(state);
        state.popReg(1);
        Instruction instruction = new Instruction(InstructionType.COPY_REG_TO_RAM_DYNAMICALLY, 1, 2);
        state.addInstruction(instruction);
    }
}

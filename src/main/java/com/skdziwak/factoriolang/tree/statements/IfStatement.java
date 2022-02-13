package com.skdziwak.factoriolang.tree.statements;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.tree.Expression;
import com.skdziwak.factoriolang.tree.Statement;

public class IfStatement extends Statement {
    private final Expression condition;
    private final Statement statement;

    public IfStatement(Expression condition, Statement statement) {
        this.condition = condition;
        this.statement = statement;
    }


    @Override
    public void compile(CompilationState state) {
        condition.compile(state);
        state.popReg(1);

        int jumpIndex = state.size();
        CompilationState.Instruction jumpInstruction = new CompilationState.Instruction();
        jumpInstruction.signalA = 8;
        state.addState(jumpInstruction);

        statement.compile(state);
        int endIndex = state.size();
        jumpInstruction.signalB = endIndex - jumpIndex - 1;
    }
}

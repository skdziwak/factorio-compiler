package com.skdziwak.factoriolang.tree.statements;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.tree.Expression;
import com.skdziwak.factoriolang.tree.Statement;

public class WhileStatement extends Statement {
    private final Expression condition;
    private final Statement statement;

    public WhileStatement(Expression condition, Statement statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public void compile(CompilationState state) {
        int startIndex = state.size();
        condition.compile(state);
        state.popReg(1);

        int escapeStateIndex = state.size();
        CompilationState.Instruction escapeInstruction = new CompilationState.Instruction();
        escapeInstruction.signalA = 8;
        state.addState(escapeInstruction);

        statement.compile(state);

        int loopIndex = state.size();
        CompilationState.Instruction loopInstruction = new CompilationState.Instruction();
        loopInstruction.signalA = 7;
        state.addState(loopInstruction);
        int endIndex = state.size();

        escapeInstruction.signalB = endIndex - escapeStateIndex - 1;
        loopInstruction.signalB = startIndex - loopIndex - 1;
    }
}

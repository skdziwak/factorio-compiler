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
        CompilationState.State escapeState = new CompilationState.State();
        escapeState.signalA = 8;
        state.addState(escapeState);

        statement.compile(state);

        int loopIndex = state.size();
        CompilationState.State loopState = new CompilationState.State();
        loopState.signalA = 7;
        state.addState(loopState);
        int endIndex = state.size();

        escapeState.signalB = endIndex - escapeStateIndex - 1;
        loopState.signalB = startIndex - loopIndex - 1;
    }
}

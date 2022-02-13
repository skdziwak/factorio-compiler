package com.skdziwak.factoriolang.tree.statements;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.tree.Expression;
import com.skdziwak.factoriolang.tree.Statement;

public class IfElseStatement extends Statement {
    private final Expression condition;
    private final Statement positive;
    private final Statement negative;

    public IfElseStatement(Expression condition, Statement positive, Statement negative) {
        this.condition = condition;
        this.positive = positive;
        this.negative = negative;
    }

    @Override
    public void compile(CompilationState state) {
        condition.compile(state);
        state.popReg(1);

        int ifIndex = state.size();
        CompilationState.State ifState = new CompilationState.State();
        ifState.signalA = 8;

        CompilationState.State ifEscapeState = new CompilationState.State();
        ifEscapeState.signalA = 7;

        state.addState(ifState);
        positive.compile(state);
        int ifEscapeIndex = state.size();
        state.addState(ifEscapeState);
        negative.compile(state);
        int endIndex = state.size();

        ifState.signalB = endIndex - ifIndex - 1;
        ifEscapeState.signalB = endIndex - ifEscapeIndex - 1;
    }
}

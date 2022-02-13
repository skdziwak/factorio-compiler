package com.skdziwak.factoriolang.tree.statements;

import com.skdziwak.factoriolang.compilation.Compilable;
import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.tree.Expression;
import com.skdziwak.factoriolang.tree.Statement;

public class OutputStatement extends Statement {
    private final Integer outputId;
    private final Expression expression;

    public OutputStatement(Integer outputId, Expression expression) {
        this.outputId = outputId;
        this.expression = expression;
    }

    @Override
    public void compile(CompilationState state) {
        expression.compile(state);
        state.popReg(1);
        state.copyOutput(1, outputId);
    }
}

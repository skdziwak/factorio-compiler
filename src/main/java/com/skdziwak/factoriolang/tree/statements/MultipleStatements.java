package com.skdziwak.factoriolang.tree.statements;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.interfaces.PreCompilable;
import com.skdziwak.factoriolang.tree.Statement;

import java.util.List;

public class MultipleStatements extends Statement implements PreCompilable {
    private final List<Statement> statementList;

    public MultipleStatements(List<Statement> statementList) {
        this.statementList = statementList;
    }

    @Override
    public void compile(CompilationState state) {
        for (Statement statement : statementList) {
            statement.compile(state);
        }
    }

    @Override
    public void preCompile(CompilationState state) {
        for (Statement statement : statementList) {
            if (statement instanceof PreCompilable preCompilable) {
                preCompilable.preCompile(state);
            }
        }
    }
}

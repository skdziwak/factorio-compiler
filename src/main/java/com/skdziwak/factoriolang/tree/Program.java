package com.skdziwak.factoriolang.tree;

import com.skdziwak.factoriolang.FactorioConstants;
import com.skdziwak.factoriolang.compilation.Compilable;
import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.Instruction;
import com.skdziwak.factoriolang.tree.functions.Function;

import java.util.ArrayList;
import java.util.List;

public class Program implements Compilable {
    private final List<Function> functions = new ArrayList<>();
    private final List<Statement> statements = new ArrayList<>();

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    public void addFunction(Function function) {
        functions.add(function);
    }

    @Override
    public void compile(CompilationState state) {
        Instruction initialJump = new Instruction(FactorioConstants.JUMP_CONSTANT_OFFSET);
        state.addState(initialJump);
        this.functions.forEach(function -> function.compile(state));
        initialJump.setSignalB(state.size() - 1);
        this.statements.forEach(statement -> statement.compile(state));
    }
}

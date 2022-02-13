package com.skdziwak.factoriolang.tree;

import com.skdziwak.factoriolang.compilation.interfaces.Compilable;
import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.Instruction;
import com.skdziwak.factoriolang.compilation.interfaces.PostCompilable;
import com.skdziwak.factoriolang.compilation.interfaces.PreCompilable;
import com.skdziwak.factoriolang.constants.InstructionType;
import com.skdziwak.factoriolang.tree.functions.Function;

import java.util.ArrayList;
import java.util.List;

public class Program implements Compilable, PreCompilable, PostCompilable {
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
        Instruction initialJump = new Instruction(InstructionType.JUMP_CONSTANT_OFFSET);
        state.addInstruction(initialJump);
        this.functions.forEach(function -> function.compile(state));
        initialJump.setSignalB(state.size() - 1);
        this.statements.forEach(statement -> statement.compile(state));
    }

    @Override
    public void postCompile(CompilationState state) {
        this.functions.forEach(compilable -> {
            if (compilable instanceof PostCompilable postCompilable) {
                postCompilable.postCompile(state);
            }
        });
        this.statements.forEach(compilable -> {
            if (compilable instanceof PostCompilable postCompilable) {
                postCompilable.postCompile(state);
            }
        });
    }

    @Override
    public void preCompile(CompilationState state) {
        this.functions.forEach(function -> function.preCompile(state));
        this.statements.forEach(compilable -> {
            if (compilable instanceof PreCompilable preCompilable) {
                preCompilable.preCompile(state);
            }
        });
    }
}

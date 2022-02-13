package com.skdziwak.factoriolang.tree.statements;

import com.skdziwak.factoriolang.FactorioConstants;
import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.Instruction;
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
        Instruction escapeInstruction = new Instruction(FactorioConstants.CONDITIONAL_JUMP_CONSTANT_OFFSET);
        state.addState(escapeInstruction);

        statement.compile(state);

        int loopIndex = state.size();
        Instruction loopInstruction = new Instruction(FactorioConstants.JUMP_CONSTANT_OFFSET);
        state.addState(loopInstruction);
        int endIndex = state.size();

        escapeInstruction.setSignalB(endIndex - escapeStateIndex - 1);
        loopInstruction.setSignalB(startIndex - loopIndex - 1);
    }
}

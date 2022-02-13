package com.skdziwak.factoriolang.tree.statements;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.Instruction;
import com.skdziwak.factoriolang.constants.InstructionType;
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
        int startIndex = state.getNextIndex();
        condition.compile(state);
        state.popReg(1);

        int escapeStateIndex = state.getNextIndex();
        Instruction escapeInstruction = new Instruction(InstructionType.CONDITIONAL_JUMP_CONSTANT_OFFSET);
        state.addInstruction(escapeInstruction);

        statement.compile(state);

        int loopIndex = state.getNextIndex();
        Instruction loopInstruction = new Instruction(InstructionType.JUMP_CONSTANT_OFFSET);
        state.addInstruction(loopInstruction);
        int endIndex = state.getNextIndex();

        escapeInstruction.setSignalB(endIndex - escapeStateIndex - 1);
        loopInstruction.setSignalB(startIndex - loopIndex - 1);
    }
}

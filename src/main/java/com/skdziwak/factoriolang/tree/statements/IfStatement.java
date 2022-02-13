package com.skdziwak.factoriolang.tree.statements;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.Instruction;
import com.skdziwak.factoriolang.constants.InstructionType;
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
        Instruction jumpInstruction = new Instruction(InstructionType.CONDITIONAL_JUMP_CONSTANT_OFFSET);
        state.addInstruction(jumpInstruction);

        statement.compile(state);
        int endIndex = state.size();
        jumpInstruction.setSignalB(endIndex - jumpIndex - 1);
    }
}

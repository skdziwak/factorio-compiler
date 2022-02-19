package com.skdziwak.factoriolang.tree.statements;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.Instruction;
import com.skdziwak.factoriolang.compilation.interfaces.PreCompilable;
import com.skdziwak.factoriolang.constants.InstructionType;
import com.skdziwak.factoriolang.tree.Expression;
import com.skdziwak.factoriolang.tree.Statement;

public class IfStatement extends Statement implements PreCompilable {
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

        int jumpIndex = state.getNextIndex();
        Instruction jumpInstruction = new Instruction(InstructionType.CONDITIONAL_JUMP_CONSTANT_OFFSET);
        state.addInstruction(jumpInstruction);

        statement.compile(state);
        int endIndex = state.getNextIndex();
        jumpInstruction.setSignalB(endIndex - jumpIndex - 1);
    }

    @Override
    public void preCompile(CompilationState state) {
        if (statement instanceof PreCompilable preCompilable) {
            preCompilable.preCompile(state);
        }
    }
}

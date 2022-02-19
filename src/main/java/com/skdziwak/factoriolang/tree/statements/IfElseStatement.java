package com.skdziwak.factoriolang.tree.statements;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.Instruction;
import com.skdziwak.factoriolang.compilation.interfaces.PreCompilable;
import com.skdziwak.factoriolang.constants.InstructionType;
import com.skdziwak.factoriolang.tree.Expression;
import com.skdziwak.factoriolang.tree.Statement;

public class IfElseStatement extends Statement implements PreCompilable {
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

        int ifIndex = state.getNextIndex();
        Instruction ifInstruction = new Instruction(InstructionType.CONDITIONAL_JUMP_CONSTANT_OFFSET);
        Instruction ifEscapeInstruction = new Instruction(InstructionType.JUMP_CONSTANT_OFFSET);

        state.addInstruction(ifInstruction);
        positive.compile(state);
        int ifEscapeIndex = state.getNextIndex();
        state.addInstruction(ifEscapeInstruction);
        int negativeIndex = state.getNextIndex();
        negative.compile(state);
        int endIndex = state.getNextIndex();

        ifInstruction.setSignalB(negativeIndex - ifIndex - 1);
        ifEscapeInstruction.setSignalB(endIndex - ifEscapeIndex - 1);
    }

    @Override
    public void preCompile(CompilationState state) {
        if (positive instanceof PreCompilable preCompilable) {
            preCompilable.preCompile(state);
        }
        if (negative instanceof PreCompilable preCompilable) {
            preCompilable.preCompile(state);
        }
    }
}

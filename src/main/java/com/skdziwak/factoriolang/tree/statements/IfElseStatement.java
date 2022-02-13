package com.skdziwak.factoriolang.tree.statements;

import com.skdziwak.factoriolang.FactorioConstants;
import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.Instruction;
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
        Instruction ifInstruction = new Instruction(FactorioConstants.CONDITIONAL_JUMP_CONSTANT_OFFSET);
        Instruction ifEscapeInstruction = new Instruction(FactorioConstants.JUMP_CONSTANT_OFFSET);

        state.addInstruction(ifInstruction);
        positive.compile(state);
        int ifEscapeIndex = state.size();
        state.addInstruction(ifEscapeInstruction);
        negative.compile(state);
        int endIndex = state.size();

        ifInstruction.setSignalB(endIndex - ifIndex - 1);
        ifEscapeInstruction.setSignalB(endIndex - ifEscapeIndex - 1);
    }
}

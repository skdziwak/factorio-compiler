package com.skdziwak.factoriolang.tree.expressions;

import com.skdziwak.factoriolang.FactorioConstants;
import com.skdziwak.factoriolang.compilation.CompilationException;
import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.FunctionContext;
import com.skdziwak.factoriolang.tree.Expression;
import com.skdziwak.factoriolang.tree.functions.Function;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FunctionCall extends Expression {

    private final String identifier;
    private final List<Expression> arguments;

    public FunctionCall(String identifier, List<Expression> arguments) {
        this.identifier = identifier;
        this.arguments = arguments;
    }

    @Override
    public void compile(CompilationState state) {
        FunctionContext functionContext = state.getFunctionContext();

        if (functionContext != null) {
            Arrays.stream(FactorioConstants.FUNCTION_ARG_REGISTERS, 0, functionContext.numberOfVariables()).forEach(state::pushReg);
        }

        Function function = state.getFunction(identifier);
        if (function == null) {
            throw new CompilationException("Function " + identifier + " not found.");
        }
        if (function.getArguments().size() != this.arguments.size()) {
            throw new CompilationException("Function " + identifier + " can not be called like that. Arguments count does not match.");
        }

        for (int i = 0 ; i < arguments.size() ; i++) {
            arguments.get(i).compile(state);
            state.popReg(1);
            state.copyRegister(1, FactorioConstants.FUNCTION_ARG_REGISTERS[i]);
        }

        int jumpStateIndex = state.size() + 2;
        state.setRegister(1, jumpStateIndex - function.getEndIndex());
        state.pushReg(1);

        CompilationState.Instruction jumpInstruction = new CompilationState.Instruction();
        jumpInstruction.signalA = FactorioConstants.JUMP_CONSTANT_OFFSET;
        jumpInstruction.signalB = function.getStartIndex() - jumpStateIndex - 1;
        state.addState(jumpInstruction);

        if (functionContext != null) {
            Arrays.stream(FactorioConstants.FUNCTION_ARG_REGISTERS, 0, functionContext.numberOfVariables()).boxed().sorted(Collections.reverseOrder()).forEach(state::popReg);
        }

        state.pushReg(1);
    }
}

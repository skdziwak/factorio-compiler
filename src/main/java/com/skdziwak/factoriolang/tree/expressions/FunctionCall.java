package com.skdziwak.factoriolang.tree.expressions;

import com.skdziwak.factoriolang.HardwareConstants;
import com.skdziwak.factoriolang.compilation.CompilationException;
import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.FunctionContext;
import com.skdziwak.factoriolang.compilation.Instruction;
import com.skdziwak.factoriolang.constants.InstructionType;
import com.skdziwak.factoriolang.tree.Expression;
import com.skdziwak.factoriolang.tree.functions.Function;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
            Arrays.stream(HardwareConstants.FUNCTION_ARG_REGISTERS, 0, functionContext.numberOfVariables()).forEach(state::pushReg);
        }

        Function function = state.getFunction(identifier);
        if (function == null) {
            throw new CompilationException("Function " + identifier + " not found.");
        }
        if (function.getArguments().size() != this.arguments.size()) {
            throw new CompilationException("Function " + identifier + " can not be called like that. Arguments count does not match.");
        }

        AtomicReference<Integer> jumpStateIndexReference = new AtomicReference<>();
        state.addInstruction(new Instruction(InstructionType.SET_REGISTER, 1).setSignalC(() -> jumpStateIndexReference.get() - function.getEndIndex()));
        state.pushReg(1);

        for (Expression argument : arguments) {
            argument.compile(state);
        }

        jumpStateIndexReference.set(state.getNextIndex());
        Instruction jumpInstruction = new Instruction(InstructionType.JUMP_CONSTANT_OFFSET)
                .setSignalB(() -> function.getStartIndex() - jumpStateIndexReference.get() - 1);
        state.addInstruction(jumpInstruction);

        if (functionContext != null) {
            Arrays.stream(HardwareConstants.FUNCTION_ARG_REGISTERS, 0, functionContext.numberOfVariables()).boxed().sorted(Collections.reverseOrder()).forEach(state::popReg);
        }

        state.pushReg(1);
    }
}

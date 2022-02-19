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
import java.util.stream.IntStream;

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

        List<String> vars = null;

        if (functionContext != null) {
            vars = functionContext.getVariablesList();
            vars.forEach(var -> {
                state.copyRAMtoRegister(functionContext.getVariableAddress(var), 1);
                state.pushReg(1);
            });
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
        state.copyRegister(1, 3);

        if (functionContext != null) {
            Collections.reverse(vars);
            vars.forEach(var -> {
                state.popReg(1);
                state.copyRegisterToRAM(1, functionContext.getVariableAddress(var));
            });
        }

        state.pushReg(3);
    }
}

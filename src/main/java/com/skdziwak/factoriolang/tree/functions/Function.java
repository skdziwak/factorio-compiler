package com.skdziwak.factoriolang.tree.functions;

import com.skdziwak.factoriolang.HardwareConstants;
import com.skdziwak.factoriolang.compilation.*;
import com.skdziwak.factoriolang.compilation.interfaces.Compilable;
import com.skdziwak.factoriolang.compilation.interfaces.PreCompilable;
import com.skdziwak.factoriolang.constants.InstructionType;
import com.skdziwak.factoriolang.tree.Statement;

import java.util.List;

public class Function implements Compilable, PreCompilable {
    private final String identifier;
    private final List<String> arguments;
    private final List<Statement> statements;
    private final String returns;
    private Integer startIndex, endIndex;
    private final FunctionContext functionContext;

    public Function(String identifier, List<String> arguments, List<Statement> statements, String returns) {
        this.identifier = identifier;
        this.arguments = arguments;
        this.statements = statements;
        this.returns = returns;
        functionContext = new FunctionContext();
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    @Override
    public void compile(CompilationState state) {
        arguments.forEach(arg -> functionContext.declareVariable(state, arg));

        this.startIndex = state.getNextIndex();
        state.setFunctionContext(functionContext);

        for (int i = arguments.size() - 1 ; i >= 0 ; i--) {
            state.popReg(1);
            state.copyRegisterToRAM(1, functionContext.getVariableAddress(arguments.get(i)));
        }

        statements.forEach(statement -> statement.compile(state));

        if (this.returns != null) {
            if (functionContext.containsVariable(returns)) {
                state.copyRAMtoRegister(functionContext.getVariableAddress(returns), 1);
            } else {
                throw new CompilationException("Unable to return variable " + returns + " from function " + identifier
                        + "(" + String.join(", ", arguments) + ")");
            }
        }

        state.setFunctionContext(null);
        state.popReg(8);
        Instruction jumpInstruction = new Instruction(InstructionType.JUMP_DYNAMIC_OFFSET);
        state.addInstruction(jumpInstruction);
        this.endIndex = state.getNextIndex() - 1;
    }

    @Override
    public void preCompile(CompilationState state) {
        state.setFunctionContext(functionContext);
        state.addFunction(this);
        for (Statement statement : this.statements) {
            if (statement instanceof PreCompilable preCompilable) {
                preCompilable.preCompile(state);
            }
        }
        state.setFunctionContext(null);
    }
}

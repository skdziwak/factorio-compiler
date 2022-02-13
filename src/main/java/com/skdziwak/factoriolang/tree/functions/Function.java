package com.skdziwak.factoriolang.tree.functions;

import com.skdziwak.factoriolang.FactorioConstants;
import com.skdziwak.factoriolang.compilation.*;
import com.skdziwak.factoriolang.tree.Statement;

import java.util.List;

public class Function implements Compilable {
    private final String identifier;
    private final List<String> arguments;
    private final List<Statement> statements;
    private final String returns;
    private Integer startIndex, endIndex;

    public Function(String identifier, List<String> arguments, List<Statement> statements, String returns) {
        this.identifier = identifier;
        this.arguments = arguments;
        this.statements = statements;
        this.returns = returns;
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
        FunctionContext functionContext = new FunctionContext();
        arguments.forEach(functionContext::declareVariable);

        this.startIndex = state.size();
        state.setFunctionContext(functionContext);

        statements.forEach(statement -> statement.compile(state));

        if (this.returns != null) {
            if (functionContext.containsVariable(returns)) {
                state.copyRegister(
                        FactorioConstants.FUNCTION_ARG_REGISTERS[functionContext.indexOfVariable(returns)], 1
                );
            } else {
                throw new CompilationException("Unable to return variable " + returns + " from function " + identifier
                        + "(" + String.join(", ", arguments) + ")");
            }
        }

        state.setFunctionContext(null);
        state.popReg(8);
        Instruction jumpInstruction = new Instruction(FactorioConstants.JUMP_DYNAMIC_OFFSET);
        state.addState(jumpInstruction);
        state.addFunction(this);
        this.endIndex = state.size() - 1;
    }
}

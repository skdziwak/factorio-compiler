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

        this.startIndex = state.getNextIndex();
        state.setFunctionContext(functionContext);

        for (int i = arguments.size() - 1 ; i >= 0 ; i--) {
            state.popReg(1);
            state.copyRegister(1, HardwareConstants.FUNCTION_ARG_REGISTERS[i]);
        }

        statements.forEach(statement -> statement.compile(state));

        if (this.returns != null) {
            if (functionContext.containsVariable(returns)) {
                state.copyRegister(
                        HardwareConstants.FUNCTION_ARG_REGISTERS[functionContext.indexOfVariable(returns)], 1
                );
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
        state.addFunction(this);
    }
}

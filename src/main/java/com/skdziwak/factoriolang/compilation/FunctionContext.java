package com.skdziwak.factoriolang.compilation;

import com.skdziwak.factoriolang.HardwareConstants;

import java.util.ArrayList;
import java.util.List;

public class FunctionContext {
    private final List<String> variables = new ArrayList<>();

    public List<String> getVariables() {
        return variables;
    }

    public boolean containsVariable(String identifier) {
        return variables.contains(identifier);
    }

    public int indexOfVariable(String identifier) {
        return variables.indexOf(identifier);
    }

    public int declareVariable(String identifier) {
        if (variables.contains(identifier)) {
            throw new CompilationException("Variable " + identifier + " is already defined in local scope!");
        } else {
            int maxVariables = HardwareConstants.FUNCTION_ARG_REGISTERS.length;
            if (variables.size() >= maxVariables) {
                throw new CompilationException("Too many variables. You can use up to " + maxVariables + " local variables including function arguments.");
            } else {
                try {
                    return variables.size();
                } finally {
                    variables.add(identifier);
                }
            }
        }
    }

    public int numberOfVariables() {
        return this.variables.size();
    }
}

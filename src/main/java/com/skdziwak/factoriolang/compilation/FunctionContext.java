package com.skdziwak.factoriolang.compilation;

import com.skdziwak.factoriolang.HardwareConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionContext {
    private final Map<String, Integer> variableAddresses = new HashMap<>();

    public List<String> getVariablesList() {
        return new ArrayList<>(variableAddresses.keySet());
    }

    public boolean containsVariable(String identifier) {
        return variableAddresses.containsKey(identifier);
    }

    public int getVariableAddress(String identifier) {
        return variableAddresses.get(identifier);
    }

    public int declareVariable(CompilationState compilationState, String identifier) {
        if (variableAddresses.containsKey(identifier)) {
            throw new CompilationException("Variable " + identifier + " is already defined in local scope!");
        } else {
            int address = compilationState.allocateVariable();
            variableAddresses.put(identifier, address);
            return address;
        }
    }

    public int numberOfVariables() {
        return this.variableAddresses.size();
    }
}

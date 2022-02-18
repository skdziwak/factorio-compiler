package com.skdziwak.factoriolang.tree.statements;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.constants.MathOperator;
import com.skdziwak.factoriolang.tree.Expression;
import com.skdziwak.factoriolang.tree.Statement;
import com.skdziwak.factoriolang.tree.expressions.BinaryOperation;
import com.skdziwak.factoriolang.tree.expressions.VariableExpression;

public class ArrayAssignmentStatement extends Statement {
    private final String identifier;
    private final Expression addressExpression;
    private final Expression valueExpression;

    public ArrayAssignmentStatement(String identifier, Expression addressExpression, Expression valueExpression) {
        this.identifier = identifier;
        this.addressExpression = addressExpression;
        this.valueExpression = valueExpression;
    }

    @Override
    public void compile(CompilationState state) {
        new DereferenceAssignmentStatement(
                new BinaryOperation(new VariableExpression(identifier), MathOperator.ADD, addressExpression),
                valueExpression).compile(state);
    }
}

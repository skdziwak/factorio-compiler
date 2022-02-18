package com.skdziwak.factoriolang.parser.visitors;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.constants.MathOperator;
import com.skdziwak.factoriolang.tree.Expression;
import com.skdziwak.factoriolang.tree.expressions.BinaryOperation;
import com.skdziwak.factoriolang.tree.expressions.VariableExpression;

public class DereferenceArrayExpression extends Expression {
    private final String identifier;
    private final Expression addressExpression;

    public DereferenceArrayExpression(String identifier, Expression addressExpression) {
        this.identifier = identifier;
        this.addressExpression = addressExpression;
    }

    @Override
    public void compile(CompilationState state) {
        new DereferenceExpression(
                new BinaryOperation(
                        new VariableExpression(identifier),
                        MathOperator.ADD,
                        addressExpression)).compile(state);
    }
}

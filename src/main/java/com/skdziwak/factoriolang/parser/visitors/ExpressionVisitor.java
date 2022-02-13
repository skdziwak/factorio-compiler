package com.skdziwak.factoriolang.parser.visitors;

import com.skdziwak.factoriolang.antlr4.LangBaseVisitor;
import com.skdziwak.factoriolang.antlr4.LangParser;
import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.parser.ParsingException;
import com.skdziwak.factoriolang.tree.Expression;
import com.skdziwak.factoriolang.tree.expressions.*;

import java.util.ArrayList;
import java.util.List;

public class ExpressionVisitor extends LangBaseVisitor<Expression> {
    @Override
    public Expression visitOrOperator(LangParser.OrOperatorContext ctx) {
        return visitBinaryOperator(ctx);
    }

    @Override
    public Expression visitAndOperator(LangParser.AndOperatorContext ctx) {
        return visitBinaryOperator(ctx);
    }

    @Override
    public Expression visitParenthesis(LangParser.ParenthesisContext ctx) {
        return ctx.getChild(1).accept(this);
    }

    @Override
    public Expression visitUnaryNot(LangParser.UnaryNotContext ctx) {
        return new BinaryOperation(new ConstantExpression(0), CompilationState.Operator.EQUALS, ctx.getChild(1).accept(this));
    }

    @Override
    public Expression visitVariable(LangParser.VariableContext ctx) {
        return new VariableExpression(ctx.getChild(0).getText());
    }

    @Override
    public Expression visitNumber(LangParser.NumberContext ctx) {
        return new ConstantExpression(Integer.valueOf(ctx.getChild(0).getText()));
    }

    @Override
    public Expression visitComparison(LangParser.ComparisonContext ctx) {
        return visitBinaryOperator(ctx);
    }

    @Override
    public Expression visitShiftOperation(LangParser.ShiftOperationContext ctx) {
        return visitBinaryOperator(ctx);
    }

    @Override
    public Expression visitUnaryMinus(LangParser.UnaryMinusContext ctx) {
        return new BinaryOperation(new ConstantExpression(0), CompilationState.Operator.SUBTRACT, ctx.getChild(1).accept(this));
    }

    @Override
    public Expression visitMultiplicationPrecedenceOperation(LangParser.MultiplicationPrecedenceOperationContext ctx) {
        return visitBinaryOperator(ctx);
    }

    @Override
    public Expression visitAdditionPrecedenceOperation(LangParser.AdditionPrecedenceOperationContext ctx) {
        return visitBinaryOperator(ctx);
    }

    @Override
    public Expression visitInput(LangParser.InputContext ctx) {
        return new InputExpression(Integer.valueOf(ctx.getChild(2).getText()));
    }

    @Override
    public Expression visitFunctionCall(LangParser.FunctionCallContext ctx) {
        String identifier = ctx.getChild(0).getText();
        List<Expression> arguments = new ArrayList<>();
        if (ctx.getChild(2) instanceof LangParser.ArgsContext argsContext) {
            for (int i = 0 ; i < argsContext.getChildCount() ; i += 2) {
                arguments.add(argsContext.getChild(i).accept(this));
            }
        }
        return new FunctionCall(identifier, arguments);
    }

    private Expression visitBinaryOperator(LangParser.ExprContext ctx) {
        Expression left = ctx.getChild(0).accept(this);
        String operatorString = ctx.getChild(1).getText();
        Expression right = ctx.getChild(2).accept(this);
        CompilationState.Operator operator = switch (operatorString) {
            case "+" -> CompilationState.Operator.ADD;
            case "-" -> CompilationState.Operator.SUBTRACT;
            case "*" -> CompilationState.Operator.MULTIPLY;
            case "/" -> CompilationState.Operator.DIVIDE;
            case "%" -> CompilationState.Operator.MODULO;
            case "<<" -> CompilationState.Operator.SHIFT_LEFT;
            case ">>" -> CompilationState.Operator.SHIFT_RIGHT;
            case "==" -> CompilationState.Operator.EQUALS;
            case "!=" -> CompilationState.Operator.NOT_EQUALS;
            case ">" -> CompilationState.Operator.GREATER;
            case "<" -> CompilationState.Operator.LOWER;
            case ">=" -> CompilationState.Operator.GREATER_EQUAL;
            case "<=" -> CompilationState.Operator.LOWER_EQUAL;
            default -> throw new ParsingException("Invalid operator");
        };
        return new BinaryOperation(left, operator, right);
    }
}

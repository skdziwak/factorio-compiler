package com.skdziwak.factoriolang.parser.visitors;

import com.skdziwak.factoriolang.antlr4.LangBaseVisitor;
import com.skdziwak.factoriolang.antlr4.LangParser;
import com.skdziwak.factoriolang.constants.MathOperator;
import com.skdziwak.factoriolang.tree.Expression;
import com.skdziwak.factoriolang.tree.Statement;
import com.skdziwak.factoriolang.tree.expressions.BinaryOperation;
import com.skdziwak.factoriolang.tree.expressions.ConstantExpression;
import com.skdziwak.factoriolang.tree.expressions.VariableExpression;
import com.skdziwak.factoriolang.tree.statements.*;

import java.util.ArrayList;
import java.util.List;

public class StatementVisitor extends LangBaseVisitor<Statement> {
    private final ExpressionVisitor expressionVisitor = new ExpressionVisitor();
    @Override
    public Statement visitAssignmentStatement(LangParser.AssignmentStatementContext ctx) {
        return new AssignmentStatement(ctx.getChild(0).getText(), ctx.getChild(2).accept(expressionVisitor));
    }

    @Override
    public Statement visitExpressionStatement(LangParser.ExpressionStatementContext ctx) {
        return new ExpressionStatement(ctx.getChild(0).accept(expressionVisitor));
    }

    @Override
    public Statement visitMultipleStatements(LangParser.MultipleStatementsContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (int i = 1 ; i < ctx.getChildCount() - 1 ; i++) {
            statements.add(ctx.getChild(i).accept(this));
        }
        return new MultipleStatements(statements);
    }

    @Override
    public Statement visitIfElseStatement(LangParser.IfElseStatementContext ctx) {
        return new IfElseStatement(ctx.getChild(2).accept(expressionVisitor),
                ctx.getChild(4).accept(this), ctx.getChild(6).accept(this));
    }

    @Override
    public Statement visitIfStatement(LangParser.IfStatementContext ctx) {
        return new IfStatement(ctx.getChild(2).accept(expressionVisitor), ctx.getChild(4).accept(this));
    }

    @Override
    public Statement visitWhileStatement(LangParser.WhileStatementContext ctx) {
        return new WhileStatement(ctx.getChild(2).accept(expressionVisitor), ctx.getChild(4).accept(this));
    }

    @Override
    public Statement visitVarDeclare(LangParser.VarDeclareContext ctx) {
        return new DeclarationStatement(ctx.getChild(1).getText());
    }

    @Override
    public Statement visitVarDeclareDefine(LangParser.VarDeclareDefineContext ctx) {
        return new DeclarationStatement(ctx.getChild(1).getText(), ctx.getChild(3).accept(expressionVisitor));
    }

    @Override
    public Statement visitOutputStatement(LangParser.OutputStatementContext ctx) {
        return new OutputStatement(Integer.valueOf(ctx.getChild(2).getText()), ctx.getChild(5).accept(expressionVisitor));
    }

    @Override
    public Statement visitPointerAssignmentStatement(LangParser.PointerAssignmentStatementContext ctx) {
        return new DereferenceAssignmentStatement(
                ctx.getChild(1).accept(expressionVisitor),
                ctx.getChild(3).accept(expressionVisitor));
    }

    @Override
    public Statement visitArrayAssignmentStatement(LangParser.ArrayAssignmentStatementContext ctx) {
        return new ArrayAssignmentStatement(
                ctx.getChild(0).getText(),
                ctx.getChild(2).accept(expressionVisitor),
                ctx.getChild(5).accept(expressionVisitor));
    }

    @Override
    public Statement visitIncrement(LangParser.IncrementContext ctx) {
        String identifier = ctx.getChild(0).getText();
        return new AssignmentStatement(identifier, new BinaryOperation(new VariableExpression(identifier), MathOperator.ADD, new ConstantExpression(1)));
    }

    @Override
    public Statement visitDecrement(LangParser.DecrementContext ctx) {
        String identifier = ctx.getChild(0).getText();
        return new AssignmentStatement(identifier, new BinaryOperation(new VariableExpression(identifier), MathOperator.SUBTRACT, new ConstantExpression(1)));
    }

    @Override
    public Statement visitIncrementN(LangParser.IncrementNContext ctx) {
        String identifier = ctx.getChild(0).getText();
        Expression n = ctx.getChild(2).accept(expressionVisitor);
        return new AssignmentStatement(identifier, new BinaryOperation(new VariableExpression(identifier), MathOperator.ADD, n));
    }

    @Override
    public Statement visitDecrementN(LangParser.DecrementNContext ctx) {
        String identifier = ctx.getChild(0).getText();
        Expression n = ctx.getChild(2).accept(expressionVisitor);
        return new AssignmentStatement(identifier, new BinaryOperation(new VariableExpression(identifier), MathOperator.SUBTRACT, n));
    }

    @Override
    public Statement visitIncrementArr(LangParser.IncrementArrContext ctx) {
        String identifier = ctx.getChild(0).getText();
        return new DereferenceAssignmentStatement(
                new BinaryOperation(
                        new VariableExpression(identifier), MathOperator.ADD, ctx.getChild(2).accept(expressionVisitor)),
                new BinaryOperation(
                        new DereferenceExpression(new BinaryOperation(
                                new VariableExpression(identifier), MathOperator.ADD, ctx.getChild(2).accept(expressionVisitor))),
                        MathOperator.ADD,
                        new ConstantExpression(1)
                )
        );
    }

    @Override
    public Statement visitDecrementArr(LangParser.DecrementArrContext ctx) {
        String identifier = ctx.getChild(0).getText();
        return new DereferenceAssignmentStatement(
                new BinaryOperation(
                        new VariableExpression(identifier), MathOperator.ADD, ctx.getChild(2).accept(expressionVisitor)),
                new BinaryOperation(
                        new DereferenceExpression(new BinaryOperation(
                                new VariableExpression(identifier), MathOperator.ADD, ctx.getChild(2).accept(expressionVisitor))),
                        MathOperator.SUBTRACT,
                        new ConstantExpression(1)
                )
        );
    }

    @Override
    public Statement visitIncrementArrN(LangParser.IncrementArrNContext ctx) {
        String identifier = ctx.getChild(0).getText();
        return new DereferenceAssignmentStatement(
                new BinaryOperation(
                        new VariableExpression(identifier), MathOperator.ADD, ctx.getChild(2).accept(expressionVisitor)),
                new BinaryOperation(
                        new DereferenceExpression(new BinaryOperation(
                                new VariableExpression(identifier), MathOperator.ADD, ctx.getChild(2).accept(expressionVisitor))),
                        MathOperator.ADD,
                        ctx.getChild(5).accept(expressionVisitor)
                )
        );
    }

    @Override
    public Statement visitDecrementArrN(LangParser.DecrementArrNContext ctx) {
        String identifier = ctx.getChild(0).getText();
        return new DereferenceAssignmentStatement(
                new BinaryOperation(
                        new VariableExpression(identifier), MathOperator.ADD, ctx.getChild(2).accept(expressionVisitor)),
                new BinaryOperation(
                        new DereferenceExpression(new BinaryOperation(
                                new VariableExpression(identifier), MathOperator.ADD, ctx.getChild(2).accept(expressionVisitor))),
                        MathOperator.SUBTRACT,
                        ctx.getChild(5).accept(expressionVisitor)
                )
        );
    }
}

package com.skdziwak.factoriolang.parser.visitors;

import com.skdziwak.factoriolang.antlr4.LangBaseVisitor;
import com.skdziwak.factoriolang.antlr4.LangParser;
import com.skdziwak.factoriolang.tree.Expression;
import com.skdziwak.factoriolang.tree.Statement;
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
}

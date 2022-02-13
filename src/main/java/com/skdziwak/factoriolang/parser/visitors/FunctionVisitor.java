package com.skdziwak.factoriolang.parser.visitors;

import com.skdziwak.factoriolang.antlr4.LangBaseVisitor;
import com.skdziwak.factoriolang.antlr4.LangParser;
import com.skdziwak.factoriolang.tree.Statement;
import com.skdziwak.factoriolang.tree.functions.Function;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class FunctionVisitor extends LangBaseVisitor<Function> {
    private final StatementVisitor statementVisitor = new StatementVisitor();
    @Override
    public Function visitFunc(LangParser.FuncContext ctx) {
        String identifier = ctx.getChild(1).getText();
        String returns = null;
        List<String> arguments = new ArrayList<>();
        List<Statement> statements = new ArrayList<>();
        if (ctx.getChild(3) instanceof LangParser.ArgsdeclContext argsDeclContext) {
            if (!argsDeclContext.isEmpty()) {
                for (int i = 0 ; i < argsDeclContext.getChildCount() ; i += 2) {
                    arguments.add(argsDeclContext.getChild(i).getText());
                }
            }
        }
        if (ctx.getChild(6) instanceof LangParser.StatementsContext statementsContext) {
            for (int i = 0 ; i < statementsContext.getChildCount() ; i++) {
                statements.add(statementVisitor.visit(statementsContext.getChild(i)));
            }
        }
        if (ctx.getChild(9) instanceof TerminalNode terminalNode) {
            returns = terminalNode.getText();
        }
        return new Function(identifier, arguments, statements, returns);
    }
}

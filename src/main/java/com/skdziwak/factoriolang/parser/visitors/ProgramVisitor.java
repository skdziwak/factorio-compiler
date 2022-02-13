package com.skdziwak.factoriolang.parser.visitors;

import com.skdziwak.factoriolang.antlr4.LangBaseVisitor;
import com.skdziwak.factoriolang.antlr4.LangParser;
import com.skdziwak.factoriolang.tree.Program;
import org.antlr.v4.runtime.tree.ParseTree;

public class ProgramVisitor extends LangBaseVisitor<Program> {
    private final StatementVisitor statementVisitor = new StatementVisitor();
    private final FunctionVisitor functionVisitor = new FunctionVisitor();
    @Override
    public Program visitProg(LangParser.ProgContext ctx) {
        Program program = new Program();
        for (ParseTree parseTree : ctx.children) {
            if (parseTree instanceof LangParser.StmtContext) {
                program.addStatement(statementVisitor.visit(parseTree));
            } else if (parseTree instanceof LangParser.FuncContext) {
                program.addFunction(functionVisitor.visit(parseTree));
            }
        }
        return program;
    }
}

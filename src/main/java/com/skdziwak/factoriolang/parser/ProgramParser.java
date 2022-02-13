package com.skdziwak.factoriolang.parser;

import com.skdziwak.factoriolang.antlr4.LangLexer;
import com.skdziwak.factoriolang.antlr4.LangParser;
import com.skdziwak.factoriolang.parser.visitors.ProgramVisitor;
import com.skdziwak.factoriolang.tree.Program;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class ProgramParser {
    public static Program parseProgram(String input) throws ParsingException {
        LangLexer lexer = new LangLexer(CharStreams.fromString(input));
        TokenStream tokenStream = new CommonTokenStream(lexer);
        LangParser langParser = new LangParser(tokenStream);

        LangErrorListener errorListener = new LangErrorListener();
        langParser.removeErrorListeners();
        langParser.addErrorListener(errorListener);

        ParseTree programParseTree = langParser.prog();

        errorListener.throwIfCollectedAny();

        ProgramVisitor programVisitor = new ProgramVisitor();

        return programVisitor.visit(programParseTree);
    }
}

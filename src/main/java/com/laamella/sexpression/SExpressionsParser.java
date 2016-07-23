package com.laamella.sexpression;

import java.io.IOException;
import java.io.Reader;
import java.util.Optional;

import static com.laamella.sexpression.ParseState.*;

enum ParseState {inAtom, inWhitespace, inQuotedAtom}

public class SExpressionsParser {

    public SExpressionNode parseExpressions(Reader reader) throws IOException {
        AtomList list = new AtomList();
        int c;
        while ((c = reader.read()) != -1) {
            if (c == '(') {
                list.add(parseList(reader));
            }
        }
        return list;
    }

    public Optional<SExpressionNode> parseExpression(Reader reader) throws IOException {
        int c;
        while ((c = reader.read()) != -1) {
            if (c == '(') {
                return Optional.of(parseList(reader));
            }
        }
        return Optional.empty();
    }

    private SExpressionNode parseList(Reader reader) throws IOException {
        AtomList root = new AtomList();
        ParseState state = inWhitespace;
        StringBuilder atom = new StringBuilder();
        int c;
        while ((c = reader.read()) != -1) {
            switch (state) {
                case inWhitespace:
                    if (!Character.isWhitespace(c)) {
                        if (c == '(') {
                            root.add(parseList(reader));
                        } else if (c == ')') {
                            return root;
                        } else if (c == '"') {
                            atom.setLength(0);
                            state = inQuotedAtom;
                        } else {
                            atom.setLength(0);
                            atom.appendCodePoint(c);
                            state = inAtom;
                        }
                    }
                    break;
                case inAtom:
                    if (Character.isWhitespace(c)) {
                        root.add(atom);
                        state = inWhitespace;
                    } else if (c == ')') {
                        root.add(atom);
                        return root;
                    } else {
                        atom.appendCodePoint(c);
                    }
                    break;
                case inQuotedAtom:
                    if (c == '"') {
                        root.add(atom);
                        state = inWhitespace;
                    } else {
                        atom.appendCodePoint(c);
                    }
                    break;
            }
        }
        throw new IllegalStateException();
    }

}

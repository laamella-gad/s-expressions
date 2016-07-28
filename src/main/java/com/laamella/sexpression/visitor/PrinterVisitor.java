package com.laamella.sexpression.visitor;

import com.laamella.sexpression.model.*;

public class PrinterVisitor implements Visitor<Appendable, Void> {
    public static final PrinterVisitor TO_STRING = new PrinterVisitor();

    @Override
    public Void accept(Atom atom, Appendable output) throws Exception {
        output.append(atom.encoded());
        return null;
    }

    @Override
    public Void accept(AtomList atomList, Appendable output) throws Exception {
        output.append('(');
        String space = "";
        for (Node n : atomList.nodes()) {
            output.append(space);
            n.visit(this, output);
            space = " ";
        }
        output.append(')');
        return null;
    }

    @Override
    public Void accept(Comment comment, Appendable output) throws Exception {
        return null;
    }

    @Override
    public Void accept(Whitespace whitespace, Appendable arg) throws Exception {
        return null;
    }

    @Override
    public Void accept(LineTerminator lineTerminator, Appendable arg) throws Exception {
        return null;
    }
}

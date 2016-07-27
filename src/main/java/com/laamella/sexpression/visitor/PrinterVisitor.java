package com.laamella.sexpression.visitor;

import com.laamella.sexpression.model.Atom;
import com.laamella.sexpression.model.AtomList;
import com.laamella.sexpression.model.Comment;
import com.laamella.sexpression.model.SExpression;

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
        for (SExpression n : atomList.values) {
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
}

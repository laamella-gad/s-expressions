package com.laamella.sexpression.visitor;

import com.laamella.sexpression.Atom;
import com.laamella.sexpression.AtomList;
import com.laamella.sexpression.SExpressionNode;

public class PrinterVisitor implements Visitor<Appendable, Void> {
    public static final PrinterVisitor TO_STRING = new PrinterVisitor();

    @Override
    public Void accept(Atom atom, Appendable output) throws Exception {
        for (char c : atom.value.toCharArray()) {
            if (Character.isWhitespace(c)) {
                output.append('"').append(atom.value).append('"');
                return null;
            }
        }
        output.append(atom.value);
        return null;
    }

    @Override
    public Void accept(AtomList atomList, Appendable output) throws Exception {
        output.append('(');
        String space = "";
        for (SExpressionNode n : atomList.values) {
            output.append(space);
            n.visit(this, output);
            space = " ";
        }
        output.append(')');
        return null;
    }
}

package com.laamella.sexpression.visitor;

import com.laamella.sexpression.model.Atom;
import com.laamella.sexpression.model.AtomList;
import com.laamella.sexpression.model.Document;
import com.laamella.sexpression.model.SExpression;
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;

import java.io.IOException;

import static com.laamella.sexpression.visitor.Visitor.EnterDecision.ENTER;

public class PrettyPrinterVisitor implements Visitor<Appendable, Void> {
    public String indentWhitespace = "\t";
    private int indent = 0;

    @Override
    public Void accept(Atom atom, Appendable output) throws Exception {
        appendComments(atom.comments, output);
        output.append(atom.encoded());
        return null;
    }

    private void appendComments(Vector<String> comments, Appendable output) throws IOException {
        for (String comment : comments) {
            output.append("\n");
            indent(output);
            output.append(";").append(comment);
        }
    }

    @Override
    public Void accept(AtomList atomList, Appendable output) throws Exception {
        print(atomList.asVector(), output);
        return null;
    }

    @Override
    public EnterDecision enter(AtomList atomList, Appendable output) throws Exception {
        appendComments(atomList.comments, output);
        output.append("\n");
        indent(output);
        output.append('(');
        increaseIndent();
        return ENTER;
    }

    private void indent(Appendable output) throws IOException {
        for (int i = 0; i < indent; i++) {
            output.append(indentWhitespace);
        }
    }

    private void increaseIndent() {
        indent++;
    }

    @Override
    public void exit(AtomList atomList, Void result, Appendable output) throws Exception {
        output.append(')');
        decreaseIndent();
    }

    private void decreaseIndent() {
        indent--;
    }

    @Override
    public EnterDecision enter(Document document, Appendable output) throws Exception {
        return ENTER;
    }

    @Override
    public void exit(Document document, Void result, Appendable output) throws Exception {
        appendComments(document.comments, output);
    }

    public void print(Seq<? extends SExpression> list, Appendable output) throws Exception {
        String space = "";
        for (SExpression n : list) {
            output.append(space);
            n.visit(this, output);
            space = " ";
        }
    }

    @Override
    public Void accept(Document document, Appendable output) throws Exception {
        print(document.asVector(), output);
        return null;
    }
}

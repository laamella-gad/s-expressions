package com.laamella.sexpression.visitor;

import javaslang.collection.Seq;

import static com.laamella.sexpression.visitor.Visitor.EnterDecision.ENTER;

public class PrinterVisitor implements Visitor<Appendable, Void> {
	public static final PrinterVisitor TO_STRING = new PrinterVisitor();

	@Override
	public Void accept(Atom atom, Appendable output) throws Exception {
		output.append(atom.encoded());
		return null;
	}

	@Override
	public Void accept(AtomList atomList, Appendable output) throws Exception {
		print(atomList.list(), output);
		return null;
	}

	@Override
	public EnterDecision enter(AtomList atomList, Appendable output) throws Exception {
		output.append('(');
		return ENTER;
	}

	@Override
	public void exit(AtomList atomList, Void result, Appendable output) throws Exception {
		output.append(')');
	}

	@Override
	public EnterDecision enter(Document document, Appendable output) throws Exception {
		return ENTER;
	}

	@Override
	public void exit(Document document, Void result, Appendable output) throws Exception {

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
		print(document.list(), output);
		return null;
	}
}

package com.laamella.sexpression.visitor;

import com.laamella.sexpression.model.*;
import javaslang.collection.Seq;

import static com.laamella.sexpression.visitor.Visitor.EnterDecision.ENTER;

/**
 * Prints only lists and atoms.
 */
public class StructuralPrinterVisitor implements Visitor<Appendable, Void> {
	@Override
	public Void accept(Atom atom, Appendable output) throws Exception {
		output.append(atom.encoded());
		return null;
	}

	@Override
	public Void accept(AtomList atomList, Appendable output) throws Exception {
		print(atomList.asVector(), output);
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

	public void print(Seq<? extends Node> list, Appendable output) throws Exception {
		String space = "";
		for (Node n : list) {
			if (n.isSExpression()) {
				output.append(space);
			}
			n.visit(this, output);
			if (n.isSExpression()) {
				space = " ";
			}
		}
	}

	@Override
	public Void accept(Document document, Appendable output) throws Exception {
		print(document.asVector(), output);
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

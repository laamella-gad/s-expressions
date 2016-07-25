package com.laamella.sexpression;

import com.laamella.sexpression.visitor.PrinterVisitor;
import com.laamella.sexpression.visitor.Visitor;

public class Atom implements SExpressionNode {
	public final String value;

	public Atom(CharSequence value) {
		this.value = value.toString();
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		try {
			PrinterVisitor.TO_STRING.accept(this, output);
		} catch (Exception e) {
			return e.getMessage();
		}
		return output.toString();
	}

	@Override
	public <A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception {
		return visitor.accept(this, arg);
	}
}

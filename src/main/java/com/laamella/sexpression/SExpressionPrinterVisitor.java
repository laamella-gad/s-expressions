package com.laamella.sexpression;

public class SExpressionPrinterVisitor implements Visitor<Appendable, Void> {
	public static final SExpressionPrinterVisitor TO_STRING = new SExpressionPrinterVisitor();

	@Override
	public Void accept(Atom atom, Appendable output) throws Exception {
		output.append('"').append(atom.value).append('"');
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

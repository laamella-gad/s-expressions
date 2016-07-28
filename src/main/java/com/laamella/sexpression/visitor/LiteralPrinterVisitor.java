package com.laamella.sexpression.visitor;

import com.laamella.sexpression.model.*;
import javaslang.collection.Seq;

/**
 * Prints everything including whitespace and comments.
 */
public class LiteralPrinterVisitor implements Visitor<Appendable, Void> {
	@Override
	public Void accept(Atom atom, Appendable output) throws Exception {
		output.append(atom.encoded());
		return null;
	}

	@Override
	public Void accept(AtomList atomList, Appendable output) throws Exception {
		output.append('(');
		visit(atomList.nodes(), output);
		output.append(')');
		return null;
	}

	public void visit(Seq<Node> list, Appendable output) throws Exception {
		for (Node n : list) {
			n.visit(this, output);
		}
	}

	@Override
	public Void accept(Document document, Appendable output) throws Exception {
		visit(document.nodes(), output);
		return null;
	}

	@Override
	public Void accept(Comment comment, Appendable output) throws Exception {
		output.append(comment.text);
		return null;
	}

	@Override
	public Void accept(Whitespace whitespace, Appendable output) throws Exception {
		output.append(whitespace.whitespace);
		return null;
	}

	@Override
	public Void accept(LineTerminator lineTerminator, Appendable output) throws Exception {
		output.append("\n");
		return null;
	}
}

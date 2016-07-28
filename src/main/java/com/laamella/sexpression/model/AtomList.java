package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.PrinterVisitor;
import com.laamella.sexpression.visitor.Visitor;
import javaslang.collection.Vector;

import java.util.function.Consumer;

import static javaslang.collection.Vector.*;

public class AtomList extends SExpression {
	private Vector<Node> nodes = empty();
	private Vector<SExpression> list = empty();

	public void add(Node node) {
		setNodes(nodes.append(node));
	}

	public void setNodes(Vector<Node> nodes) {
		this.nodes = nodes;
		list = nodes.filter(Node::isSExpression).map(Node::asSExpression);
	}

	public void add(CharSequence atom) {
		setNodes(nodes.append(new Atom(atom)));
	}

	/**
	 * @return all the nodes here, including comments, whitespace, etc.
	 */
	public Vector<Node> nodes() {
		return nodes;
	}

	/**
	 * @return the atoms and lists inside this list.
	 */
	public Vector<SExpression> list() {
		return list;
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

	@Override
	public Otherwise whenList(Consumer<AtomList> action) {
		action.accept(this);
		return new Otherwise(false);
	}

	@Override
	public Otherwise whenAtom(Consumer<Atom> action) {
		return new Otherwise(true);
	}

	@Override
	public boolean isAtom() {
		return false;
	}

	@Override
	public boolean isList() {
		return true;
	}

	@Override
	public Atom asAtom() {
		throw new IllegalStateException();
	}

	@Override
	public AtomList asList() {
		return this;
	}

	public boolean isAllAtoms() {
		for (Node e : nodes) {
			if (e.isList()) {
				return false;
			}
		}
		return true;
	}

	public boolean isEmpty() {
		return nodes.isEmpty();
	}


}

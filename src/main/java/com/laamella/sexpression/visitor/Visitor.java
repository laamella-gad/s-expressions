package com.laamella.sexpression.visitor;

import com.laamella.sexpression.model.*;

public interface Visitor<A, R> {
	R accept(Atom atom, A arg) throws Exception;

	R accept(AtomList atomList, A arg) throws Exception;

	R accept(Document document, A arg) throws Exception;

	R accept(Comment comment, A arg) throws Exception;

	R accept(Whitespace whitespace, A arg) throws Exception;

	R accept(LineTerminator lineTerminator, A arg) throws Exception;

	class Adapter<A, R> implements Visitor<A, R> {
		@Override
		public R accept(Atom atom, A arg) throws Exception {
			return null;
		}

		@Override
		public R accept(AtomList atomList, A arg) throws Exception {
			return null;
		}

		@Override
		public R accept(Document document, A arg) throws Exception {
			return null;
		}

		@Override
		public R accept(Comment comment, A arg) throws Exception {
			return null;
		}

		@Override
		public R accept(Whitespace whitespace, A arg) throws Exception {
			return null;
		}

		@Override
		public R accept(LineTerminator lineTerminator, A arg) throws Exception {
			return null;
		}
	}
}

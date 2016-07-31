package com.laamella.sexpression.model;

import com.laamella.sexpression.CharSource;
import com.laamella.sexpression.codec.AtomCodec;
import com.laamella.sexpression.codec.CombinedCodec;
import com.laamella.sexpression.visitor.Visitor;

import java.util.function.Consumer;

import static com.laamella.sexpression.codec.AtomCodec.*;

public class Atom extends SExpression {
	private final byte[] data;
	private final AtomCodec codec;

    private static final AtomCodec DEFAULT_STRING_CODEC = new CombinedCodec(SIMPLE, DOUBLE_QUOTE, BASE64);

	public Atom(CharSequence value) {
		this.codec = DEFAULT_STRING_CODEC;
		this.data = codec.decode(value).get();
	}

	public Atom(CharSequence value, AtomCodec codec) {
		this.codec = codec;
		this.data = codec.decode(value).get();
	}

	public Atom(byte[] data, AtomCodec codec) {
		this.codec = codec;
		this.data = data;
	}

	/**
	 * @return the atom as it would appear in an s-expression.
	 */
	public String encoded() {
		return codec.encode(data).get();
	}

	/**
	 * @return the raw bytes represented by this atom.
	 */
	public byte[] raw() {
		return data;
	}

	/**
	 * @return the actual text represented by this atom.
	 * If the atom contains binary data this is undefined, use raw() instead.
	 */
	public String value() {
		return new String(data, CharSource.UTF8);
	}

	@Override
	public <A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception {
		return visitor.accept(this, arg);
	}

	@Override
	public boolean isAtom() {
		return true;
	}
	
	@Override
	public Atom asAtom() {
		return this;
	}
}

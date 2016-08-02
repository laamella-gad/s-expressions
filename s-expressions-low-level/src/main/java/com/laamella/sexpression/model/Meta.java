package com.laamella.sexpression.model;

public abstract class Meta extends Node {
	public Meta(Node parent) {
		super(parent);
	}

	@Override
	public Meta asMeta() {
		return this;
	}

	@Override
	public boolean isMeta() {
		return true;
	}
}

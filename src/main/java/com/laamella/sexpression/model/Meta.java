package com.laamella.sexpression.model;

public abstract class Meta extends Node {
	@Override
	public Meta asMeta() {
		return this;
	}

	@Override
	public boolean isMeta() {
		return true;
	}
}

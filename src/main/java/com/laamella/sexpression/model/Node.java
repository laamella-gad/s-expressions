package com.laamella.sexpression.model;

public interface Node {
	/**
	 * Get a node by index. Useful if it is known to exist
	 *
	 * @param i the index, starts with 0
	 * @return the node found
	 * @throws IndexOutOfBoundsException when i less than 0 or i larger than getSize -1
	 */
	<T extends Node> T node(int i);

	<T extends Node> T node(String name);
}

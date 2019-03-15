package com.laamella.sexpression.codec;

import java.util.Optional;

import static com.laamella.sexpression.CharSink.UTF8;
import static com.laamella.sexpression.utils.Utils.hasSpecialCharacters;
import static com.laamella.sexpression.utils.Utils.hasWhitespace;

/**
 * Simply encodes/decodes UTF-8
 */
public class SimpleCodec implements AtomCodec {
	@Override
	public Optional<String> encode(byte[] data) {
		String atom = new String(data, UTF8);
		if (hasWhitespace(atom) || hasSpecialCharacters(atom)) {
			return Optional.empty();
		}
		return Optional.of(atom);
	}

	@Override
	public Optional<byte[]> decode(CharSequence atom) {
		return Optional.of(atom.toString().getBytes(UTF8));
	}
}

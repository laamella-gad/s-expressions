package com.laamella.sexpression.codec;

import java.util.Optional;

import static com.laamella.sexpression.CharSource.UTF8;
import static com.laamella.sexpression.utils.Utils.hasSpecialCharacters;

/**
 * The double quote codec double quotes the value.
 * Data is assumed to be UTF-8 encoded.
 */
public class DoubleQuoteCodec implements AtomCodec {
	@Override
	public Optional<String> encode(byte[] data) {
		String in = new String(data, UTF8);
		if (hasSpecialCharacters(in)) {
			return Optional.empty();
		}
		return Optional.of("\"" + in + "\"");
	}

	@Override
	public Optional<byte[]> decode(CharSequence atom) {
		String val = atom.toString();
		if (val.charAt(0) == '"' && val.charAt(val.length() - 1) == '"') {
			return Optional.of(val.substring(1, val.length() - 1).getBytes(UTF8));
		}
		return Optional.empty();
	}
}

package com.laamella.sexpression.codec;

import java.util.Base64;
import java.util.Optional;

public class Base64Codec implements AtomCodec {
	private final Base64.Decoder decoder = Base64.getDecoder();
	private final Base64.Encoder encoder = Base64.getEncoder();

	@Override
	public Optional<String> encode(byte[] data) {
		return Optional.of("|" + new String(encoder.encode(data)) + "|");
	}

	@Override
	public Optional<byte[]> decode(CharSequence atom) {
		String val = atom.toString();
		if (val.charAt(0) == '|' && val.charAt(val.length() - 1) == '|') {
			try {
				return Optional.of(decoder.decode(val.substring(1, val.length() - 1)));
			} catch (IllegalArgumentException e) {
				return Optional.empty();
			}
		}
		return Optional.empty();
	}

}

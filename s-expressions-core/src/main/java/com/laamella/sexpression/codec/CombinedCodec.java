package com.laamella.sexpression.codec;

import java.util.Optional;

public class CombinedCodec implements AtomCodec {
	private final AtomCodec[] codecs;

	/**
	 * @param codecs codecs to try. For encoding, from beginning to end. For decoding, from end to beginning.
	 */
	public CombinedCodec(AtomCodec... codecs) {
		this.codecs = codecs;
	}

	@Override
	public Optional<String> encode(byte[] data) {
		for (AtomCodec codec : codecs) {
			Optional<String> encode = codec.encode(data);
			if (encode.isPresent()) {
				return encode;
			}
		}
		return Optional.empty();
	}

	@Override
	public Optional<byte[]> decode(CharSequence atom) {
		for (int i = codecs.length - 1; i >= 0; i--) {
			AtomCodec codec = codecs[i];
			Optional<byte[]> encode = codec.decode(atom);
			if (encode.isPresent()) {
				return encode;
			}
		}
		return Optional.empty();
	}
}

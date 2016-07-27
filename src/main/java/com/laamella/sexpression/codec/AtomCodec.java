package com.laamella.sexpression.codec;

import org.omg.IOP.Codec;

import java.util.Optional;

/**
 * Ways to display an atom.
 * An atom is seen as a sequence of bytes that can be output as text.
 * Often the bytes simply represent text, but they can house any binary data.
 */
public interface AtomCodec {
	AtomCodec BASE64 = new Base64Codec();
	AtomCodec DOUBLE_QUOTE = new DoubleQuoteCodec();
	AtomCodec SIMPLE = new SimpleCodec();

	/**
	 * @return the encoded String, or empty if it could not encode.
	 */
	Optional<String> encode(byte[] data);

	/**
	 * @return the decoded data, or empty if this decoder does not apply.
	 */
	Optional<byte[]> decode(CharSequence atom);
}

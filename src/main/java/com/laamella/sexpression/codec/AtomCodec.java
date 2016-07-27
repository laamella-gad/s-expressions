package com.laamella.sexpression.codec;

import static com.laamella.sexpression.CharSource.UTF8;

/**
 * Ways to display an atom.
 * An atom is seen as a sequence of bytes that can be output as text.
 * Often the bytes simply represent text, but they can house any binary data.
 */
public interface AtomCodec {
	/**
	 * The double quote codec double quotes the value if whitespace is detected.
	 * Data is assumed to be UTF-8 encoded.
	 */
	AtomCodec DOUBLE_QUOTE = new AtomCodec() {
		@Override
		public String encode(byte[] data) {
			String in = new String(data, UTF8);
			for (char c : in.toCharArray()) {
				if (Character.isWhitespace(c)) {
					return "\"" + in + "\"";
				}
			}
			return in;
		}

		@Override
		public byte[] decode(CharSequence atom) {
			String val = atom.toString();
			if (val.charAt(0) == '"' && val.charAt(val.length() - 1) == '"') {
				val = val.substring(1, val.length() - 2);
			}
			return val.getBytes(UTF8);
		}
	};

	String encode(byte[] data);

	byte[] decode(CharSequence atom);
}

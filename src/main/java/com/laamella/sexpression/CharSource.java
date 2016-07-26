package com.laamella.sexpression;

import java.io.*;
import java.nio.charset.Charset;

public class CharSource {
	public static Charset UTF8 = Charset.forName("utf-8");

	public static <T extends CharSink & Closeable> void push(Reader reader, T charSink) throws IOException {
		while (true) {
			int c = reader.read();
			if (c == -1) {
				charSink.close();
				return;
			}
			charSink.accept((char) c);
		}
	}

	public static <T extends CharSink & Closeable> void pushResource(String file, Charset encoding, T charSink) throws IOException {
		try (
				InputStream resourceAsStream = CharSource.class.getResourceAsStream(file);
				InputStreamReader reader = new InputStreamReader(resourceAsStream, encoding);
		) {
			push(reader, charSink);
		}
	}

	public static <T extends CharSink & Closeable> void pushString(String string, T charSink) throws IOException {
		push(new StringReader(string), charSink);
	}
}

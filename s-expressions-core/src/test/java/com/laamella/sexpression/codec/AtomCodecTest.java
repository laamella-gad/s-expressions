package com.laamella.sexpression.codec;

import com.laamella.sexpression.CharSource;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class AtomCodecTest {
	@Test
	public void base64Encode() {
		Optional<String> encode = AtomCodec.BASE64.encode(new byte[]{0, 1, 2, 3, 4, 5, 6});
		assertEquals("|AAECAwQFBg==|", encode.get());
	}

	@Test
	public void base64Decode() {
		Optional<byte[]> encode = AtomCodec.BASE64.decode("|AAECAwQFBg==|");
		assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6}, encode.get());
	}

	@Test
	public void base64DecodeSomethingNonMatching() {
		Optional<byte[]> encode = AtomCodec.BASE64.decode("bla");
		assertEquals(false, encode.isPresent());
	}

	@Test
	public void base64DecodeSomethingInvalid() {
		Optional<byte[]> encode = AtomCodec.BASE64.decode("|{}|");
		assertEquals(false, encode.isPresent());
	}

	@Test
	public void doubleQuoteEncode() {
		Optional<String> encode = AtomCodec.DOUBLE_QUOTE.encode(new byte[]{97, 98, 99, 100});
		assertEquals("\"abcd\"", encode.get());
	}

	@Test
	public void doubleQuoteDecode() {
		Optional<byte[]> encode = AtomCodec.DOUBLE_QUOTE.decode("\"abcd\"");
		assertArrayEquals(new byte[]{97, 98, 99, 100}, encode.get());
	}

	@Test
	public void doubleQuoteDecodeSomethingNonMatching() {
		Optional<byte[]> encode = AtomCodec.DOUBLE_QUOTE.decode("bla");
		assertEquals(false, encode.isPresent());
	}
}
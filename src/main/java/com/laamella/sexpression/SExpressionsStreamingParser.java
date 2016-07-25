package com.laamella.sexpression;

import java.io.Closeable;
import java.io.IOException;

public class SExpressionsStreamingParser implements CharSink, Closeable {
	private final SExpressionsStreamingLexer.Callback lexerCallback = new SExpressionsStreamingLexer.Callback() {
		private boolean inQuotes;
		private StringBuilder quoted;
		private boolean inComment;
		private boolean nothingButWhitespaceOnThisNewLine;

		@Override
		public void onText(String text, long start, long end) {
			if (inComment) {
				return;
			}
			nothingButWhitespaceOnThisNewLine = false;
			if (inQuotes) {
				quoted.append(text);
			} else {
				callback.onAtom(text);
			}
		}

		@Override
		public void onWhitespace(String whitespace, long start, long end) {
			if (inComment) {
				if (whitespace.contains("\n")) {
					nothingButWhitespaceOnThisNewLine = true;
					inComment = false;
				} else {
					return;
				}
			}
			if (inQuotes) {
				quoted.append(whitespace);
			} else if (whitespace.contains("\n")) {
				nothingButWhitespaceOnThisNewLine = true;
				inComment = false;
			}
		}

		@Override
		public void onOpeningBrace(char b, long pos) {
			if (inComment) {
				return;
			}
			nothingButWhitespaceOnThisNewLine = false;
			if (inQuotes) {
				quoted.appendCodePoint(b);
			} else {
				callback.onListBegin();
			}
		}

		@Override
		public void onClosingBrace(char b, long pos) {
			if (inComment) {
				return;
			}
			nothingButWhitespaceOnThisNewLine = false;
			if (inQuotes) {
				quoted.appendCodePoint(b);
			} else {
				callback.onListEnd();
			}
		}

		@Override
		public void onQuote(char q, long pos) {
			if (inComment) {
				return;
			}
			nothingButWhitespaceOnThisNewLine = false;
			if (inQuotes) {
				callback.onAtom(quoted.toString());
				quoted = new StringBuilder();
				inQuotes = false;
			} else {
				inQuotes = true;
			}

		}

		@Override
		public void onCloseStream() {
			if (inQuotes) {
				callback.onError(Error.STREAM_ENDED_WHILE_IN_QUOTES);
			}
			callback.onCloseStream();
		}

		@Override
		public void onOpenStream() {
			inQuotes = false;
			quoted = new StringBuilder();
			inComment = false;
			nothingButWhitespaceOnThisNewLine = true;
			callback.onOpenStream();
		}

		@Override
		public void onComment(char c, long pos) {
			if (inComment) {
				return;
			}
			if (inQuotes) {
				quoted.append(c);
			} else if (nothingButWhitespaceOnThisNewLine) {
				inComment = true;
			}
		}
	};

	private final SExpressionsStreamingLexer lexer = new SExpressionsStreamingLexer(lexerCallback);

	private final Callback callback;

	public SExpressionsStreamingParser(Callback callback) {
		this.callback = callback;
	}

	@Override
	public void accept(char c) {
		lexer.accept(c);
	}

	@Override
	public void close() throws IOException {
		lexer.close();
	}

	public enum Error {STREAM_ENDED_WHILE_IN_QUOTES}

	public interface Callback {

		void onAtom(String text);

		void onListBegin();

		void onListEnd();

		void onComment(String comment);

		void onError(Error error);

		void onOpenStream();

		void onCloseStream();
	}
}

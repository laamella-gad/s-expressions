package com.laamella.sexpression;

public class SExpressionsStreamingParser implements SExpressionsStreamingLexer.Callback {
	private boolean inQuotes;
	private StringBuilder accumulator;
	private boolean inComment;
	private boolean nothingButWhitespaceOnThisNewLine;

	private final Callback callback;

	public SExpressionsStreamingParser(Callback callback) {
		this.callback = callback;
	}

	@Override
	public void onText(String text, long start, long end) {
		if (inComment) {
			accumulator.append(text);
			return;
		}
		nothingButWhitespaceOnThisNewLine = false;
		if (inQuotes) {
			accumulator.append(text);
		} else {
			callback.onText(text);
		}
	}

	@Override
	public void onWhitespace(String whitespace, long start, long end) {
		if (inComment || inQuotes) {
			accumulator.append(whitespace);
		} else {
			callback.onWhitespace(whitespace);
		}
	}

	@Override
	public void onOpeningBrace(char b, long pos) {
		if (inComment) {
			accumulator.appendCodePoint(b);
			return;
		}
		nothingButWhitespaceOnThisNewLine = false;
		if (inQuotes) {
			accumulator.appendCodePoint(b);
		} else {
			callback.onListBegin();
		}
	}

	@Override
	public void onClosingBrace(char b, long pos) {
		if (inComment) {
			accumulator.appendCodePoint(b);
			return;
		}
		nothingButWhitespaceOnThisNewLine = false;
		if (inQuotes) {
			accumulator.appendCodePoint(b);
		} else {
			callback.onListEnd();
		}
	}

	@Override
	public void onQuote(char q, long pos) {
		if (inComment) {
			accumulator.appendCodePoint(q);
			return;
		}
		nothingButWhitespaceOnThisNewLine = false;
		if (inQuotes) {
			accumulator.appendCodePoint(q);
			callback.onText(accumulator.toString());
			accumulator = new StringBuilder();
			inQuotes = false;
		} else {
			inQuotes = true;
			accumulator.appendCodePoint(q);
		}

	}

	@Override
	public void onCloseStream() {
		if (inQuotes) {
			callback.onError(Error.STREAM_ENDED_WHILE_IN_QUOTES);
		}
		if (inComment) {
			callback.onComment(accumulator.toString());
		}
		callback.onCloseStream();
	}

	@Override
	public void onOpenStream() {
		inQuotes = false;
		accumulator = new StringBuilder();
		inComment = false;
		nothingButWhitespaceOnThisNewLine = true;
		callback.onOpenStream();
	}

	@Override
	public void onComment(char c, long pos) {
		if (inComment) {
			accumulator.appendCodePoint(c);
			return;
		}
		if (inQuotes) {
			accumulator.append(c);
		} else if (nothingButWhitespaceOnThisNewLine) {
			inComment = true;
		}
	}

	@Override
	public void onEndOfLine(long pos) {
		if (inQuotes) {
			accumulator.append("\n");
		} else {
			if (inComment) {
				callback.onComment(accumulator.toString());
				accumulator = new StringBuilder();
			}
			callback.onEndOfLine();
		}
		nothingButWhitespaceOnThisNewLine = true;
		inComment = false;
	}


	public enum Error {STREAM_ENDED_WHILE_IN_QUOTES}

	public interface Callback {

		void onText(String text);

		void onWhitespace(String whitespace);

		void onEndOfLine();

		void onListBegin();

		void onListEnd();

		void onComment(String comment);

		void onError(Error error);

		void onOpenStream();

		void onCloseStream();
	}
}

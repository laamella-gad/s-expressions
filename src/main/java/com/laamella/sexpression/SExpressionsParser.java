package com.laamella.sexpression;

import com.laamella.sexpression.model.AtomList;
import com.laamella.sexpression.model.Document;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class SExpressionsParser implements CharSink, Closeable {
	private final SExpressionsStreamingParser parser = new SExpressionsStreamingParser(new SExpressionsStreamingParser.Callback() {
		private final Deque<AtomList> stack = new ArrayDeque<>();
		private Document document = null;

		@Override
		public void onText(String text) {
			if (stack.isEmpty()) {
				// TODO #26
				document.add(text);
				callback.onOrphanText(text);
			} else {
				// TODO #26
				stack.peek().add(text);
			}
		}

		@Override
		public void onListBegin() {
			stack.push(new AtomList());
		}

		@Override
		public void onListEnd() {
			if (stack.isEmpty()) {
				callback.onError(Error.TOO_MANY_CLOSING_PARENTHESES);
				return;
			}
			AtomList finishedList = stack.pop();
			if (stack.isEmpty()) {
				document.add(finishedList);
				callback.onExpression(finishedList);
			} else {
				stack.peek().add(finishedList);
			}
		}

		@Override
		public void onComment(String comment) {
			document.add(comment);
			callback.onComment(comment);
		}

		@Override
		public void onError(SExpressionsStreamingParser.Error error) {
			switch (error) {
				case STREAM_ENDED_WHILE_IN_QUOTES:
					callback.onError(Error.STREAM_ENDED_WHILE_IN_QUOTES);
					break;
				default:
					throw new AssertionError("Problem translating unknown error that should have been handled here.");
			}
		}

		@Override
		public void onOpenStream() {
			stack.clear();
			document = new Document();
			callback.onOpenStream();
		}

		@Override
		public void onCloseStream() {
			if (stack.size() != 0) {
				callback.onError(Error.UNCLOSED_PARENTHESES);
			}
			callback.onDocument(document);
			callback.onCloseStream();
		}
	});

	private final Callback callback;

	public enum Error {TOO_MANY_CLOSING_PARENTHESES, STREAM_ENDED_WHILE_IN_QUOTES, UNCLOSED_PARENTHESES}

	public SExpressionsParser(Callback callback) {
		this.callback = callback;
	}

	public interface Callback {

		void onError(Error error);

		void onOrphanText(String text);

		void onExpression(AtomList expression);

		void onComment(String comment);

		void onOpenStream();

		void onCloseStream();

		void onDocument(Document document);

		class Adapter implements Callback {
			@Override
			public void onError(Error error) {
			}

			@Override
			public void onOrphanText(String text) {
			}

			@Override
			public void onExpression(AtomList expression) {
			}

			@Override
			public void onComment(String comment) {
			}

			@Override
			public void onOpenStream() {
			}

			@Override
			public void onCloseStream() {
			}

			@Override
			public void onDocument(Document document) {
			}
		}
	}

	@Override
	public void accept(char c) {
		parser.accept(c);
	}

	@Override
	public void close() throws IOException {
		parser.close();
	}
}

package com.laamella.sexpression;

import java.util.ArrayDeque;
import java.util.Deque;

public class SExpressionsParser implements CharSink {
    private final SExpressionsLexer.Callback lexerCallback = new SExpressionsLexer.Callback() {
        private final Deque<AtomList> stack = new ArrayDeque<>();
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
                if (stack.isEmpty()) {
                    callback.onAtom(text);
                } else {
                    stack.peek().add(text);
                }
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
        public void onOpenBrace(char b, long pos) {
            if (inComment) {
                return;
            }
            nothingButWhitespaceOnThisNewLine = false;
            if (inQuotes) {
                quoted.appendCodePoint(b);
            } else {
                stack.push(new AtomList());
            }
        }

        @Override
        public void onCloseBrace(char b, long pos) {
            if (inComment) {
                return;
            }
            nothingButWhitespaceOnThisNewLine = false;
            if (inQuotes) {
                quoted.appendCodePoint(b);
            } else {
                if (stack.isEmpty()) {
                    callback.onError(Callback.Error.TOO_MANY_CLOSING_PARENTHESES);
                    return;
                }
                AtomList finishedList = stack.pop();
                if (stack.isEmpty()) {
                    callback.onExpression(finishedList);
                } else {
                    stack.peek().add(finishedList);
                }
            }
        }

        @Override
        public void onQuote(char q, long pos) {
            if (inComment) {
                return;
            }
            nothingButWhitespaceOnThisNewLine = false;
            if (inQuotes) {
                if (stack.isEmpty()) {
                    callback.onAtom(quoted.toString());
                } else {
                    stack.peek().add(quoted.toString());
                }
                quoted = new StringBuilder();
                inQuotes = false;
            } else {
                inQuotes = true;
            }

        }

        @Override
        public void onClose() {
            if (!stack.isEmpty()) {
                callback.onError(Callback.Error.UNCLOSED_PARENTHESES);
            }
            if (inQuotes) {
                callback.onError(Callback.Error.STREAM_ENDED_WHILE_IN_QUOTES);
            }
        }

        @Override
        public void onOpen() {
            inQuotes = false;
            quoted = new StringBuilder();
            inComment = false;
            nothingButWhitespaceOnThisNewLine = true;
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

    private final SExpressionsLexer lexer = new SExpressionsLexer(lexerCallback);

    private final Callback callback;

    public SExpressionsParser(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void accept(char c) {
        lexer.accept(c);
    }

    @Override
    public void close() throws Exception {
        lexer.close();
    }

    public interface Callback {
        enum Error {TOO_MANY_CLOSING_PARENTHESES, STREAM_ENDED_WHILE_IN_QUOTES, UNCLOSED_PARENTHESES}

        void onAtom(String text);

        void onExpression(AtomList expression);

        void onError(Error error);
    }
}

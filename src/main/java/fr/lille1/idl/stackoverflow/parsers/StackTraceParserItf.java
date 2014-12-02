package fr.lille1.idl.stackoverflow.parsers;

import java.util.List;

public interface StackTraceParserItf {

	public List<StackTraceItf> parseAll(String text);
	
	public static class ParseException extends Exception {
		private static final long serialVersionUID = 8888287541440248186L;

		public ParseException(final String message) {
			super(message);
		}

		public ParseException(final String message, final Throwable e) {
			super(message, e);
		}
	}
}

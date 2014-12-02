package fr.lille1.idl.stackoverflow.parsers.java;

import de.tud.stacktraces.evaluation.datastruct.StackTraceElement;
import fr.lille1.idl.stackoverflow.parsers.StackTraceElementItf;

public class JavaStackTraceElement implements StackTraceElementItf {
	
	private StackTraceElement stackTraceElement;
	
	public JavaStackTraceElement(StackTraceElement stackTraceElement) {
		this.stackTraceElement = stackTraceElement;
	}
	
	@Override
	public String getMethod() {
		return this.stackTraceElement.getMethod();
	}

	@Override
	public String getFileName() {
		return this.stackTraceElement.getSource().split(":")[0];
	}

	@Override
	public int getLineNumber() {
		return Integer.parseInt(this.stackTraceElement.getSource().split(":")[1]);
	}

}

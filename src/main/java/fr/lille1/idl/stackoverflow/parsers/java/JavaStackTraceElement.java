package fr.lille1.idl.stackoverflow.parsers.java;

import de.tud.stacktraces.evaluation.datastruct.StackTraceElement;
import fr.lille1.idl.stackoverflow.parsers.StackTraceElementItf;

public class JavaStackTraceElement implements
		StackTraceElementItf {
	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLineNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

}

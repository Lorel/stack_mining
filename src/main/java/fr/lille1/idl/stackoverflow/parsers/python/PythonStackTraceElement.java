package fr.lille1.idl.stackoverflow.parsers.python;

import lille.iagl.entity.Frame;
import fr.lille1.idl.stackoverflow.parsers.StackTraceElementItf;

public class PythonStackTraceElement implements StackTraceElementItf {

	private Frame stackTraceElement;
	
	public PythonStackTraceElement(Frame stackTraceElement) {
		this.stackTraceElement = stackTraceElement;
	}

	@Override
	public String getMethod() {
		return this.stackTraceElement.getMethod();
	}

	@Override
	public String getFileName() {
		return this.stackTraceElement.getFile();
	}

	@Override
	public int getLineNumber() {
		return Integer.parseInt(this.stackTraceElement.getLine());
	}

}

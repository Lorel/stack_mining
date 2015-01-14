package fr.lille1.idl.stackoverflow.parsers.java;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.tud.stacktraces.evaluation.datastruct.StackTraceElement;
import fr.lille1.idl.stackoverflow.parsers.StackTraceElementItf;

public class JavaStackTraceElement implements StackTraceElementItf {
	
	private StackTraceElement stackTraceElement;
	
	public JavaStackTraceElement(StackTraceElement stackTraceElement) {
		this.stackTraceElement = stackTraceElement;
	}
	
	@Override
	public String getMethod() {
		return this.stackTraceElement.getMethod().trim();
	}

	@Override
	public String getFileName() {
		return this.stackTraceElement.getSource().split(":")[0].trim();
	}

	@Override
	public int getLineNumber() {
		try {
			return Integer.parseInt(this.stackTraceElement.getSource().split(":")[1].trim().replaceAll(" ", ""));
		} catch (NumberFormatException nfe) {
			Logger.getGlobal().log(Level.WARNING, nfe.getMessage(), nfe);
		}
		return -1;
	}

}

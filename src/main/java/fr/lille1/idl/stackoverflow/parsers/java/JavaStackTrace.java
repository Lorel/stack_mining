package fr.lille1.idl.stackoverflow.parsers.java;

import java.util.ArrayList;
import java.util.List;

import de.tud.stacktraces.evaluation.datastruct.StackTrace;
import de.tud.stacktraces.evaluation.datastruct.StackTraceElement;
import fr.lille1.idl.stackoverflow.parsers.StackTraceElementItf;
import fr.lille1.idl.stackoverflow.parsers.StackTraceItf;

public class JavaStackTrace implements StackTraceItf {
	
	private StackTrace stackTrace;
	private JavaStackTrace next;
	private JavaStackTrace parent;
	
	public JavaStackTrace(StackTrace stackTrace) {
		this.stackTrace = stackTrace;
		if (stackTrace.getCausedBy() != null)
			this.setNext(new JavaStackTrace(stackTrace.getCausedBy()));
	}

	@Override
	public List<StackTraceElementItf> getStackTraceElements() {
		List<StackTraceElementItf> elements = new ArrayList<StackTraceElementItf> ();
		for (StackTraceElement stackTraceElement : stackTrace.getElements()) {
			elements.add(new JavaStackTraceElement(stackTraceElement));
		}
		return elements;
	}

	@Override
	public int getPosition() {
		if (this.parent == null)
			return 0;
		return this.parent.getPosition() + 1;
	}

	@Override
	public void setParent(StackTraceItf stackTrace) {
		this.parent = (JavaStackTrace) stackTrace;
	}

	@Override
	public void setNext(StackTraceItf stackTrace) {
		this.next = (JavaStackTrace) stackTrace;
		stackTrace.setParent(this);
	}

	@Override
	public String getLanguage() {
		return "java";
	}

}

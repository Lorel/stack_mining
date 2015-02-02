package fr.lille1.idl.stackoverflow.parsers.python;

import java.util.ArrayList;
import java.util.List;

import lille.iagl.entity.Frame;
import lille.iagl.entity.StackTrace;
import fr.lille1.idl.stackoverflow.parsers.StackTraceElementItf;
import fr.lille1.idl.stackoverflow.parsers.StackTraceItf;

public class PythonStackTrace implements StackTraceItf {
	
	private StackTrace stackTrace;
	
	public PythonStackTrace(StackTrace stackTrace) {
		this.stackTrace = stackTrace;
	}

	@Override
	public List<StackTraceElementItf> getStackTraceElements() {
		List<StackTraceElementItf> elements = new ArrayList<StackTraceElementItf> ();
		for (Frame stackTraceElement : stackTrace.getFrames()) {
			elements.add(new PythonStackTraceElement(stackTraceElement));
		}
		return elements;
	}

    @Override
    public String getExceptionType() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
	public int getPosition() {
		return 0;
	}

	@Override
	public void setParent(StackTraceItf stackTrace) {}

	@Override
	public void setNext(StackTraceItf stackTrace) {}

	@Override
	public String getLanguage() {
		return "python";
	}

}

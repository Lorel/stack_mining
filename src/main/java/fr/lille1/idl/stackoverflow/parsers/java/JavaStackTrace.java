package fr.lille1.idl.stackoverflow.parsers.java;

import java.util.List;

import de.tud.stacktraces.evaluation.datastruct.StackTrace;
import fr.lille1.idl.stackoverflow.parsers.StackTraceElementItf;
import fr.lille1.idl.stackoverflow.parsers.StackTraceItf;

public class JavaStackTrace extends StackTrace implements StackTraceItf {

	@Override
	public List<StackTraceElementItf> getStackTraceElements() {
		return null;
	}

	@Override
	public int getPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setParent(StackTraceItf stackTrace) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNext(StackTraceItf stackTrace) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getLanguage() {
		// TODO Auto-generated method stub
		return null;
	}

}

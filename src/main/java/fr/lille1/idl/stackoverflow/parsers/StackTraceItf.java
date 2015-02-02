package fr.lille1.idl.stackoverflow.parsers;

import java.util.List;

public interface StackTraceItf {

	public List<StackTraceElementItf> getStackTraceElements();

    public String getExceptionType();
	
	public int getPosition();
	
	public void setParent(StackTraceItf stackTrace);
	
	public void setNext(StackTraceItf stackTrace);
	
	public String getLanguage();
	
}

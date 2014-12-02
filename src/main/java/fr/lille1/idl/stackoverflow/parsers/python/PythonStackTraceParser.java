package fr.lille1.idl.stackoverflow.parsers.python;

import java.util.ArrayList;
import java.util.List;

import fr.lille1.idl.stackoverflow.parsers.StackTraceItf;
import fr.lille1.idl.stackoverflow.parsers.StackTrackParserItf;
import lille.iagl.entity.StackTrace;
import lille.iagl.pythonstacktracerecognizer.PythonStackTraceRecognizer;

public class PythonStackTraceParser implements StackTrackParserItf {

	private PythonStackTraceRecognizer parser;
	
	
	
	public PythonStackTraceParser() {
		super();
		this.parser = new PythonStackTraceRecognizer();
	}



	@Override
	public List<StackTraceItf> parseText(String text) {
		List<StackTraceItf> stackTraces = new ArrayList<StackTraceItf>();
		for (StackTrace stackTrace : this.parser.getStackTrace(text)) {
			stackTraces.add(new PythonStackTrace(stackTrace));
		}
		return null;
	}

}

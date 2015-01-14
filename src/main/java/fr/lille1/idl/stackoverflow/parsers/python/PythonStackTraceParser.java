package fr.lille1.idl.stackoverflow.parsers.python;

import java.util.ArrayList;
import java.util.List;

import fr.lille1.idl.stackoverflow.parsers.StackTraceItf;
import fr.lille1.idl.stackoverflow.parsers.StackTraceParserItf;
import lille.iagl.entity.StackTrace;
import lille.iagl.pythonstacktracerecognizer.PythonStackTraceRecognizer;

public class PythonStackTraceParser implements StackTraceParserItf {

	private PythonStackTraceRecognizer parser;
	
	public PythonStackTraceParser() {
		super();
		this.parser = new PythonStackTraceRecognizer();
	}


	// TODO check if useful, unless remove
	public List<StackTraceItf> parseText(String text) {
		List<StackTraceItf> stackTraces = new ArrayList<StackTraceItf>();
		for (StackTrace stackTrace : this.parser.getStackTrace(text)) {
			stackTraces.add(new PythonStackTrace(stackTrace));
		}
		return null;
	}



	@Override
	public List<StackTraceItf> parseAll(String text) {
		List<StackTraceItf> stackTraces = new ArrayList<StackTraceItf>();
		for (StackTrace stackTrace : this.parser.getStackTrace(text)) {
			stackTraces.add(new PythonStackTrace(stackTrace));
		}
		return stackTraces;
	}

}

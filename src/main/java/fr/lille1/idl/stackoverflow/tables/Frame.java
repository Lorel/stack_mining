package fr.lille1.idl.stackoverflow.tables;

import java.io.Serializable;

public class Frame implements Serializable {

	private static final long serialVersionUID = -8766800051874102000L;

	private int id;
	
	private String methodName;
	
	private String fileName;
	
	private int lineNumber;

	public Frame(int id, String fileName, String methodName, int lineNumber) {
		super();
		this.setId(id);
		this.setMethodName(methodName);
		this.setFileName(fileName);
		this.setLineNumber(lineNumber);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	
}

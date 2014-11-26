package fr.lille1.idl.stackoverflow.tables;

import java.io.Serializable;

public class Frame implements Serializable {

	private static final long serialVersionUID = -8766800051874102000L;

	private int id;
	
	private String method_name;
	
	private String file_name;
	
	private int lineNumber;

	public Frame(int id, String method_name, String file_name, int lineNumber) {
		super();
		this.setId(id);
		this.setMethod_name(method_name);
		this.setFile_name(file_name);
		this.setLineNumber(lineNumber);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMethod_name() {
		return method_name;
	}

	public void setMethod_name(String method_name) {
		this.method_name = method_name;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	
}

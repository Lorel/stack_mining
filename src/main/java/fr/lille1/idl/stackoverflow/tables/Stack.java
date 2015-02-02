package fr.lille1.idl.stackoverflow.tables;

import java.io.Serializable;

public class Stack implements Serializable {

	private static final long serialVersionUID = 9050250416653280101L;
	
	private int id;
	
	private String language;

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    private String exceptionType;

	public Stack(int id, String language, String exceptionType) {
		super();
		this.id = id;
		this.language = language;
        this.exceptionType = exceptionType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}

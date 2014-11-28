package fr.lille1.idl.stackoverflow.models;

import java.io.Serializable;

public class StackLink implements Serializable {

	private static final long serialVersionUID = -8603679887482056904L;

	private int id;
	
	private Stack stack;
	
	private Link link;

	public StackLink(int id, Stack stack, Link link) {
		super();
		this.setId(id);
		this.setStack(stack);
		this.setLink(link);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Stack getStack() {
		return stack;
	}

	public void setStack(Stack stack) {
		this.stack = stack;
	}

	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

}

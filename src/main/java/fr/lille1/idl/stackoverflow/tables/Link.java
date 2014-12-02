package fr.lille1.idl.stackoverflow.tables;

import java.io.Serializable;

public class Link implements Serializable {

	private static final long serialVersionUID = 3244837290425523775L;

	private int id;
	
	private Frame parentFrame;
	
	private Frame childFrame;
	
	private Link next;

	public Link(int id, Frame parentFrame, Frame childFrame, Link next) {
		super();
		this.setId(id);
		this.setParentFrame(parentFrame);
		this.setChildFrame(childFrame);
		this.setNext(next);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Frame getParentFrame() {
		return parentFrame;
	}

	public void setParentFrame(Frame parentFrame) {
		this.parentFrame = parentFrame;
	}

	public Frame getChildFrame() {
		return childFrame;
	}

	public void setChildFrame(Frame childFrame) {
		this.childFrame = childFrame;
	}

	public Link getNext() {
		return next;
	}

	public void setNext(Link next) {
		this.next = next;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Link) {
			Link o = (Link) obj;
			return this.id == o.getId()
					|| ( this.getParentFrame() == o.getParentFrame()
						&& this.getChildFrame() == o.getChildFrame()
						&& this.next == o.getNext()
						);
		}
		return false;
	}
}

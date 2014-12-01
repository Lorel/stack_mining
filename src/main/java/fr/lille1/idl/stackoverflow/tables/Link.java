package fr.lille1.idl.stackoverflow.tables;

import java.io.Serializable;

public class Link implements Serializable {

	private static final long serialVersionUID = 3244837290425523775L;

	private int id;
	
	private Frame parent_frame;
	
	private Frame child_frame;
	
	private Link next;

	public Link(int id, Frame parent_frame, Frame child_frame, Link next) {
		super();
		this.setId(id);
		this.setParent_frame(parent_frame);
		this.setChild_frame(child_frame);
		this.setNext(next);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Frame getParent_frame() {
		return parent_frame;
	}

	public void setParent_frame(Frame parent_frame) {
		this.parent_frame = parent_frame;
	}

	public Frame getChild_frame() {
		return child_frame;
	}

	public void setChild_frame(Frame child_frame) {
		this.child_frame = child_frame;
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
					|| ( this.getParent_frame() == o.getParent_frame()
						&& this.getChild_frame() == o.getChild_frame()
						&& this.next == o.getNext()
						);
		}
		return false;
	}
}

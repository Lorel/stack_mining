package fr.lille1.idl.stackoverflow.tables;

import java.io.Serializable;

public class PostStack implements Serializable {

	private static final long serialVersionUID = -8337087324473581343L;

	private int id;
	
	private Post post;
	
	private Stack stack;
	
	private int position;

	public PostStack(int id, Post post, Stack stack, int position) {
		super();
		this.setId(id);
		this.setPost(post);
		this.setStack(stack);
		this.setPosition(position);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public Stack getStack() {
		return stack;
	}

	public void setStack(Stack stack) {
		this.stack = stack;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}

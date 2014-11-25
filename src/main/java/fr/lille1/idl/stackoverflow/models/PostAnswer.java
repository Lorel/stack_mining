package fr.lille1.idl.stackoverflow.models;

import java.io.Serializable;

public class PostAnswer implements Serializable {

	private static final long serialVersionUID = -8226612781750235876L;

	private int id;
	
	private Post post;
	
	private Post answer;
	
	private int score;

	public PostAnswer(int id, Post post, Post answer, int score) {
		super();
		this.setId(id);
		this.setPost(post);
		this.setAnswer(answer);
		this.setScore(score);
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

	public Post getAnswer() {
		return answer;
	}

	public void setAnswer(Post answer) {
		this.answer = answer;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
}

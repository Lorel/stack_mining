package fr.lille1.idl.stackoverflow;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by dorian on 21/11/14.
 */
public class Post implements Serializable {

    private int id;

    private String title;

    private String body;

    private int acceptedAnswer;

    private Timestamp creationDate;

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getAcceptedAnswer() {
        return acceptedAnswer;
    }

    public void setAcceptedAnswer(int acceptedAnswer) {
        this.acceptedAnswer = acceptedAnswer;
    }
}
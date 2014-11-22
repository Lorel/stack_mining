package fr.lille1.idl.stackoverflow;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by dorian on 21/11/14.
 */
@Entity
@Table(schema = "stackoverflow", name = "post")
public class Post implements Serializable {

    @Id
    private int id;

    @Column
    private String title;

    @Column
    private String body;

    @Column(name = "accepted_answer")
    private int acceptedAnswer;

    @Column(name = "creation_date")
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
package fr.lille1.idl.stackoverflow.tables;

import org.apache.commons.lang3.StringEscapeUtils;

import fr.lille1.idl.stackoverflow.persistence.PostDatabase;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dorian on 21/11/14.
 */
public class Post implements Serializable {

	private static final long serialVersionUID = -9076424213715351234L;

	private int id;

    private String title;

    private String body;

    private Post acceptedAnswer;
    
    private Integer acceptedAnswerId;

    private Timestamp creationDate;

    public Post(final XMLEvent event) {
        StartElement start = event.asStartElement();
        Attribute idAttribute = start.getAttributeByName(new QName("Id"));
        int id;
        try {
            id = Integer.parseInt(idAttribute.getValue());
        } catch (NumberFormatException e) {
            id = -1;
        }
        this.id = id;
        Attribute titleAttribute = start.getAttributeByName(new QName("Title"));
        this.title = titleAttribute.getValue();
        Attribute bodyAttribute = start.getAttributeByName(new QName("Body"));
        this.body = StringEscapeUtils.unescapeXml(bodyAttribute.getValue());
        Attribute acceptedAnswerAttribute = start.getAttributeByName(new QName("AcceptedAnswerId"));
        Integer acceptedAnswer;
        try {
            acceptedAnswer = Integer.parseInt(acceptedAnswerAttribute.getValue());
        } catch (NumberFormatException e) {
            acceptedAnswer = -1;
        }
        this.acceptedAnswerId = acceptedAnswer;
        Attribute creationDateAttribute = start.getAttributeByName(new QName("CreationDate"));
        DateFormat format = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss.S");
        Timestamp creationDate;
        try {
            format.parse(creationDateAttribute.getValue());
            creationDate = new Timestamp(format.parse(creationDateAttribute.getValue()).getTime());
        } catch (ParseException e) {
            creationDate = new Timestamp((new Date()).getTime());
        }
        this.creationDate = creationDate;
    }

    public Post() {

    }

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

    public Post getAcceptedAnswer() throws ClassNotFoundException, SQLException {    	
    	if (this.acceptedAnswer == null && this.acceptedAnswerId != null)
    		this.acceptedAnswer = PostDatabase.getInstance().find(this.acceptedAnswerId.intValue());
        return acceptedAnswer;
    }

    public void setAcceptedAnswer(Post acceptedAnswer) {
        this.acceptedAnswer = acceptedAnswer;
    }

	public Integer getAcceptedAnswerId() {
		if (this.acceptedAnswer != null && this.acceptedAnswerId == null)
			this.acceptedAnswerId = this.acceptedAnswer.getId();
		return acceptedAnswerId;
	}

	public void setAcceptedAnswerId(Integer acceptedAnswerId) {
		this.acceptedAnswerId = acceptedAnswerId;
	}
}
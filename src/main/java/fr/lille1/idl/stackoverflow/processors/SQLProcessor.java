package fr.lille1.idl.stackoverflow.processors;

import fr.lille1.idl.stackoverflow.Post;
import fr.lille1.idl.stackoverflow.persistence.PostDatabase;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by dorian on 21/11/14.
 */
public class SQLProcessor implements XMLEventProcessor {
    private PostDatabase database;

    public SQLProcessor() throws SQLException, ClassNotFoundException {
        //Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/stackoverflow?user=root&password=toor");
        this.database = new PostDatabase(connection);
    }

    @Override
    public void process(final XMLEvent event) throws Exception {
        StartElement start = event.asStartElement();
        Post post = new Post();
        Attribute idAttribute = start.getAttributeByName(new QName("Id"));
        post.setId(Integer.parseInt(idAttribute.getValue()));
        Attribute titleAttribute = start.getAttributeByName(new QName("Title"));
        post.setTitle(titleAttribute.getValue());
        Attribute bodyAttribute = start.getAttributeByName(new QName("Body"));
        post.setBody(bodyAttribute.getValue());
        Attribute acceptedAnswer = start.getAttributeByName(new QName("AcceptedAnswerId"));
        post.setAcceptedAnswer(Integer.parseInt(acceptedAnswer.getValue()));
        Attribute creationDateAttribute = start.getAttributeByName(new QName("CreationDate"));
        DateFormat format = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss.S");
        format.parse(creationDateAttribute.getValue());
        Timestamp creationDate = new Timestamp(format.parse(creationDateAttribute.getValue()).getTime());
        post.setCreationDate(creationDate);
        database.insert(post);
    }

    @Override
    public void close() throws Exception {
        this.database.close();
    }
}

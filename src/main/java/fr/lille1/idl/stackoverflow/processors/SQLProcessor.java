package fr.lille1.idl.stackoverflow.processors;

import fr.lille1.idl.stackoverflow.Post;
import fr.lille1.idl.stackoverflow.persistence.PostDatabase;

import javax.xml.stream.events.XMLEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by dorian on 21/11/14.
 */
public class SQLProcessor implements XMLEventProcessor {
    private PostDatabase database;

    public SQLProcessor() throws SQLException, ClassNotFoundException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/stackoverflow?user=root&password=toor");
        this.database = new PostDatabase(connection);
    }

    @Override
    public void process(final XMLEvent event) throws Exception {
        Post post = new Post(event);
        database.insert(post);
    }

    @Override
    public void close() throws Exception {
        this.database.close();
    }
}

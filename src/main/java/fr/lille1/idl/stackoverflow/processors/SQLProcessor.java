package fr.lille1.idl.stackoverflow.processors;

import fr.lille1.idl.stackoverflow.Configuration;
import fr.lille1.idl.stackoverflow.persistence.PostDatabase;
import fr.lille1.idl.stackoverflow.tables.Post;

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
        this.database = new PostDatabase(getConnection());
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
    	Class.forName("com.mysql.jdbc.Driver");
        Configuration configuration = Configuration.getConfiguration();
        String host = configuration.getProperty("db.host", "127.0.0.1");
        int port = Integer.parseInt(configuration.getProperty("db.port", "3306"));
        String database = configuration.getProperty("db.database", "stackoverflow");
        String user = configuration.getProperty("db.user", "stackoverflow");
        String password = configuration.getProperty("db.password", "stackoverflow");
        Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database +
                "?user=" + user + "&password=" + password);
		return connection;
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

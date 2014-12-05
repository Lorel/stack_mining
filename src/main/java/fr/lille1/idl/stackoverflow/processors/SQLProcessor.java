package fr.lille1.idl.stackoverflow.processors;

import de.tud.stacktraces.evaluation.datastruct.StackTrace;
import de.tud.stacktraces.evaluation.datastruct.StackTraceParser;
import fr.lille1.idl.stackoverflow.Configuration;
import fr.lille1.idl.stackoverflow.persistence.PostDatabase;
import fr.lille1.idl.stackoverflow.tables.Post;
import fr.lille1.idl.stackoverflow.tables.PostStack;
import fr.lille1.idl.stackoverflow.tables.Stack;

import javax.xml.stream.events.XMLEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by dorian on 21/11/14.
 */
public class SQLProcessor implements XMLEventProcessor {
    private PostDatabase database;

    public SQLProcessor() throws SQLException, ClassNotFoundException {
        this.database = new PostDatabase(getConnection());
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Configuration configuration = Configuration.getConfiguration();
        String host = configuration.getProperty("db.host", "127.0.0.1");
        int port = Integer.parseInt(configuration.getProperty("db.port", "3306"));
        String database = configuration.getProperty("db.database", "stackoverflow");
        String user = configuration.getProperty("db.user", "stackoverflow");
        String password = configuration.getProperty("db.password", "stackoverflow");
	String driver = configuration.getProperty("db.driver", "com.mysql.jdbc.Driver");
        Class.forName(driver);
	String protocol = "";
	if (driver.toLowerCase().contains("mysql")) {
		protocol = "mysql";
	} else if (driver.toLowerCase().contains("postgresql")) {
		protocol = "postgresql";
	}
        Connection connection = DriverManager.getConnection("jdbc:" + protocol + "://" + host + ":" + port + "/" + database +
                "?user=" + user + "&password=" + password);
        return connection;
    }

    public void process(final XMLEvent event) throws Exception {
        Post post = new Post(event);
        database.insert(post);
        List<StackTrace> traces = StackTraceParser.parseAll(post.getBody());
        for (int i = 0; i < traces.size(); i++) {
            StackTrace trace = traces.get(i);
            Stack stack = database.insert(trace);
            PostStack postStack = new PostStack(0, post, stack, i);
            database.insert(postStack);
        }
    }

    public void close() throws Exception {
        this.database.close();
    }
}

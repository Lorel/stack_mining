package fr.lille1.idl.stackoverflow.persistence;

import fr.lille1.idl.stackoverflow.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dorian on 22/11/14.
 */
public class PostDatabase {
    public static enum OPERATIONS {
        INSERT
    }

    public static final int BUFFER_SIZE = 1000;

    private Connection connection;
    private Map<OPERATIONS, PreparedStatement> statements = null;
    private int counter = 0;

    public PostDatabase(Connection connection) throws SQLException {
        this.connection = connection;
        this.statements = new HashMap<OPERATIONS, PreparedStatement>();
        String insertStatement = "INSERT INTO Post(id, title, body, accepted_answer, creation_date) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement insertPreparedStatement = this.connection.prepareStatement(insertStatement);
        this.statements.put(OPERATIONS.INSERT, insertPreparedStatement);
    }

    public void insert(final Post post) throws SQLException {
        PreparedStatement statement = this.statements.get(OPERATIONS.INSERT);
        statement.setInt(1, post.getId());
        statement.setString(2, post.getTitle());
        statement.setString(3, post.getBody());
        statement.setInt(4, post.getAcceptedAnswer());
        statement.setTimestamp(5, post.getCreationDate());
        statement.addBatch();
        ++counter;
        if (counter == BUFFER_SIZE) {
            this.flush();
            counter = 0;
        }
    }

    public void flush() throws SQLException {
        PreparedStatement statement = this.statements.get(OPERATIONS.INSERT);
        statement.executeBatch();
        statement.clearParameters();
    }

    public void close() throws SQLException {
        this.flush();
        for (PreparedStatement ps : this.statements.values()) {
            ps.close();
        }
        this.connection.close();
    }
}

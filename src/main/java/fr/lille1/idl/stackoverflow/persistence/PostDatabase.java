package fr.lille1.idl.stackoverflow.persistence;

import fr.lille1.idl.stackoverflow.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dorian on 22/11/14.
 */
public class PostDatabase {

    public static enum OPERATIONS {
        INSERT,
        FIND_BY_ID,
        LIST_IDS
    }


    private Connection connection;
    private Map<OPERATIONS, PreparedStatement> statements = null;

    public PostDatabase(Connection connection) throws SQLException {
        this.connection = connection;
        this.statements = new HashMap<OPERATIONS, PreparedStatement>();
        String insertStatement = "INSERT INTO post(id, title, body, accepted_answer, creation_date) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement insertPreparedStatement = this.connection.prepareStatement(insertStatement);
        this.statements.put(OPERATIONS.INSERT, insertPreparedStatement);
        String findStatement = "SELECT * FROM post WHERE id = ?";
        PreparedStatement findPreparedStatement = this.connection.prepareStatement(findStatement);
        this.statements.put(OPERATIONS.FIND_BY_ID, findPreparedStatement);
        String listIdsStatement = "SELECT id FROM post";
        PreparedStatement listIdsPreparedStatement = this.connection.prepareStatement(listIdsStatement);
        this.statements.put(OPERATIONS.LIST_IDS, listIdsPreparedStatement);
    }

    /**
     * Insert a post in the database.
     *
     * @param post The post to insert
     * @throws SQLException If the post already exists in the database (if the id is already in use) or an error occurs while executing the query.
     */
    public void insert(final Post post) throws SQLException {
        PreparedStatement statement = this.statements.get(OPERATIONS.INSERT);
        statement.setInt(1, post.getId());
        statement.setString(2, post.getTitle());
        statement.setString(3, post.getBody());
        statement.setInt(4, post.getAcceptedAnswer());
        statement.setTimestamp(5, post.getCreationDate());
        statement.executeUpdate();
    }

    /**
     * Find a post matching an id in the database.
     *
     * @param id Id of the post
     * @return A post if the id exists in the database, null otherwise
     * @throws SQLException If an error occurs while executing or fetching the results from the query.
     */
    public Post find(final int id) throws SQLException {
        PreparedStatement statement = this.statements.get(OPERATIONS.FIND_BY_ID);
        statement.setInt(1, id);
        final ResultSet resultSet = statement.executeQuery();
        statement.clearParameters();
        if (!resultSet.next()) {
            resultSet.close();
            return null;
        }
        int acceptedAnswer = resultSet.getInt("accepted_answer");
        String title = resultSet.getString("title");
        String body = resultSet.getString("body");
        Timestamp creationDate = resultSet.getTimestamp("creation_date");
        resultSet.close();
        Post post = new Post();
        post.setId(id);
        post.setTitle(title);
        post.setBody(body);
        post.setAcceptedAnswer(acceptedAnswer);
        post.setCreationDate(creationDate);
        return post;
    }

    /**
     * Get the complete list of post ids.
     *
     * @return a list of ids
     * @throws SQLException If an error occurs while executing the query
     */
    public List<Integer> listIds() throws SQLException {
        final PreparedStatement statement = this.statements.get(OPERATIONS.LIST_IDS);
        final ResultSet resultSet = statement.executeQuery();
        List<Integer> ids = new ArrayList<Integer>();
        while (resultSet.next()) {
            ids.add(resultSet.getInt("id"));
        }
        resultSet.close();
        return ids;
    }

    /**
     * Properly close the statements open during the object's lifecycle.
     *
     * @throws SQLException If an error occurs while trying close a statement.
     */
    public void close() throws SQLException {
        for (PreparedStatement ps : this.statements.values()) {
            ps.close();
        }
        this.connection.close();
    }
}

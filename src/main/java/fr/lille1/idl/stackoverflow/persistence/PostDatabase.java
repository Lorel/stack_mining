package fr.lille1.idl.stackoverflow.persistence;

import fr.lille1.idl.stackoverflow.models.Frame;
import fr.lille1.idl.stackoverflow.models.Post;
import fr.lille1.idl.stackoverflow.processors.SQLProcessor;

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
        LIST_IDS,
        INSERT_FRAME;
    }

    private static String ID = "id";
    private static String TITLE = "title";
    private static String BODY = "body";
    private static String ACCEPTED_ANSWER = "accepted_answer_id";
    private static String DATE = "creation_date";

    private Connection connection;
    private Map<OPERATIONS, PreparedStatement> statements = null;

    public PostDatabase(Connection connection) throws SQLException {
        this.connection = connection;
        this.statements = new HashMap<OPERATIONS, PreparedStatement>();
        String insertStatement = "INSERT INTO post(" + ID + ", " + TITLE + ", " + BODY + ", " + ACCEPTED_ANSWER + ", " + DATE + ") VALUES(?, ?, ?, ?, ?)";
        PreparedStatement insertPreparedStatement = this.connection.prepareStatement(insertStatement);
        this.statements.put(OPERATIONS.INSERT, insertPreparedStatement);
        String findStatement = "SELECT * FROM post WHERE " + ID + " = ?";
        PreparedStatement findPreparedStatement = this.connection.prepareStatement(findStatement);
        this.statements.put(OPERATIONS.FIND_BY_ID, findPreparedStatement);
        String listIdsStatement = "SELECT " + ID + " FROM post";
        PreparedStatement listIdsPreparedStatement = this.connection.prepareStatement(listIdsStatement);
        this.statements.put(OPERATIONS.LIST_IDS, listIdsPreparedStatement);
        String insertFrameStatement = "INSERT INTO frame(file_name, method_name, line_number) VALUES(?, ?, ?);";
        PreparedStatement insertFrame = connection.prepareStatement(insertFrameStatement);
        this.statements.put(OPERATIONS.INSERT_FRAME, insertFrame);
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
        statement.setInt(4, post.getAcceptedAnswerId());
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
        int acceptedAnswer = resultSet.getInt(ACCEPTED_ANSWER);
        String title = resultSet.getString(TITLE);
        String body = resultSet.getString(BODY);
        Timestamp creationDate = resultSet.getTimestamp(DATE);
        resultSet.close();
        Post post = new Post();
        post.setId(id);
        post.setTitle(title);
        post.setBody(body);
        post.setAcceptedAnswerId(Integer.valueOf(acceptedAnswer));
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
            ids.add(resultSet.getInt(ID));
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

    public static PostDatabase getInstance() throws ClassNotFoundException, SQLException {
        return new PostDatabase(SQLProcessor.getConnection());
    }

    public Frame insert(Frame frame) throws SQLException {
        PreparedStatement statement = statements.get(OPERATIONS.INSERT_FRAME);
        statement.setString(1, frame.getFileName());
        statement.setString(2, frame.getMethodName());
        statement.setInt(3, frame.getLineNumber());
        /*ResultSet resultSet = */
        statement.execute();
        /*
        resultSet.next();
        int id= resultSet.getInt("id");
        String filename = resultSet.getString("filemane");
        String methodName = resultSet.getString("method");
        statement.close();
        int lineNumber = resultSet.getInt("line_number");
        frame = new Frame(id, filename, methodName, lineNumber);
        */
        statement.clearParameters();
        statement.close();
        return null;
    }
}

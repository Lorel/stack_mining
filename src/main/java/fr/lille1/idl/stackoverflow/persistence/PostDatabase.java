package fr.lille1.idl.stackoverflow.persistence;

import de.tud.stacktraces.evaluation.datastruct.*;
import de.tud.stacktraces.evaluation.datastruct.StackTraceElement;
import fr.lille1.idl.stackoverflow.models.*;
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
        INSERT_FRAME,
        INSERT_LINK,
        INSERT_STACK,
        INSERT_STACKLINK,
        INSERT_POSTSTACK;
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
        PreparedStatement insertFrame = connection.prepareStatement(insertFrameStatement, Statement.RETURN_GENERATED_KEYS);
        this.statements.put(OPERATIONS.INSERT_FRAME, insertFrame);
        String insertLinkStatement = "INSERT INTO link(parent_frame_id, child_frame_id, next_id) VALUES(?, ?, ?);";
        PreparedStatement insertLink = connection.prepareStatement(insertLinkStatement, Statement.RETURN_GENERATED_KEYS);
        this.statements.put(OPERATIONS.INSERT_LINK, insertLink);
        String insertStackStatement = "INSERT INTO stack(language) VALUES(?);";
        PreparedStatement insertStack = connection.prepareStatement(insertStackStatement, Statement.RETURN_GENERATED_KEYS);
        this.statements.put(OPERATIONS.INSERT_STACK, insertStack);
        String insertStackLinkStatement = "INSERT INTO stack_link(stack_id, link_id) VALUES(?, ?) ";
        PreparedStatement insertStackLink = connection.prepareStatement(insertStackLinkStatement, Statement.RETURN_GENERATED_KEYS);
        this.statements.put(OPERATIONS.INSERT_STACKLINK, insertStackLink);
        String insertPostStackStatement = "INSERT INTO post_stack(post_id, stack_id, position) VALUES(?, ?, ?);";
        PreparedStatement insertPostStack = connection.prepareStatement(insertPostStackStatement, Statement.RETURN_GENERATED_KEYS);
        this.statements.put(OPERATIONS.INSERT_POSTSTACK, insertPostStack);
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
        statement.executeUpdate();
        ResultSet resultset = statement.getGeneratedKeys();
        resultset.next();
        int id = resultset.getInt(1);
        statement.clearParameters();
        frame.setId(id);
        return frame;
    }

    public Link insert(Link link) throws SQLException {
        PreparedStatement statement = statements.get(OPERATIONS.INSERT_LINK);
        statement.setInt(1, link.getParent_frame().getId());
        statement.setInt(2, link.getChild_frame().getId());
        if (link.getNext() != null) {
            statement.setInt(3, link.getNext().getId());
        } else {
            statement.setNull(3, Types.INTEGER);
        }
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        resultSet.next();
        int id = resultSet.getInt(1);
        link.setId(id);
        statement.clearParameters();
        return link;
    }

    public Stack insert(Stack stack) throws SQLException {
        PreparedStatement statement = statements.get(OPERATIONS.INSERT_STACK);
        statement.setString(1, stack.getLanguage());
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        resultSet.next();
        int id = resultSet.getInt(1);
        stack.setId(id);
        statement.clearParameters();
        return stack;
    }

    public StackLink insert(StackLink stackLink) throws SQLException {
        PreparedStatement statement = statements.get(OPERATIONS.INSERT_STACKLINK);
        statement.setInt(1, stackLink.getStack().getId());
        statement.setInt(2, stackLink.getLink().getId());
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        resultSet.next();
        int id = resultSet.getInt(1);
        stackLink.setId(id);
        statement.clearParameters();
        return stackLink;
    }

    public PostStack insert(PostStack postStack) throws SQLException {
        PreparedStatement statement = statements.get(OPERATIONS.INSERT_POSTSTACK);
        statement.setInt(1, postStack.getPost().getId());
        statement.setInt(2, postStack.getStack().getId());
        statement.setInt(3, postStack.getPosition());
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        resultSet.next();
        int id = resultSet.getInt(1);
        postStack.setId(id);
        statement.clearParameters();
        return postStack;
    }

    public Stack insert(StackTrace stackTrace) throws SQLException {
        List<StackTraceElement> elements = stackTrace.getElements();
        List<Link> links = new ArrayList<Link>();
        Frame parent = null;
        for (int i = elements.size() - 1; i >= 0; i--) {
            StackTraceElement element = elements.get(i);
            String[] tokens = element.getSource().split(":");
            String fileName = tokens[0];
            String methodName = element.getMethod();
            int lineNumber = Integer.parseInt(tokens[1]);
            Frame child = new Frame(0, fileName, methodName, lineNumber);
            child = this.insert(child);
            if (parent != null) {
                Link link = new Link(0, parent, child, null);
                links.add(link);
            }
            parent = child;
        }
        Stack stack = new Stack(0, "java");
        stack = this.insert(stack);
        Link parentLink = null;
        for (Link link : links) {
            if (parentLink != null) {
                link.setNext(parentLink);
            }
            link = this.insert(link);
            StackLink stackLink = new StackLink(0, stack, link);
            stackLink = this.insert(stackLink);
            parentLink = link;
        }
        return stack;
    }
}

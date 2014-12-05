package fr.lille1.idl.stackoverflow.persistence;

import fr.lille1.idl.stackoverflow.parsers.StackTraceElementItf;
import fr.lille1.idl.stackoverflow.parsers.StackTraceItf;
import fr.lille1.idl.stackoverflow.parsers.java.JavaStackTrace;
import fr.lille1.idl.stackoverflow.parsers.python.PythonStackTrace;
import fr.lille1.idl.stackoverflow.processors.SQLProcessor;
import fr.lille1.idl.stackoverflow.tables.*;
import fr.lille1.idl.stackoverflow.tables.Stack;

import java.sql.*;
import java.util.*;

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
        INSERT_POSTSTACK,
        GET_FRAME,
        GET_LINK,
        FIND_LINK_BY_ID,
        FIND_FRAME_BY_ID;
    }

    private static String ID = "id";

    // POSTS
    private static String TITLE = "title";
    private static String BODY = "body";
    private static String ACCEPTED_ANSWER = "accepted_answer_id";
    private static String DATE = "creation_date";
    private static String SCORE = "score";

    // LINKS
    private static String PARENT_FRAME = "parent_frame_id";
    private static String CHILD_FRAME = "child_frame_id";
    private static String NEXT = "next_id";

    // FRAMES
    private static String FILE_NAME = "file_name";
    private static String METHOD_NAME = "method_name";
    private static String LINE_NUMBER = "line_number";

    private Connection connection;
    private Map<OPERATIONS, PreparedStatement> statements = null;

    public PostDatabase(Connection connection) throws SQLException {
        this.connection = connection;
        this.statements = new HashMap<OPERATIONS, PreparedStatement>();

        String insertStatement = "INSERT INTO stackoverflow.post(" + ID + ", " + TITLE + ", " + BODY + ", " + ACCEPTED_ANSWER + ", " + DATE + ", " + SCORE + ") VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement insertPreparedStatement = this.connection.prepareStatement(insertStatement);
        this.statements.put(OPERATIONS.INSERT, insertPreparedStatement);
        String findStatement = "SELECT * FROM stackoverflow.post WHERE " + ID + " = ?";
        PreparedStatement findPreparedStatement = this.connection.prepareStatement(findStatement);
        this.statements.put(OPERATIONS.FIND_BY_ID, findPreparedStatement);
        String listIdsStatement = "SELECT " + ID + " FROM stackoverflow.post";
        PreparedStatement listIdsPreparedStatement = this.connection.prepareStatement(listIdsStatement);
        this.statements.put(OPERATIONS.LIST_IDS, listIdsPreparedStatement);
        String insertFrameStatement = "INSERT INTO stackoverflow.frame(file_name, method_name, line_number) VALUES(?, ?, ?);";
        PreparedStatement insertFrame = connection.prepareStatement(insertFrameStatement, Statement.RETURN_GENERATED_KEYS);
        this.statements.put(OPERATIONS.INSERT_FRAME, insertFrame);
        String insertLinkStatement = "INSERT INTO stackoverflow.link(parent_frame_id, child_frame_id, next_id) VALUES(?, ?, ?);";
        PreparedStatement insertLink = connection.prepareStatement(insertLinkStatement, Statement.RETURN_GENERATED_KEYS);
        this.statements.put(OPERATIONS.INSERT_LINK, insertLink);
        String insertStackStatement = "INSERT INTO stackoverflow.stack(language, exception_type) VALUES(?, ?);";
        PreparedStatement insertStack = connection.prepareStatement(insertStackStatement, Statement.RETURN_GENERATED_KEYS);
        this.statements.put(OPERATIONS.INSERT_STACK, insertStack);
        String insertStackLinkStatement = "INSERT INTO stackoverflow.stack_link(stack_id, link_id) VALUES(?, ?) ";
        PreparedStatement insertStackLink = connection.prepareStatement(insertStackLinkStatement, Statement.RETURN_GENERATED_KEYS);
        this.statements.put(OPERATIONS.INSERT_STACKLINK, insertStackLink);
        String insertPostStackStatement = "INSERT INTO stackoverflow.post_stack(post_id, stack_id, position) VALUES(?, ?, ?);";
        PreparedStatement insertPostStack = connection.prepareStatement(insertPostStackStatement, Statement.RETURN_GENERATED_KEYS);
        this.statements.put(OPERATIONS.INSERT_POSTSTACK, insertPostStack);
        String getFrameStatement = "SELECT id FROM stackoverflow.frame WHERE file_name = ? AND method_name = ? AND line_number = ?";
        PreparedStatement getFrame = connection.prepareStatement(getFrameStatement);
        this.statements.put(OPERATIONS.GET_FRAME, getFrame);
        String getLinkStatement = "SELECT id FROM stackoverflow.link WHERE parent_frame_id = ? AND child_frame_id = ? AND next_id = ?";
        PreparedStatement getLink = connection.prepareStatement(getLinkStatement);
        this.statements.put(OPERATIONS.GET_LINK, getLink);
        
        String findLinkStatement = "SELECT * FROM link WHERE " + ID + " = ?";
        PreparedStatement findLinkPreparedStatement = this.connection.prepareStatement(findLinkStatement);
        this.statements.put(OPERATIONS.FIND_LINK_BY_ID, findLinkPreparedStatement);
        
        String findFrameStatement = "SELECT * FROM frame WHERE " + ID + " = ?";
        PreparedStatement findFramePreparedStatement = this.connection.prepareStatement(findFrameStatement);
        this.statements.put(OPERATIONS.FIND_FRAME_BY_ID, findFramePreparedStatement);
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
        statement.setInt(6, post.getScore());
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
        int score = resultSet.getInt(SCORE);
        resultSet.close();
        Post post = new Post();
        post.setId(id);
        post.setTitle(title);
        post.setBody(body);
        post.setAcceptedAnswerId(Integer.valueOf(acceptedAnswer));
        post.setCreationDate(creationDate);
        post.setScore(score);
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

    public Frame getExisting(Frame frame) throws SQLException {
        String filename = frame.getFileName();
        String methodName = frame.getMethodName();
        int lineNumber = frame.getLineNumber();
        PreparedStatement statement = statements.get(OPERATIONS.GET_FRAME);
        statement.setString(1, filename);
        statement.setString(2, methodName);
        statement.setInt(3, lineNumber);
        ResultSet resultSet = statement.executeQuery();
        statement.clearParameters();
        if (!resultSet.next()) {
            return null;
        }
        int id = resultSet.getInt(1);
        Frame existingFrame = new Frame(id, filename, methodName, lineNumber);
        return existingFrame;
    }

    public Frame insert(Frame frame) throws SQLException {
        Frame existingFrame = getExisting(frame);
        if (existingFrame != null) {
            return existingFrame;
        }
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

    public Link getExisting(Link link) throws SQLException {
        Frame parent = link.getParentFrame();
        Frame child = link.getChildFrame();
        Link next = link.getNext();
        PreparedStatement statement = statements.get(OPERATIONS.GET_LINK);
        statement.setInt(1, parent.getId());
        statement.setInt(2, child.getId());
        if (next == null) {
            statement.setNull(3, Types.INTEGER);
            statement.setNull(4, Types.INTEGER);
        } else {
            statement.setInt(3, next.getId());
            statement.setInt(4, next.getId());
        }
        ResultSet resultSet = statement.executeQuery();
        statement.clearParameters();
        if (!resultSet.next()) {
            return null;
        }
        int id = resultSet.getInt(1);
        Link existingLink = new Link(id, parent, child, next);
        return existingLink;
    }

    public Link insert(Link link) throws SQLException {
        Link existingLink = getExisting(link);
        if (existingLink != null) {
            return existingLink;
        }
        PreparedStatement statement = statements.get(OPERATIONS.INSERT_LINK);
        statement.setInt(1, link.getParentFrame().getId());
        statement.setInt(2, link.getChildFrame().getId());
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
        statement.setString(2, stack.getExceptionType());
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

    public Stack insert(StackTraceItf stackTrace) throws SQLException {
        List<StackTraceElementItf> elements = stackTrace.getStackTraceElements();
        List<Link> links = new ArrayList<Link>();
        Frame parent = null;
        for (int i = elements.size() - 1; i >= 0; i--) {
            StackTraceElementItf element = elements.get(i);
            String fileName = element.getFileName();
            String methodName = element.getMethod();
            int lineNumber = element.getLineNumber();
            Frame child = new Frame(0, fileName, methodName, lineNumber);
            child = this.insert(child);
            if (parent != null) {
                Link link = new Link(0, parent, child, null);
                links.add(link);
            }
            parent = child;
        }
        String language = "unknown";
        if (stackTrace instanceof JavaStackTrace) {
            language = "java";
        } else if (stackTrace instanceof PythonStackTrace) {
            language = "python";
        }
        Stack stack = new Stack(0, language, stackTrace.getExceptionType().trim());
        stack = this.insert(stack);
        Link parentLink = null;
        for (Link link : links) {
            if (parentLink != null) {
                link.setNext(parentLink);
            }
            link = this.insert(link);
            StackLink stackLink = new StackLink(0, stack, link);
            this.insert(stackLink);
            parentLink = link;
        }
        return stack;
    }

    public Link find_link_by_id(int id) throws SQLException {
        PreparedStatement statement = this.statements.get(OPERATIONS.FIND_LINK_BY_ID);
        statement.setInt(1, id);
        final ResultSet resultSet = statement.executeQuery();
        statement.clearParameters();
        if (!resultSet.next()) {
            resultSet.close();
            return null;
        }
        int parentFrameId = resultSet.getInt(PARENT_FRAME);
        int childFrameId = resultSet.getInt(CHILD_FRAME);
        int nextId = resultSet.getInt(NEXT);
        resultSet.close();
        Link link = new Link(id, this.find_frame_by_id(parentFrameId), this.find_frame_by_id(childFrameId), this.find_link_by_id(nextId));
        return link;
    }

    public Frame find_frame_by_id(int id) throws SQLException {
        PreparedStatement statement = this.statements.get(OPERATIONS.FIND_FRAME_BY_ID);
        statement.setInt(1, id);
        final ResultSet resultSet = statement.executeQuery();
        statement.clearParameters();
        if (!resultSet.next()) {
            resultSet.close();
            return null;
        }
        String fileName = resultSet.getString(FILE_NAME);
        String methodName = resultSet.getString(METHOD_NAME);
        int lineNumber = resultSet.getInt(LINE_NUMBER);
        Frame frame = new Frame(id, fileName, methodName, lineNumber);
        return frame;
    }

    /**
     * Transforms a StackTraceElement into  a Frame.
     *
     * @param element original element
     * @return A Frame
     */
    private Frame toFrame(final StackTraceElementItf element) {
        String fileName = element.getFileName();
        String methodName = element.getMethod();
        int lineNumber = element.getLineNumber();

        Frame frame = new Frame(0, fileName, methodName, lineNumber);
        return frame;
    }

    /**
     * Look for an identical stack trace in the database.
     *
     * @param stackTrace Stack trace to look for
     * @return A list of posts containing an identical stack trace
     */
    public List<Post> find(StackTraceItf stackTrace) throws SQLException {
        List<Post> posts = new ArrayList<Post>();
        //Test if the frames exist in the database
        List<StackTraceElementItf> elements = stackTrace.getStackTraceElements();
        List<Frame> frames = new ArrayList<Frame>();
        for (int i = elements.size() - 1; i >= 0; i--) {
            StackTraceElementItf element = elements.get(i);
            Frame existingFrame = getExisting(toFrame(element));
            if (existingFrame == null) {
                return posts;
            }
            frames.add(existingFrame);
        }
        String statement = "SELECT p.id, p.accepted_answer_id FROM post p, post_stack ps, stack s ";
        String from = "";
        String where = "";
        for (int i = 0; i < frames.size() - 1; i++) {
            String linkName = "l" + i;
            String stackLinkName = "sl" + i;
            from += ", stack_link " + stackLinkName + ", link " + linkName;
            where += " AND \n" + stackLinkName + ".stack_id = s.id  AND " + stackLinkName + ".link_id = " + linkName + ".id";
            where += " AND " + linkName + ".parent_frame_id = ? AND " + linkName + ".child_frame_id = ?";
        }
        statement += from;
        statement += " WHERE p.id = ps.post_id AND ps.stack_id = s.id ";
        statement += where;
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        int i = 1;
        int length = (frames.size() - 1) * 2;
        if (length > 0) {
            Iterator<Frame> it = frames.iterator();
            Frame first = it.next();
            Frame second = it.next();
            while (i <= length && first != null && second != null) {
                preparedStatement.setInt(i, first.getId());
                preparedStatement.setInt(i + 1, second.getId());
                first = second;
                if (it.hasNext()) {
                    second = it.next();
                }
                i += 2;
            }
        }
        ResultSet set = preparedStatement.executeQuery();
        while (set.next()) {
            int id = set.getInt(1);
            Post post = find(id);
            posts.add(post);
        }
        return posts;
    }
}

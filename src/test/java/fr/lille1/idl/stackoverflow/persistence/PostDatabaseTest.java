package fr.lille1.idl.stackoverflow.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;
import de.tud.stacktraces.evaluation.datastruct.StackTrace;
import de.tud.stacktraces.evaluation.datastruct.StackTraceParser;
import fr.lille1.idl.stackoverflow.Configuration;
import fr.lille1.idl.stackoverflow.tables.Frame;
import fr.lille1.idl.stackoverflow.tables.Link;
import fr.lille1.idl.stackoverflow.tables.Post;

public class PostDatabaseTest {
	
	@Before
	public void setUp () {
		Configuration configuration = Configuration.getConfiguration();
		try {
			configuration.load(new FileInputStream("src/main/resources/config.properties"));
		} catch (FileNotFoundException e) {
			fail("Unexpected FileNotFoundException");
		} catch (IOException e) {
			fail("Unexpected IOException");
		}
	}
	
	@Test
	public void testPostDatabase() throws ClassNotFoundException, SQLException {
		PostDatabase postDatabase = PostDatabase.getInstance();
		Post post = postDatabase.find(173487);
		assertNotNull(post);
		List<StackTrace> stackTraceList = StackTraceParser.parseAll(post.getBody());
		StackTrace stackTrace = stackTraceList.get(0);
		int count = 0;
		while (stackTrace != null) {
			stackTrace = stackTrace.getCausedBy();
			count++;
		}
		assertEquals(4, count);
	}
	
	@Test
	public void testGetExistingLink() throws ClassNotFoundException, SQLException {
		PostDatabase postDatabase = PostDatabase.getInstance();
		Link link = postDatabase.find_link_by_id(1);
		
		if (link == null) {
			Frame frame1 = postDatabase.find_frame_by_id(1);
			if (frame1 == null) {
				frame1 = postDatabase.insert(new Frame(0,"testFrameFile","testFrameMethod", 42));
			}
			Frame frame2 = postDatabase.find_frame_by_id(2);
			if (frame2 == null) {
				frame2 = postDatabase.insert(new Frame(0,"testFrameFile2","testFrameMethod2", 43));
			}
			link = postDatabase.insert(new Link(0, frame1, frame2, null));
		}
		
		assertNotNull(link);
		
		Link searchedLink = new Link(0, link.getParentFrame(), link.getChildFrame(), link.getNext());
		
		assertEquals(link, postDatabase.getExisting(searchedLink));
	}
}

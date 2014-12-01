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
}

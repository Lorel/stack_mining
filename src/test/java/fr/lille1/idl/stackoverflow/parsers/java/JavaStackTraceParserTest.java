package fr.lille1.idl.stackoverflow.parsers.java;

import fr.lille1.idl.stackoverflow.parsers.StackTraceItf;

import java.util.List;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JavaStackTraceParserTest {

	@Test
	public void parseAllTest() {
		String text = "java.net.ConnectException: Connection refused: connect]]]\n"
				+ "at org.jnp.interfaces.NamingContext.checkRef(NamingContext.java:1385)\n"
				+ "at org.jnp.interfaces.NamingContext.lookup(NamingContext.java:579)\n"
				+ "at org.jnp.interfaces.NamingContext.lookup(NamingContext.java:572)\n"
				+ "at javax.naming.InitialContext.lookup(InitialContext.java:347)\n"
				+ "at org.jboss.Shutdown.main(Shutdown.java:202)\n"
				+ "Caused by: java.net.ConnectException: Connection refused: connect]]\n"
				+ "at org.jnp.interfaces.NamingContext.getServer(NamingContext.java:254)\n"
				+ "at org.jnp.interfaces.NamingContext.checkRef(NamingContext.java:1370)\n"
				+ "Caused by: java.net.ConnectException: Connection refused: connect]\n"
				+ "at org.jnp.interfaces.NamingContext.getServer(NamingContext.java:228)\n"
				+ "Caused by: java.net.ConnectException: Connection refused: connect\n"
				+ "at java.net.PlainSocketImpl.socketConnect(Native Method)\n"
				+ "at java.net.PlainSocketImpl.doConnect(PlainSocketImpl.java:305)\n"
				+ "at java.net.PlainSocketImpl.connectToAddress(PlainSocketImpl.java:171)\n"
				+ "at java.net.PlainSocketImpl.connect(PlainSocketImpl.java:158)\n"
				+ "at java.net.Socket.connect(Socket.java:452)\n"
				+ "at java.net.Socket.connect(Socket.java:402)\n"
				+ "at java.net.Socket.<init>(Socket.java:309)\n"
				+ "at java.net.Socket.<init>(Socket.java:211)\n"
				+ "at org.jnp.interfaces.TimedSocketFactory.createSocket(TimedSocketFactory.java:69)\n"
				+ "at org.jnp.interfaces.TimedSocketFactory.createSocket(TimedSocketFactory.java:62)\n"
				+ "at org.jnp.interfaces.NamingContext.getServer(NamingContext.java:224)";
		List<StackTraceItf> traces = (new JavaStackTraceParser()).parseAll(text);
		assertEquals(1, traces.size()); //, "le parser ne doit renvoyer qu'une stacktrace");
	}
}

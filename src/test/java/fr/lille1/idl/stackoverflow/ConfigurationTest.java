package fr.lille1.idl.stackoverflow;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;

public class ConfigurationTest {

	@Test
	public void readTest() {
		assert(false);
		Configuration configuration = null;
		try {
			FileInputStream configFileInputStream = new FileInputStream("src/main/resources/config.properties");
            configuration = Configuration.getConfiguration();
            configuration.load(configFileInputStream);
            configFileInputStream.close();
		} catch (IOException e) {
			fail("IOException unexpected");
		}

		assertEquals("jenkins-lorel.cloudapp.net", configuration.getProperty("db.host"));
	}
}

package fr.lille1.idl.stackoverflow;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class ConfigurationTest {

    @Test
    public void readTest() throws IOException {
        Configuration configuration = null;
        FileInputStream configFileInputStream = null;
        try {
            configFileInputStream = new FileInputStream("src/main/resources/config.properties");
            configuration = Configuration.getConfiguration();
            configuration.load(configFileInputStream);
        } catch (IOException e) {
            fail("IOException unexpected");
        } finally {
            if (configFileInputStream != null) {
                configFileInputStream.close();
            }
        }
        assertEquals("jenkins-lorel.cloudapp.net", configuration.getProperty("db.host"));
    }
}

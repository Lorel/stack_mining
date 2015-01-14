package fr.lille1.idl.stackoverflow;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by dorian on 22/11/14.
 */
public class Configuration extends Properties {

	private static final long serialVersionUID = -8023822143720181433L;
	private static Configuration configuration;

    public static Configuration getConfiguration() {
        if (configuration == null) {
            configuration = new Configuration();
        }
        return configuration;
    }

    public Configuration read(final String filename) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            this.load(fis);
        } catch (Exception e) {

        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return this;
    }
}

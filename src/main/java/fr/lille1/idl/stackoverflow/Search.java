package fr.lille1.idl.stackoverflow;

import fr.lille1.idl.stackoverflow.parsers.StackTraceItf;
import fr.lille1.idl.stackoverflow.parsers.StackTraceParserItf;
import fr.lille1.idl.stackoverflow.parsers.java.JavaStackTraceParser;
import fr.lille1.idl.stackoverflow.persistence.PostDatabase;
import fr.lille1.idl.stackoverflow.tables.Post;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.*;

/**
 * Created by dorian on 01/12/14.
 */
public class Search {
    public static final String USAGE = "Usage : java -jar search-jar-with-dependencies.jar path/to/text.file";

    public static void main(String[] args) throws IOException, StackTraceParserItf.ParseException, SQLException, ClassNotFoundException {
        if (args.length != 1) {
            System.out.println(USAGE);
            System.exit(0);
        }
        Logger logger = Logger.getGlobal();
        logger.setUseParentHandlers(false);
        SimpleFormatter formatter = new SimpleFormatter();
        FileHandler fh = new FileHandler("search.log", true);
        fh.setFormatter(formatter);
        logger.addHandler(fh);
        String filename = args[0];
        long beginning = System.nanoTime();
        FileInputStream configFileInputStream = null;
        try {
            configFileInputStream = new FileInputStream("config.properties");
            Configuration configuration = Configuration.getConfiguration();
            configuration.load(configFileInputStream);
            configFileInputStream.close();
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Could not find config file config.properties", e);
            System.exit(1);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            System.exit(2);
        } finally {
            if (configFileInputStream != null) {
                configFileInputStream.close();
            }
        }
        String text = new String(Files.readAllBytes(Paths.get(filename)));
        List<StackTraceItf> stackTraces = (new JavaStackTraceParser()).parseAll(text);
        if (!stackTraces.isEmpty()) {
            for (StackTraceItf stackTrace : stackTraces) {
                System.out.println(stackTrace);
                List<Post> posts = PostDatabase.getInstance().find(stackTrace);
                System.out.println("posts : " + posts.size());
                for (Post post : posts) {
                    String message = "http://stackoverflow.com/questions/" + post.getId() + "\t" + post.getTitle();
                    System.out.println(message);
                    logger.log(Level.INFO, message);
                }
            }
        } else {
            logger.log(Level.WARNING, "Did not find any stack trace in input file " + filename);
        }
        long ending = System.nanoTime();
        logger.log(Level.INFO, "Time spent : " + (ending - beginning) + " ns");
    }
}

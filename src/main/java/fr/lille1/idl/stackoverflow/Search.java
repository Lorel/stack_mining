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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.*;

/**
 * Created by dorian on 01/12/14.
 */
public class Search {
	
	private static Logger logger = Logger.getGlobal();
	
    public static final String USAGE = "Usage : java -jar search-jar-with-dependencies.jar path/to/text.file";
    
    public static Set<Post> getSimilarPosts (StackTraceParserItf parser, String stacktrace) throws ClassNotFoundException, SQLException {
    	Set<Post> similarPosts = new TreeSet<Post>();
    	List<StackTraceItf> stackTraces = parser.parseAll(stacktrace);
        if (!stackTraces.isEmpty()) {
            for (StackTraceItf stackTrace : stackTraces) {
                logger.info("Stacktrace : " + stackTrace.toString());
                List<Post> posts = PostDatabase.getInstance().find(stackTrace);
                similarPosts.addAll(posts);
            }
        }
        
        return similarPosts;
    }

    public static void main(String[] args) throws IOException, StackTraceParserItf.ParseException, SQLException, ClassNotFoundException {
        if (args.length != 1) {
            System.out.println(USAGE);
            System.exit(0);
        }

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
       	Set<Post> posts = getSimilarPosts(new JavaStackTraceParser(),text);
        if ( !posts.isEmpty() ) {
        	logger.info("posts : " + posts.size());
            for (Post post : posts) {
                String message = "http://stackoverflow.com/questions/" + post.getId() + "\t" + post.getTitle();
                logger.info(message);
            }
        } else {
            logger.log(Level.WARNING, "Did not find any stack trace in input file " + filename);
        }
        long ending = System.nanoTime();
        logger.log(Level.INFO, "Time spent : " + (ending - beginning) + " ns");
    }
}

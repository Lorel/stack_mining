stack_mining
============

Study project : data mining stackoverflow

This project is split in two parts:
 * A program that goes through a stack overflow dump and produces a XML file or extracts the data to put it into a MySQL database
 * A program that performs that receives a stack trace in a text file as input and performs search for an identical stack trace in the program and returns the URL to the post containing the stack trace.

## Requirements
This project was developped using the following technologies:
* java 7
* mysql-server 5.5

## Run the extraction
In order to run the project, you must have a valid configuration file at the root of the project such as the one in *src/main/resources/*.
If you don't, copy *src/main/resources/config.properties* at the root of the project and tweak it to reflect your installation needs.
```bash
mvn package
java -jar target/so-extractor-1.0-SNAPSHOT-jar-with-dependencies.jar path/to/Posts.xml
```


Execute sql file on cloud MySQL server
```bash
mysql -h jenkins-lorel.cloudapp.net -P 443 -p -u stackoverflow stackoverflow < file.sql
```

## Search for a stack trace
### Compiling the program
```bash
mvn -f pom_search.xml package
```

Just like the extractor, this program requires a valid configuration file.
If you don't have one copy the one from *src/main/resources/* at the root of the directory and tune it to your needs.

### Running the program
```bash
java -jar target/search-jar-with-dependencies /path/to/text.file
```

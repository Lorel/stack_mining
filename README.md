stack_mining
============

Study project : data mining stackoverflow

## Run the project
```bash
mvn package
java -jar target/so-extractor-1.0-SNAPSHOT-jar-with-dependencies.jar path/to/Posts.xml
```

execute sql
```bash
mysql -h jenkins-lorel.cloudapp.net -P 443 -p -u stackoverflow stackoverflow < file.sql
```
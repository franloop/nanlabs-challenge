# nanlabs-challenge

Set JAVA_HOME to point JDK 11 directory. 

Run mvnw.cmd in windows or ./mvnw on linux with the "clean package" options:  

- Windows : mvnw.cmd clean package
- Linux: ./mvnw clean package

Then start the application with:  

- Windows: %JAVA_HOME%/bin/java -jar ./target/task-management-api.jar
- Linux: $JAVA_HOME/bin/java -jar ./target/task-management-api.jar

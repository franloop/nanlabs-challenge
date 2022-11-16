# nanlabs-challenge

Set the environment variable "JAVA_HOME" pointing to JDK>=11. Example:
- Windows: set JAVA_HOME=c:/dev/lib/jdk11
- Linux: export JAVA_HOME=/opt/lib/openjdk-11

Run mvnw.cmd in windows or ./mvnw on linux with the "clean package" options:  

- Windows : mvnw.cmd clean package
- Linux: ./mvnw clean package

Set the environment variable "PROFILE" to default with
- Windows: set PROFILE=default
- Linux: export PROFILE=default

The application comes with predefined values for the Trello API such as API KEY, TOKEN and default Board TO-DO. 
If any of these values are need to be replaced do it with Enviroment Variables, replace the profile variable to env:

- Windows: set PROFILE=env
- Linux: export PROFILE=env

With this profile three more variables are need to be configured: TRELLO_API_KEY, TRELLO_API_TOKEN and TRELLO_API_BOARD
Default values are available on application-default.properties or .env file in the root of project.

Then start the application with:  

- Windows: %JAVA_HOME%/bin/java -jar ./target/task-management-api.jar --spring.profiles.active=%PROFILE%
- Linux: $JAVA_HOME/bin/java -jar ./target/task-management-api.jar --spring.profiles.active=$PROFILE


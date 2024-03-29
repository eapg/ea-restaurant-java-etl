# ea-restaurant-java-etl
`java etl`  is a microservice to implement an ETL system to help python ETL system with `order status histories` processing in the [ea_restaurant](https://github.com/eapg/EA_RESTAURANT) project, sharing information through gRPC protocol


# Project Setup

Download and install the following tools:

* [Intellij IDEA](https://www.jetbrains.com/es-es/idea/download/#section=windows)
* [JDK 17](https://jdk.java.net/java-se-ri/17)
* [Maven](https://maven.apache.org/)
* [Spring boot](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web/3.0.1)
* [checkstyle](https://maven.apache.org/plugins/maven-checkstyle-plugin/usage.html)
* [Google java format](https://github.com/google/google-java-format)

* In order to setup this project it's necessary to:

  * Validate that migrations ran in python side of the project to have BD and tables available.
    To go deeper into the migrations part refer to: [migrations](https://github.com/eapg/EA_RESTAURANT#migrations)
  * Set BD connexion parameters in `application.properties` (URL, Username, Password).
  * Set BD connexion parameters for testing in route test/resources `applicatin.properties` (URL, Username, Password).
  * Set oauth2.secret.key in `application.properties` as part of Oauth2 security.
  * Run maven command `mvn package` in order to set all dependencies in `POM` file.
  * Run maven command `mvn compile` to build `.java` from `.proto` files since this is a project that implements Grpc.
  * Before running the `springBootApp` to start the application it is necessary to set intellij to get the specific profile using
  command `spring.profiles.active=local` where `local` is the main profile to run the application, to set this command go to:
  `edit configurations` and put the command `spring.profiles.active=local` in the environment variables field. See image below.
  

  ![img.png](envConfig.png)

## Run java etl project

* To compile and run the spring boot application run the maven command `mvn spring-boot:run`
* To run checkstyle as linter run the maven command `mvn checkstyle::chek`
* To run the Google java format as formatter run the maven command `mvn fmt:format`                                                                                                                       
* To build `.java` files from `.proto` as part of gRPC implementation run the maven command `mvn compile`

## Pre-Commit Hooks

The pre commit hooks are actions that runs first , before you even commit your change, they are
typically use to inspect yours changes before committed. e.g. check code style, format the code, etc.

to set pre-commit Hooks:

* create a bash file named pre-commit and set all the hooks you want to run before commit in hooks folder.
* from main project directory move to hooks using `cd hooks`
* copy pre-commit file and paste it in follow directory `C:\Users\your_user\IdeaProjects\ea-restaurant-java-etl\.git\hooks` using
`cp pre-commit C:\Users\your_user\IdeaProjects\ea-restaurant-java-etl\.git\hooks`.

## INTELLIJ setup

* To avoid intellij to package imports instead of individual imports go to:
  `File` -> `Setting` -> `Code Style` -> `Java` -> `imports` and change `Class count to use import with *` 
   to 100 or a number that you know your project don't exceed. 
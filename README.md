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

## Run java etl project

* To compile and run the spring boot application run the maven command `mvn  spring-boot:run`
* To run checkstyle as linter run the maven command `mvn checkstyle::chek`
* To run the Google java format as formatter run the maven command `mvn process-sources`                                                                                                                       

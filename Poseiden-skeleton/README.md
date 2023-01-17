# SPRING-BOOT

## About The Project

Poseidon is Spring Boot web application
It is a web-deployed enterprise software that aims to generate more 
trades for institutional investors buying and selling fixed income securities.

---------
## Technical:
1. Framework: Spring Boot v2.6.4
2. Java 11
3. Thymeleaf
4. Bootstrap v.4.3.1

---------

## Getting Started

# Steps for Windows 10 to config your DataBase Username and Password
1. Click on the Start Menu and search for “environment variables.”
2. Click on the environment variable end you create new environment
   variable
   1. Now enter the Variable Name: spring.datasource.username and Value:username and its Value and press the OK button.
   2. Now enter the Variable Name: spring.datasource.password and Value:database password and its Value and press the OK button.

### Running App

To run the application, go to folder `Poseidon`

1. Set up the tables and data in the database.

   1. Create database with script present in`doc/data.sql`
   2. as configuration in application.properties

2. Compile and generate the final jar by running command line: `mvn clean package`

After for start application digit

Command line : `mvn spring-boot:run`

1. To access the application, open your favorite browser and go to address: http://localhost:8080

### Testing

- To run the tests execute the command: `mvn verify`
- To generate the project's reports site, please run :`mvn site`
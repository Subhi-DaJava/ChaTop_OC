# ChâTop Backend API
## Description
The ChâTop Backend API is the server for the ChâTop application. It is developed in `Spring Boot 3` using `Java 17` and leverages `Spring-doc OpenAPI and Sawagger UI` for Documentation.

## Features 
The ChâTop API provides the following major features: 
- User Authentication with JWT (utilizing io.jsonwebtoken library)
- User registration
- Rental creation
- Rental update
- Rental display
- Sending Message
  
### Installation
Before running the ChâTop Backend API, please follow these installation steps :
1. Clone this repository from here [(Link Repo)](https://github.com/Subhi-DaJava/ChaTop_OC.git).
2. Ensure that you have `Java Development Kit` (JDK) 17 installed on your local machine.
3. Make sure you have `Maven` installed locally.
4. Ensure that `MySQL` is installed on your local machine and create a `MySQL database` for the application. You can use the provided [script](https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring/blob/main/ressources/sql/script.sql) to set up tables in your database.
5. Configure the necessary environment variables either in your system or within your IDE before running the application. <br/>

### Required environment variables
  * `mysql_database_name`: The name of your `MySQL database`.
  * `mysql_username`: Your `MySQL username`.
  * `mysql_user_password`: Your `MySQL password`.
  * `IMAGE_STORAGE_PATH`: The local path where the application should store `Rental` images.
  * `IMAGE_URL`: The URL path for accessing the stored `Rental`' images.
  * `JWT_SECRET_KEY`: A secret key for generating `JSON Web Token` (JWT) signature (Ensure it is at least 32 characters long for security reason).

### Running The Application
#### There are three ways to run the ChâTop API:

  1. Run the application using a `JAR` file
     * Generate a `JAR` in the root folder of the project by executing: <br/>
       - `mvn clean install -Dmysql_database_name=${mysql_database_name} -Dmysql_username=${mysql_username} -Dmysql_user_password=${mysql_user_password} -DIMAGE_STORAGE_PATH=${IMAGE_STORAGE_PATH} -DIMAGE_URL=${IMAGE_URL} -DJWT_SECRET_KEY=${JWT_SECRET_KEY}`
     * Run the `JAR` file:
       - `java -jar target/chatop-0.0.1-SNAPSHOT.jar`(in the root folder of the project) or run `java -jar chatop-0.0.1-SNAPSHOT.jar` in the folder target
     * Access the API at [http://localhost:3001](http://localhost:3001) (test with the `Postman` or with the Frontend app [ChâTop Angular App](https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring))
  2. Run the application using `Maven` command : <br/>
     * Execute `mvn spring-boot:run`
     * Access the API at [http://localhost:3001](http://localhost:3001) (test with the `Postman` or with the frontend app [ChâTop Angular App](https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring))
  3. Run the application within your IDE
     * Import the project into your IDE
     * Set the environment variables within your IDE's `Run/Debug Configurations`(e.g., IntelliJ)
     * Run the application
     * Access the API at [http://localhost:3001](http://localhost:3001) (test with the Postman or with the frontend app [ChâTop Angular App](https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring))
  
  ### Spring-doc OpenAPI-Swagger Documentation
  + Access the API documentation by following the Url: [Swagger-UI](http://localhost:3001/swagger-ui/index.html#)
  + Test the endpoints and generate JWT token for authorization (except two endpoints)
  + Download the `api-docs` in YAML format from this link: [download api-doc](http://localhost:3001/v3/api-docs.yaml)
  + `Swagger UI` documentation is automatically generated from annotations and information defined in the `API controller classes`. `OpenAPI documentation` is based on  the `OpenAPI30Configuration` general configuration class.

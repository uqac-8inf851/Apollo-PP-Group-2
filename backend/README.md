# Backend using Java Spring Boot

If you are testing the backend you might want to build and run locally the application.

Use the following command to build the application using Maven

```
./mvnw -Dmaven.test.failure.ignore=true clean package
```

After the building the binaries will be on *target* folder.

For a quick running, you can use the command below to build and run the backend application using Maven

```
./mvnw spring-boot:run
```

Consider to install the [Spring Boot CLI](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-cli.html) to help you manage the development lifecycle.


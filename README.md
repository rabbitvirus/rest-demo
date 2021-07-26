# rest-demo

Simple demo app that returns some user data from GitHUB through simple REST API.

By default, the app runs on the 8080 port. You can use `server.port` property to override it.

The easiest way to start the app is to change directory to project's root and then run: ```mvn spring-boot:run```.

For simple tests, once you start the application, you can use OpenAPI:
[localhost:8080/swagger-ui.html](localhost:8080/swagger-ui.html)

Also, H2 console is enable: [localhost:8080/h2](localhost:8080/h2) (*important: use `jdbc:h2:mem:maindb`*).
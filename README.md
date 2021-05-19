## About the Game
* Game Of Three is played between two users. If there is **only one connected** player to the game, **game could not start**.
* "Waiting for a player" **alert** is sent in this situation.
* Game could be played **manually** or **automatic**.
* **Random number (defaut range is 100)** is generated **by the system** and the player who starts the game sends this random number to the second player.
* Manual Play Type: You can **only choose -1,0,1**.
* If a **wrong adjustment** is chosen, **alert** would be sent by the system.
* The player who reaches the 1 will win the game.
* If you disconnect from the game, starting connect screen will reappear.
* To continue play please refresh the page, disconnect button requires front-end development.

## About the Technical Details
* The microservice runs with **Java 1.8** and it was developed with spring boot project.
* If one user connects to the game, the user sent a **POST** call is sent to create or to join/update to the game. (**spring-webmvc**)
* To establish a game channel **webSocket** communication protocol is used for a bi-directional, full-duplex, persistent connection between a web browser and a server. 
* For webSocket message handling **STOMP** implementation is user, backed by simple in-memory message broker is used. (**spring-boot-starter-websocket**)
* Generated gameId is used to set a specialized channel between two player.
* At frontend **jQuery, Bootstrap, SockJS** and **Stomp** were used.
* Google Chrome is used as a default browser.
* For object-relational mapping **Spring Data JPA** is used.
* **H2 in memory db** is used to persist the game data. 
* Only **one repository** "game repository" is used.
* In each **restart** database will **drop and create**.
* **Swagger** is used for **REST API** documentation  
* **JUnit and Mockito** framework is used in writing and running your unit tests.
* **Test Coverage : 95.5 %**
* **SonarQube analyze** was done with SonarLint Eclipse plugin.
* Spring DevTools was used during development.

### GitHub  
  The application can be downloaded to local repository by the following command after configuring SSH settings : 
  
  ```
  git clone git@github.com:OzlemDoganGitPortfolio/gameOfThreeApp.git
  ```
### To execute the game
To run the application and play the game, issue the following command from the project home directory:
  
```
$ java -jar gameofthree-0.0.1-SNAPSHOT.jar  
```
```
$ ./mvnw spring-boot:run 
```
* Then, access the following url, from two different browser session windows/tabs:

```
http://localhost:8885
```
* If you want to check the database: (**username: sa**, **password:password**)

```
http://localhost:8885/h2-console/
```
* API documentation

```
http://localhost:8885/swagger-ui.html
```
### Database Configurations - Embedded H2 database
```
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.h2.console.settings.trace=false
spring.h2.console.settings.web-allow-others=false
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.hbm2ddl.auto=create-drop
```

**Note: Snapshots of the application were added to the image folder. **


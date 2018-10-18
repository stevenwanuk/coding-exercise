## coding-exercise

### assumptions

- User hasn't been already registered if user name has not been used.
- "Post /users/register" api is created due to requirements, even it should be replaced by "Post /users"     


### TODO

- Currently, this app returns "User" Entity directly. It should use a ApiResponse instead.
- acceptance-test need to be done
- complete all unit tests 
- GDPR compliance

### database
 
H2 database is using in this demo

#### init db
For quick simulation, import.sql is used to initial database. Could be improved by using flyway . 

	sql file: /src/main/resources/import.sql


The following user is loaded into database already.

	INSERT INTO User (username, password, date_of_birth, ssn) VALUES  ('usera', 'pwD1', now(), 'test');


### tests


- Unit tests are under folder /src/test/

- Integration tests are under folder /src/integreation-test

- Acceptance tests are under folder /src/acceptance-test (not completed yet)


### compile & run

>  gradle clean build integrationTest bootRun

>  http://localhost:8080


### deployment

Dockerfile & Jenkinsfile are not included here for simplify implementation.

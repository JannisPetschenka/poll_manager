# Assignment 1

## Implementation

### Implementation of the [Model-Classes](./src/main/java/no/hvl/poll_manager/model/)
The biggest difficulty with the implementation of model classes was the cyclical definition of the classes and the lack of an id.

- The issue with the cyclical definition was mainly understanding `@JsonIdentityInfo` and `@JsonIdentityReference` since I have never worked with them.
- In the UML diagram it is not implicitly specified that the objects should also contain an id field. However this makes implementing methods like `DELETE` and `UPDATE` difficult. Also referencing a User when creating a Poll would also require using the username, which is more complicated in comparison to using an id. To solve this an id field was added to the `User.java` and `Poll.java` which was also used for the `@JsonIdentityInfo` and `@JsonIdentityReference` annotation. 

### Implementation of the [Controllers](./src/main/java/no/hvl/poll_manager/controller/)

- Straight forward implementation of the `GET`, `POST`, `PUT`, `DELETE` endpoints.
- In creating the `POST` and `PUT` mappings for the `/api/v1/vote`, `/api/v1/user` and `/api/v1/poll` endpoints `VoteRequest`, `UserRequest` and `PollRequest` records were created to define which arguments are needed for the specific requests.

### Implementation of the [PollManager](./src/main/java/no/hvl/poll_manager/repository/PollManager.java)

- Storing users and poll as `List`
- Storing the votes in a `HashMap<Poll, List<Vote>>`
    - Reasoning behind this is, that the UML diagram did not define a connection between a Poll and a Vote. Which would make it impossible to tell which Vote belongs to which Poll.
    - It would be better to fix this issue by adding cyclical definition between a Poll (list/set of votes) and Votes (one poll).
- The PollManager also checks that when creating a User the User does not already exist.

### Implementation of the [Tests](./src/test/java/no/hvl/poll_manager/integration/)

- Implementation via integration tests in Java using `TestRestTemplate`
    - With this requests to the different endpoints via `postForEntity` etc. can be send and the results verified.
    - For this a [ScenarioBaseTest](./src/test/java/no/hvl/poll_manager/integration/ScenarioBaseTest.java) class was created which offers standardized methods to test certain endpoints. This enables writing test-scenarios quicker and reuse already defined test-steps.
    - The biggest issue with creating the tests was the inability to map the responses to the corresponding classes. When trying to map the response of the `GET /api/v1/users` endpoint to the `User` class there would always be an error message stating that the mapping to the `User.java` class failed.
        - In the end the error was avoided by mapping to `String.class` and verifying that the String contains a specific substring.
        - However this solution is not ideal and is an issue to be fixed in the future.
            - Possible fix would be using a library like Jackson to parse the response into a JSON object to verify it.

## Future issues

- Validation:
    - up onto this point in the code there is no validation. So even though by definition a Poll needs at least two VoteOptions there is at the moment only a rudimentary check in the [PollManager.java](./src/main/java/no/hvl/poll_manager/repository/PollManager.java) it would be better to have such a validation in the [Poll.java](./src/main/java/no/hvl/poll_manager/model/Poll.java) and other Model-Classes. So that a HTTP 400 would be returned.

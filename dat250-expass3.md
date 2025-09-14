# Assignment 3

## Implementation

First step of the Implementation was splitting the project into a frontend and backend subfolder. 
And setting up gradle so that both of these projects can be build and integrated using a gradle
task. This was achieved by building the frontend project into a static site and copying that into
the `poll_manager/backend/src/main/resources/static/` folder so that the frontend will be hosted by
the backend application. This simplifies the deployment of the project, since only one Docker image
is needed and no additional configuration to avoid CORS errors is needed. The gradle task for the
frontend project is defined [here](./frontend/build.gradle.kts)

### Frontend

The main functionalities in the frontend project are divided into the store and the view-components.

#### Implementation of the [Store](./frontend/src/lib/store.js)

The store encapsulates the calls to the backend API and the corresponding objects. By doing so the
user (which is used for multiple requests to the backend) can easily be accessed by all other
functions. 

Since for development and deployment the frontend is hosted by the Spring-Boot backend the calls to
the Poll-API can be defined with the relative path `fetch("/api/v1/users")`.

#### Implementation of the [Components](./frontend/src/lib/)

There are three components in the frontend application.
- [Users](./frontend/src/lib/Users.svelte): A dropdown component, with which a User can be selected.
    - Since up until now there is no sort of login the user can be freely picked from the list of
      all available users created.
- [Poll](./frontend/src/lib/Poll.svelte): A component with input fields to create a new poll.
    - Input fields for question, validUntil and a resizable list of voteOptions. The creatorId
      needed for the request is taken from the currently selected user.
- [Polls](./frontend/src/lib/Polls.svelte): A list of all created polls.
    - Displays the question and voteOptions. For the voteOptions a button to vote on said specific
      option in the name of the currently selected user is displayed.
    - Additionally the current standing of the votes is displayed. This includes the caption of each
      vote option and the number of votes for said vote option.
    - If the currently selected user is the same as the creator of the poll a `Delete Poll` button
      will be displayed.

### Backend

There was one small change done to the backend for the frontend implementation.

Previously the Polls did not contain any information regarding how many votes were given for said
specific poll. To display this in the frontend an additional request to the `GET /api/v1/votes/{pollId}`
Endpoint would be necessary with iterating over the votes to get a `voteOption &rarr; number of
Votes` relation. Since this is a basic functionality, that should be placed in the backend code the
[Poll.java](./backend/src/main/java/no/hvl/poll_manager/model/Poll.java) model was extended to
contain a `Set<String, Integer>` which will be filled from the `Map<Integer, List<Vote>> votes` on
every request to the `GET /api/v1/polls` endpoint.

## Future issues

- Proper user management
    - Currently no login/authentication is done. The user can simply select a chosen account from
      the dropdown.
- Creating new Users.
- Deleting and modifying Users, Polls and Votes.

# Assignment 7

Basis for the docker image: Assignment 5 Redis

## Implementation

A crude implementation of a docker image was already done in Assignment 1/2.
However this image was not optimal, since it did neither contain a build stage 
nor had any user-control. 

So the main modifications to the [Dockerfile](./Dockerfile) were:
- Add build step `FROM gradle:9-jdk21 AS builder`. In this step the frontend
  and backend application are build.
- Add user to the "running" stage.
```Dockerfile
ARG user=poll_user
ARG group=poll_group
ARG uid=1000
ARG gid=1000
# create usergroup
RUN groupadd -g ${gid} ${group}
# create user (the -m flag creates the folder /home/${user}
RUN useradd -u ${uid} -g ${group} -s /bin/sh -m ${user}

# Switch to user
USER ${uid}:${gid}
```
- Additionally to the jar also copy the `db.mv.db` and `db.trace.db` file to
  have some default values in the DB inside the docker container.

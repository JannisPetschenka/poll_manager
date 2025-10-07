FROM gradle:9-jdk21 AS builder
WORKDIR /home/builder

COPY . .
RUN gradle copyWebApp
RUN gradle bootJar

FROM openjdk:21-jdk-slim

ARG user=poll_user
ARG group=poll_group
ARG uid=1000
ARG gid=1000
RUN groupadd -g ${gid} ${group}
RUN useradd -u ${uid} -g ${group} -s /bin/sh -m ${user}

USER ${uid}:${gid}

WORKDIR /home/${user}

COPY --from=builder --chown=${uid}:${gid} /home/builder/backend/build/libs/backend-0.0.1-SNAPSHOT.jar app.jar 

COPY --from=builder --chown=${uid}:${gid} /home/builder/backend/db.mv.db . 
COPY --from=builder --chown=${uid}:${gid} /home/builder/backend/db.trace.db . 

CMD ["java", "-jar", "app.jar"]

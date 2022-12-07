FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle -Pvaadin.productionMode=true bootJar --no-daemon

FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/docs-storage.jar /app/docs-storage.jar
ENTRYPOINT ["java","-jar","/app/docs-storage.jar"]
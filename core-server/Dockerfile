# the first stage of our build will use a maven 3.6.1 parent image
FROM maven:3.6.1-jdk-8-alpine AS MAVEN_BUILD
 
# copy the pom and src code to the container
COPY ./ ./
 
# package our application code
RUN mvn clean package

# Docker Agent
FROM openjdk:8-jdk-alpine
# Add user & group for spring
RUN addgroup -S spring && adduser -S spring -G spring
# Swtich to spring user
USER spring:spring
# Copy *.jar file in current path
COPY --from=MAVEN_BUILD /target/*.jar app.jar
# Run this app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
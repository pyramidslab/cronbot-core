# Docker Agent
FROM openjdk:8-jdk-alpine
# Add user & group for spring
RUN addgroup -S spring && adduser -S spring -G spring
# Swtich to spring user
USER spring:spring
# Copy *.jar file in current path
COPY target/core-server.jar app.jar
#Expose port 8080
EXPOSE 8080
# Run this app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
# Docker Agent
FROM openjdk:8-jdk-alpine
# Add user & group for spring
RUN addgroup -S spring && adduser -S spring -G spring
# Copy *.jar file in current path
COPY target/core-server.jar app.jar
# Copy entrypoint bash
COPY src/main/docker/entrypoint.sh entrypoint.sh
# Make dir for config
RUN mkdir /config
# Allow config dir for volume bind
VOLUME [ "/config" ]
# Make entrypoint executable
RUN chmod 777 /entrypoint.sh
RUN chmod +x /entrypoint.sh
# Copy application-prod.yml
COPY src/main/resources/application-prod.yml /config/application.yml
# Swtich to spring user
USER spring:spring
# Expose port 8080
EXPOSE 8080
# Run this app.jar
ENTRYPOINT ["./entrypoint.sh"]
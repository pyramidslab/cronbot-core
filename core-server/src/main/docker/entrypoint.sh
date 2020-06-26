#!/bin/sh
java -jar /app.jar --spring.profiles.active=prod --spring.config.location=/config/application.yml

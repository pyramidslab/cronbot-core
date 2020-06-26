#!/bin/sh
spring_config_location=""
if [[ -z "${CONFIGURATION_FILE}" ]]; then
    spring_config_location=""
else
    spring_config_location="--spring.config.location=${CONFIGURATION_FILE}"
fi

echo "${spring_config_location}"
java -jar /app.jar --spring.profiles.active=prod ${spring_config_location}

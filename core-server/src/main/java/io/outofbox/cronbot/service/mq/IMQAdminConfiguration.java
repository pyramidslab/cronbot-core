package io.outofbox.cronbot.service.mq;

import io.outofbox.cronbot.model.MQConfiguration;

import java.util.Optional;

public interface IMQAdminConfiguration {
    Optional<MQConfiguration> createMQConfig(String vHost);
}

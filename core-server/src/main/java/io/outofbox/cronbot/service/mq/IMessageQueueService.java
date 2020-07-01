package io.outofbox.cronbot.service.mq;

import io.outofbox.cronbot.error.MQException;

public interface IMessageQueueService {
    /**
     * Send an object after convert it to json
     * @param obj Object to be sent
     * @throws MQException Throws an exception if something went wrong
     */
    <T> void send(T obj) throws MQException;
}

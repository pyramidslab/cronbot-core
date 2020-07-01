package io.outofbox.cronbot.error;

public class MQException extends Throwable{
    public MQException() {super("An error occurred in message queue");}
    public MQException(String msg) {
        super(msg);
    }
}

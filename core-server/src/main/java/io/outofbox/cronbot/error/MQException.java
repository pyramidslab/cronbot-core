package io.outofbox.cronbot.error;

public class MQException extends Throwable {
    private static final String MESSAGE = "An error occurred in message queue";

    public MQException() {
        super(MESSAGE);
    }

    public MQException(String msg) {
        super(msg);
    }

    public MQException(String msg, Throwable t) {
        super(msg, t);
    }

    public MQException(Throwable t) {
        super(MESSAGE, t);
    }
}

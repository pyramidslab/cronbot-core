package io.outofbox.cronbot.error;

public class OperationFailureException extends Throwable {
    public OperationFailureException(String msg) {
        super(msg);
    }

    public OperationFailureException(String msg, Throwable t) {
        super(msg, t);
    }
}

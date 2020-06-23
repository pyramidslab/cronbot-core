package io.outofbox.cronbot.error;

public class NotFoundException extends Throwable {
    public NotFoundException(String msg) {
        super(msg);
    }
}

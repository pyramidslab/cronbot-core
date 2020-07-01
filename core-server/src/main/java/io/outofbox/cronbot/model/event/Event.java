package io.outofbox.cronbot.model.event;


import lombok.*;

@AllArgsConstructor
public class Event<T> {
    @NonNull
    private EventType event;
    private String _class;
    @NonNull
    private T object;
}

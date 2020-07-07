package io.outofbox.cronbot.model.event;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class Event<T> {
    @NonNull
    private EventType event;
    private String _class;
    @NonNull
    private T object;
}

package io.outofbox.cronbot.model.event;

public enum EventType{
    CREATE("create"), UPDATE("update"), DELETE("delete");
    private String name;
    EventType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
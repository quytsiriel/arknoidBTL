package com.uet.arkanoid;

public class PauseAction {
    public enum Type {
        RESUME,
        VOLUME,
        QUIT
    }

    private Type type;

    public PauseAction(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}

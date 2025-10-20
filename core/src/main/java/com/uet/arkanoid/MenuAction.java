package com.uet.arkanoid;

public class MenuAction {
    public enum Type {
        START_1_PLAYER,
        START_2_PLAYER,
        CHANGE_DIFFICULTY,
        EXIT
    }

    private Type type;
    private Difficulty difficulty;

    public MenuAction(Type type, Difficulty difficulty) {
        this.type = type;
        this.difficulty = difficulty;
    }

    public Type getType() {
        return type;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}

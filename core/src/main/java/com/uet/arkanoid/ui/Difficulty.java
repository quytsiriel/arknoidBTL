package com.uet.arkanoid.ui;

public enum Difficulty {
    EASY(""),
    NORMAL(""),
    HARD("");

    private final String displayName;

    Difficulty(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

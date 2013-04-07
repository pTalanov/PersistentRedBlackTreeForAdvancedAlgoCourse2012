package ru.spbau.talanov.advalgo.impl;

import org.jetbrains.annotations.NotNull;

/**
 * @author Pavel Talanov
 */
public enum Direction {
    LEFT,
    RIGHT;

    @NotNull
    public Direction opposite() {
        if (this == Direction.LEFT) {
            return Direction.RIGHT;
        } else {
            return Direction.LEFT;
        }
    }
}

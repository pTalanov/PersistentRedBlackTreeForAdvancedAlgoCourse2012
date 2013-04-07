package ru.spbau.talanov.advalgo.impl;

import org.jetbrains.annotations.NotNull;

/**
 * @author Pavel Talanov
 */
public enum Color {
    RED,
    BLACK;

    @NotNull
    public Color other() {
        if (this == RED) {
            return BLACK;
        } else {
            return RED;
        }
    }
}

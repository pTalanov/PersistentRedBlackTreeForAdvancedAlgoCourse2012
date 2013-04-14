package ru.spbau.talanov.advalgo.impl;

import org.jetbrains.annotations.NotNull;

/**
* @author Pavel Talanov
*/
public interface Mutator<E> {
    @NotNull
    Node<E> mutate(@NotNull Node<E> node);
}

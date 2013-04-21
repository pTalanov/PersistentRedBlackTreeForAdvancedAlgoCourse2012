package ru.spbau.talanov.advalgo.problem.comparison;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.talanov.advalgo.api.PersistentNavigableSet;

import java.util.TreeSet;

/**
 * @author Pavel Talanov
 */
public final class TreeSetBasedPersistentSet<E extends Comparable<? super E>> implements PersistentNavigableSet<E, TreeSetBasedPersistentSet<E>> {

    @NotNull
    private final TreeSet<E> set;

    private TreeSetBasedPersistentSet(@NotNull TreeSet<E> set) {
        this.set = set;
    }

    @Override
    public boolean contains(@NotNull E element) {
        return set.contains(element);
    }

    @Nullable
    @Override
    public TreeSetBasedPersistentSet<E> add(@NotNull E element) {
        if (set.contains(element)) {
            return null;
        }
        TreeSet<E> newSet = new TreeSet<E>(set);
        newSet.add(element);
        return new TreeSetBasedPersistentSet<E>(newSet);
    }

    @Nullable
    @Override
    public TreeSetBasedPersistentSet<E> remove(@NotNull E element) {
        if (!set.contains(element)) {
            return null;
        }
        TreeSet<E> newSet = new TreeSet<E>(set);
        newSet.remove(element);
        return new TreeSetBasedPersistentSet<E>(newSet);
    }

    @Nullable
    @Override
    public E ceiling(@NotNull E element) {
        return set.ceiling(element);
    }

    @Nullable
    @Override
    public E floor(@NotNull E element) {
        return set.floor(element);
    }

    @Nullable
    @Override
    public E higher(@NotNull E element) {
        return set.higher(element);
    }

    @Nullable
    @Override
    public E lower(@NotNull E element) {
        return set.lower(element);
    }

    @NotNull
    public static <E extends Comparable<? super E>> TreeSetBasedPersistentSet<E> empty() {
        return new TreeSetBasedPersistentSet<E>(new TreeSet<E>());
    }

    @Override
    public String toString() {
        return set.toString();
    }
}

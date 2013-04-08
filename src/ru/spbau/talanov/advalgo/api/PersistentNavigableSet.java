package ru.spbau.talanov.advalgo.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Pavel Talanov
 */
public interface PersistentNavigableSet<E extends Comparable<? super E>, S extends PersistentNavigableSet<E, S>> {
    /**
     * Returns <tt>true</tt> if this collection contains the specified element.
     *
     * @param element element whose presence in this collection is to be tested
     * @return <tt>true</tt> if this set contains the specified
     *         element
     */
    boolean contains(@NotNull E element);

    /**
     * Returns an instance of S containing the element
     * and retaining all the elements that are contained in this instance.
     *
     * @param element element to be added
     * @return collection containing the element, {@code null}, if element is already in the set
     */
    @Nullable
    S add(@NotNull E element);

    /**
     * Returns an instance of S without the element
     * and retaining all other elements that are contained in this instance.
     *
     * @param element element to be removed
     * @return collection without the element, {@code null}, if the is no such element
     */
    @Nullable
    S remove(@NotNull E element);

    /**
     * Returns the least element in this set greater than or equal to
     * the given element, or {@code null} if there is no such element.
     *
     * @param element the value to match
     * @return the least element greater than or equal to {@code element},
     *         or {@code null} if there is no such element
     */
    @Nullable
    E ceiling(@NotNull E element);

    /**
     * Returns the greatest element in this set less than or equal to
     * the given element, or {@code null} if there is no such element.
     *
     * @param element the value to match
     * @return the greatest element less than or equal to {@code element},
     *         or {@code null} if there is no such element
     */
    @Nullable
    E floor(@NotNull E element);

    /**
     * Returns the least element in this set strictly greater than the
     * given element, or {@code null} if there is no such element.
     *
     * @param element the value to match
     * @return the least element greater than {@code element},
     *         or {@code null} if there is no such element
     */
    @Nullable
    E higher(@NotNull E element);

    /**
     * Returns the greatest element in this set strictly less than the
     * given element, or {@code null} if there is no such element.
     *
     * @param element the value to match
     * @return the greatest element less than {@code element},
     *         or {@code null} if there is no such element
     */
    @Nullable
    E lower(@NotNull E element);
}


/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Parameter;
import jakarta.persistence.TemporalType;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A {@link SearchStream} that is guaranteed to return at most a single result.
 */
public interface SearchValue<X, S extends Selection<X>> extends SearchStream<X, S> {

    /**
     * Build and evaluate a JPA query based on this instance and return the single result, if any.
     *
     * @return result of executed query
     * @throws NoResultException if there is no result
     * @throws NonUniqueResultException if there is more than one result
     */
    default X value() {
        return this.toQuery().getSingleResult();
    }

    /**
     * Build and evaluate a JPA query based on this instance and return the single result, if any,
     * otherwise the given value.
     *
     * @param defaultValue value to return if there is no match
     * @return result of executed query, or {@code defaultValue} if not found
     * @throws NonUniqueResultException if there is more than one result
     */
    default X orElse(X defaultValue) {
        try {
            return this.value();
        } catch (NoResultException e) {
            return defaultValue;
        }
    }

    /**
     * Build and evaluate a JPA query based on this instance and return the single result, if any,
     * otherwise throw an exception provided by the given {@link Supplier}.
     *
     * @param supplier creator of exception
     * @param <T> exception type
     * @return result of executed query
     * @throws T if there is no result
     * @throws NonUniqueResultException if there is more than one result
     */
    default <T extends Throwable> X orElseThrow(Supplier<? extends T> supplier) throws T {
        if (supplier == null)
            throw new IllegalArgumentException("null supplier");
        try {
            return this.value();
        } catch (NoResultException e) {
            throw supplier.get();
        }
    }

    /**
     * Build and evaluate a JPA query based on this instance and return the single result, if any,
     * otherwise the value from the given {@link Supplier}.
     *
     * @param supplier creator of exception
     * @return result of executed query
     * @throws NonUniqueResultException if there is more than one result
     * @throws IllegalArgumentException if {@code supplier} is null
     */
    default X orElseGet(Supplier<? extends X> supplier) {
        if (supplier == null)
            throw new IllegalArgumentException("null supplier");
        try {
            return this.value();
        } catch (NoResultException e) {
            return supplier.get();
        }
    }

    /**
     * Build and evaluate a JPA query based on this instance and give the returned value, if any, to the given {@link Consumer}.
     *
     * @param consumer receives value returned by query, if any
     * @throws IllegalArgumentException if {@code consumer} is null
     * @throws NonUniqueResultException if there is more than one result
     */
    default void ifPresent(Consumer<? super X> consumer) {
        if (consumer == null)
            throw new IllegalArgumentException("null consumer");
        final X value;
        try {
            value = this.value();
        } catch (NoResultException e) {
            return;
        }
        consumer.accept(value);
    }

    /**
     * Build and evaluate a JPA query based on this instance and return true if a result is returned, otherwise false.
     *
     * @return true if executed query returns a result, false otherwise
     * @throws NonUniqueResultException if there is more than one result
     */
    default boolean isPresent() {
        try {
            this.value();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    /**
     * Build and evaluate a JPA query based on this instance and return the single result, if any, as an {@link Optional}.
     *
     * <p>
     * <b>Note:</b> due to limitations of the {@link Optional} class, this method does not support returning null values;
     * if the query returns a null value, an exception is thrown.
     *
     * @return the optional result of the executed query
     * @throws NonUniqueResultException if there is more than one result
     * @throws IllegalArgumentException if this query returns a null value
     */
    default Optional<X> toOptional() {
        final X value;
        try {
            value = this.value();
        } catch (NoResultException e) {
            return Optional.empty();
        }
        if (value == null)
            throw new IllegalArgumentException("null values cannot be represented in Optional");
        return Optional.of(value);
    }

// Narrowing overrides (SearchStream)

    @Override
    default <Y> SearchValue<Y, Selection<Y>> mapToSelection(Class<Y> type,
      Function<? super S, ? extends Selection<Y>> selectionFunction) {
        return ((SearchStreamImpl<Y, Selection<Y>>)SearchStream.super.mapToSelection(type, selectionFunction)).toValue();
    }

// Narrowing overrides (QueryStream)

    @Override
    SearchValue<X, S> bind(Ref<X, ? super S> ref);

    @Override
    SearchValue<X, S> peek(Consumer<? super S> peeker);

    @Override
    <X2, S2 extends Selection<X2>> SearchValue<X, S> bind(Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction);

    @Override
    SearchValue<X, S> filter(SingularAttribute<? super X, Boolean> attribute);

    @Override
    SearchValue<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder);

    @Override
    SearchValue<X, S> withFlushMode(FlushModeType flushMode);

    @Override
    SearchValue<X, S> withLockMode(LockModeType lockMode);

    @Override
    SearchValue<X, S> withHint(String name, Object value);

    @Override
    SearchValue<X, S> withHints(Map<String, Object> hints);

    @Override
    <T> SearchValue<X, S> withParam(Parameter<T> parameter, T value);

    @Override
    SearchValue<X, S> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType);

    @Override
    SearchValue<X, S> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType);

    @Override
    SearchValue<X, S> withParams(Iterable<? extends ParamBinding<?>> params);

    @Override
    SearchValue<X, S> withLoadGraph(String name);

    @Override
    SearchValue<X, S> withFetchGraph(String name);
}

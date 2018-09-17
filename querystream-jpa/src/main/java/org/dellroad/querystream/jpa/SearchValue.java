
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.NoResultException;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

/**
 * A {@link SearchStream} that is guaranteed to return at most a single result.
 */
public interface SearchValue<X, S extends Selection<X>> extends SearchStream<X, S> {

    /**
     * Build and evaluate a JPA query based on this instance and return the single result, if any.
     *
     * @return result of executed query
     * @throws NoResultException if there is no result
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
     */
    default X orElse(X defaultValue) {
        try {
            return this.value();
        } catch (NoResultException e) {
            return defaultValue;
        }
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
}

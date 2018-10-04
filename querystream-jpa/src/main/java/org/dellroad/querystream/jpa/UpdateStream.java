
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.UpdateType;

/**
 * Builder for JPA criteria bulk update queries using a {@link java.util.stream.Stream}-like API.
 */
public interface UpdateStream<X> extends QueryStream<X, Root<X>, CriteriaUpdate<X>, CriteriaUpdate<X>, Query> {

    /**
     * Build and execute a JPA query based on this instance.
     *
     * <p>
     * Ultimately delegates to {@link Query#executeUpdate} and can throw any exception thrown by that method.
     *
     * @return the number of entities updated
     */
    int update();

// Setters

    <Y> UpdateStream<X> set(Path<Y> attribute, Expression<? extends Y> value);

    <Y, V extends Y> UpdateStream<X> set(Path<Y> attribute, V value);

    <Y> UpdateStream<X> set(SingularAttribute<? super X, Y> attribute, Expression<? extends Y> value);

    <Y, V extends Y> UpdateStream<X> set(SingularAttribute<? super X, Y> attribute, V value);

// Narrowing overrides

    @Override
    UpdateType<X> getQueryType();

    @Override
    UpdateStream<X> bind(Ref<X, ? super Root<X>> ref);

    @Override
    UpdateStream<X> peek(Consumer<? super Root<X>> peeker);

    @Override
    UpdateStream<X> filter(SingularAttribute<? super X, Boolean> attribute);

    @Override
    UpdateStream<X> filter(Function<? super Root<X>, ? extends Expression<Boolean>> predicateBuilder);

    @Override
    UpdateStream<X> limit(int maxSize);

    @Override
    UpdateStream<X> skip(int num);

    @Override
    UpdateStream<X> withFlushMode(FlushModeType flushMode);

    @Override
    UpdateStream<X> withLockMode(LockModeType lockMode);

    @Override
    UpdateStream<X> withHint(String name, Object value);

    @Override
    UpdateStream<X> withHints(Map<String, Object> hints);

    @Override
    UpdateStream<X> withLoadGraph(String name);

    @Override
    UpdateStream<X> withFetchGraph(String name);
}

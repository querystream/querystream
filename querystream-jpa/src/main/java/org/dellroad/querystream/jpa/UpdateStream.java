
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Parameter;
import jakarta.persistence.Query;
import jakarta.persistence.TemporalType;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

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

    /**
     * Set the property described by the specified {@link Path} to the value described by the specified expression.
     *
     * @param path the property to be set
     * @param value JPA expression for the value to set
     * @param <Y> property type
     * @return new modified stream
     * @throws IllegalArgumentException if {@code path} is null
     * @throws IllegalArgumentException if {@code value} is null
     */
    <Y> UpdateStream<X> set(Path<Y> path, Expression<? extends Y> value);

    /**
     * Set the property described by the specified {@link Path} to the specified value.
     *
     * @param path the property to be set
     * @param value the value to set
     * @param <Y> property type
     * @param <V> value type
     * @return new modified stream
     * @throws IllegalArgumentException if {@code path} is null
     */
    <Y, V extends Y> UpdateStream<X> set(Path<Y> path, V value);

    /**
     * Set the property described by the specified attribute to the value described by the specified expression.
     *
     * @param attribute entity attribute to be set
     * @param value JPA expression for the value to set
     * @param <Y> property type
     * @return new modified stream
     * @throws IllegalArgumentException if {@code attribute} is null
     * @throws IllegalArgumentException if {@code value} is null
     */
    <Y> UpdateStream<X> set(SingularAttribute<? super X, Y> attribute, Expression<? extends Y> value);

    /**
     * Set the property described by the specified attribute to the specified value.
     *
     * @param attribute entity attribute to be set
     * @param value the value to set
     * @param <Y> property type
     * @param <V> value type
     * @return new modified stream
     * @throws IllegalArgumentException if {@code attribute} is null
     */
    <Y, V extends Y> UpdateStream<X> set(SingularAttribute<? super X, Y> attribute, V value);

    /**
     * Set the property described by the specified {@link Path} using the value expression returned by the given function.
     *
     * @param path the property to be set
     * @param expressionBuilder function returning a JPA expression for the value to set
     * @param <Y> property type
     * @return new modified stream
     * @throws IllegalArgumentException if {@code path} is null
     * @throws IllegalArgumentException if {@code expressionBuilder} is null
     */
    <Y> UpdateStream<X> set(Path<Y> path, Function<? super Root<X>, ? extends Expression<? extends Y>> expressionBuilder);

    /**
     * Set the property described by the specified attribute using the value expression returned by the given function.
     *
     * @param attribute entity attribute to be set
     * @param expressionBuilder function returning a JPA expression for the value to set
     * @param <Y> property type
     * @return new modified stream
     * @throws IllegalArgumentException if {@code attribute} is null
     * @throws IllegalArgumentException if {@code expressionBuilder} is null
     */
    <Y> UpdateStream<X> set(SingularAttribute<? super X, Y> attribute,
      Function<? super Root<X>, ? extends Expression<? extends Y>> expressionBuilder);

// Narrowing overrides

    @Override
    UpdateType<X> getQueryType();

    @Override
    UpdateStream<X> bind(Ref<X, ? super Root<X>> ref);

    @Override
    <X2, S2 extends Selection<X2>> UpdateStream<X> bind(Ref<X2, ? super S2> ref,
      Function<? super Root<X>, ? extends S2> refFunction);

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
    <T> UpdateStream<X> withParam(Parameter<T> parameter, T value);

    @Override
    UpdateStream<X> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType);

    @Override
    UpdateStream<X> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType);

    @Override
    UpdateStream<X> withParams(Iterable<? extends ParamBinding<?>> params);

    @Override
    UpdateStream<X> withLoadGraph(String name);

    @Override
    UpdateStream<X> withFetchGraph(String name);
}

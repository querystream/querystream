
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Parameter;
import jakarta.persistence.TemporalType;
import jakarta.persistence.criteria.Expression;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A boolean {@link ExprValue}.
 */
public interface BooleanValue extends ExprValue<Boolean, Expression<Boolean>> {

    /**
     * Invert this instance.
     *
     * @return inverse value
     */
    BooleanValue not();

    /**
     * Build and evaluate a JPA query based on this instance and return the single non-NULL result.
     *
     * <p>
     * This variant of {@link #value} is useful when it is known that NULL won't be returned.
     *
     * @return result of executed query
     * @throws NoResultException if the query returns NULL
     */
    default boolean booleanValue() {
        final Boolean value = this.value();
        if (value == null)
            throw new NoResultException("NULL value returned from query");
        return (boolean)value;
    }

// Narrowing overrides (QueryStream)

    @Override
    BooleanValue bind(Ref<Boolean, ? super Expression<Boolean>> ref);

    @Override
    BooleanValue peek(Consumer<? super Expression<Boolean>> peeker);

    @Override
    BooleanValue filter(Function<? super Expression<Boolean>, ? extends Expression<Boolean>> predicateBuilder);

    @Override
    BooleanValue withFlushMode(FlushModeType flushMode);

    @Override
    BooleanValue withLockMode(LockModeType lockMode);

    @Override
    BooleanValue withHint(String name, Object value);

    @Override
    BooleanValue withHints(Map<String, Object> hints);

    @Override
    <T> BooleanValue withParam(Parameter<T> parameter, T value);

    @Override
    BooleanValue withParam(Parameter<Date> parameter, Date value, TemporalType temporalType);

    @Override
    BooleanValue withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType);

    @Override
    BooleanValue withParams(Iterable<? extends ParamBinding<?>> params);

    @Override
    BooleanValue withLoadGraph(String name);

    @Override
    BooleanValue withFetchGraph(String name);
}

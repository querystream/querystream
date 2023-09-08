
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
import jakarta.persistence.criteria.Selection;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A double {@link ExprValue}.
 */
public interface DoubleValue extends ExprValue<Double, Expression<Double>>, DoubleStream {

    /**
     * Build and evaluate a JPA query based on this instance and return the single non-NULL result.
     *
     * <p>
     * This variant of {@link #value} is useful when it is known that NULL won't be returned.
     *
     * @return result of executed query
     * @throws NoResultException if the query returns NULL
     */
    default double doubleValue() {
        final Double value = this.value();
        if (value == null)
            throw new NoResultException("NULL value returned from query");
        return (double)value;
    }

// Narrowing overrides (QueryStream)

    @Override
    DoubleValue bind(Ref<Double, ? super Expression<Double>> ref);

    @Override
    DoubleValue peek(Consumer<? super Expression<Double>> peeker);

    @Override
    <X2, S2 extends Selection<X2>> DoubleValue bind(
      Ref<X2, ? super S2> ref, Function<? super Expression<Double>, ? extends S2> refFunction);

    @Override
    DoubleValue filter(Function<? super Expression<Double>, ? extends Expression<Boolean>> predicateBuilder);

    @Override
    DoubleValue withFlushMode(FlushModeType flushMode);

    @Override
    DoubleValue withLockMode(LockModeType lockMode);

    @Override
    DoubleValue withHint(String name, Object value);

    @Override
    DoubleValue withHints(Map<String, Object> hints);

    @Override
    <T> DoubleValue withParam(Parameter<T> parameter, T value);

    @Override
    DoubleValue withParam(Parameter<Date> parameter, Date value, TemporalType temporalType);

    @Override
    DoubleValue withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType);

    @Override
    DoubleValue withParams(Iterable<? extends ParamBinding<?>> params);

    @Override
    DoubleValue withLoadGraph(String name);

    @Override
    DoubleValue withFetchGraph(String name);
}


/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Parameter;
import javax.persistence.TemporalType;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Selection;

/**
 * A int {@link ExprValue}.
 */
public interface IntValue extends ExprValue<Integer, Expression<Integer>>, IntStream {

    /**
     * Build and evaluate a JPA query based on this instance and return the single non-NULL result.
     *
     * <p>
     * This variant of {@link #value} is useful when it is known that NULL won't be returned.
     *
     * @return result of executed query
     * @throws NoResultException if the query returns NULL
     */
    default int intValue() {
        final Integer value = this.value();
        if (value == null)
            throw new NoResultException("NULL value returned from query");
        return (int)value;
    }

// Narrowing overrides (QueryStream)

    @Override
    IntValue bind(Ref<Integer, ? super Expression<Integer>> ref);

    @Override
    IntValue peek(Consumer<? super Expression<Integer>> peeker);

    @Override
    <X2, S2 extends Selection<X2>> IntValue bind(
      Ref<X2, ? super S2> ref, Function<? super Expression<Integer>, ? extends S2> refFunction);

    @Override
    IntValue filter(Function<? super Expression<Integer>, ? extends Expression<Boolean>> predicateBuilder);

    @Override
    IntValue withFlushMode(FlushModeType flushMode);

    @Override
    IntValue withLockMode(LockModeType lockMode);

    @Override
    IntValue withHint(String name, Object value);

    @Override
    IntValue withHints(Map<String, Object> hints);

    @Override
    <T> IntValue withParam(Parameter<T> parameter, T value);

    @Override
    IntValue withParam(Parameter<Date> parameter, Date value, TemporalType temporalType);

    @Override
    IntValue withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType);

    @Override
    IntValue withParams(Iterable<? extends ParamBinding<?>> params);

    @Override
    IntValue withLoadGraph(String name);

    @Override
    IntValue withFetchGraph(String name);
}

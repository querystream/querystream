
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
 * A long {@link ExprValue}.
 */
public interface LongValue extends ExprValue<Long, Expression<Long>>, LongStream {

    /**
     * Build and evaluate a JPA query based on this instance and return the single non-NULL result.
     *
     * <p>
     * This variant of {@link #value} is useful when it is known that NULL won't be returned.
     *
     * @return result of executed query
     * @throws NoResultException if the query returns NULL
     */
    default long longValue() {
        final Long value = this.value();
        if (value == null)
            throw new NoResultException("NULL value returned from query");
        return (long)value;
    }

// Narrowing overrides (QueryStream)

    @Override
    LongValue bind(Ref<Long, ? super Expression<Long>> ref);

    @Override
    LongValue peek(Consumer<? super Expression<Long>> peeker);

    @Override
    <X2, S2 extends Selection<X2>> LongValue bind(
      Ref<X2, ? super S2> ref, Function<? super Expression<Long>, ? extends S2> refFunction);

    @Override
    LongValue filter(Function<? super Expression<Long>, ? extends Expression<Boolean>> predicateBuilder);

    @Override
    LongValue withFlushMode(FlushModeType flushMode);

    @Override
    LongValue withLockMode(LockModeType lockMode);

    @Override
    LongValue withHint(String name, Object value);

    @Override
    LongValue withHints(Map<String, Object> hints);

    @Override
    <T> LongValue withParam(Parameter<T> parameter, T value);

    @Override
    LongValue withParam(Parameter<Date> parameter, Date value, TemporalType temporalType);

    @Override
    LongValue withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType);

    @Override
    LongValue withParams(Iterable<? extends ParamBinding<?>> params);

    @Override
    LongValue withLoadGraph(String name);

    @Override
    LongValue withFetchGraph(String name);
}

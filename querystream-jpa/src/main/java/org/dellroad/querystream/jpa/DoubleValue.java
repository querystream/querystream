
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.TemporalType;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Selection;

/**
 * A double {@link ExprValue}.
 */
public interface DoubleValue extends ExprValue<Double, Expression<Double>>, DoubleStream {

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
    DoubleValue withParams(Set<ParamBinding<?>> params);

    @Override
    DoubleValue withLoadGraph(String name);

    @Override
    DoubleValue withFetchGraph(String name);
}

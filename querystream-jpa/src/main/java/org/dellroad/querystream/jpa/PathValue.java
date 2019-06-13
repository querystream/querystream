
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
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

/**
 * A {@link PathStream} that is guaranteed to return at most a single result.
 */
public interface PathValue<X, S extends Path<X>> extends ExprValue<X, S>, PathStream<X, S> {

// Narrowing overrides (QueryStream)

    @Override
    PathValue<X, S> bind(Ref<X, ? super S> ref);

    @Override
    PathValue<X, S> peek(Consumer<? super S> peeker);

    @Override
    <X2, S2 extends Selection<X2>> PathValue<X, S> bind(Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction);

    @Override
    PathValue<X, S> filter(SingularAttribute<? super X, Boolean> attribute);

    @Override
    PathValue<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder);

    @Override
    PathValue<X, S> withFlushMode(FlushModeType flushMode);

    @Override
    PathValue<X, S> withLockMode(LockModeType lockMode);

    @Override
    PathValue<X, S> withHint(String name, Object value);

    @Override
    PathValue<X, S> withHints(Map<String, Object> hints);

    @Override
    <T> PathValue<X, S> withParam(Parameter<T> parameter, T value);

    @Override
    PathValue<X, S> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType);

    @Override
    PathValue<X, S> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType);

    @Override
    PathValue<X, S> withParams(Set<ParamBinding<?>> params);

    @Override
    PathValue<X, S> withLoadGraph(String name);

    @Override
    PathValue<X, S> withFetchGraph(String name);
}

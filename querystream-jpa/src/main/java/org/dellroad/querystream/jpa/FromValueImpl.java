
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Parameter;
import jakarta.persistence.TemporalType;
import jakarta.persistence.criteria.AbstractQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.dellroad.querystream.jpa.querytype.SearchType;

class FromValueImpl<X, S extends From<?, X>> extends FromStreamImpl<X, S> implements FromValue<X, S> {

// Constructors

    FromValueImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, QueryInfo queryInfo) {
        super(entityManager, queryType, configurer, queryInfo);
    }

// Subclass required methods

    @Override
    FromValue<X, S> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, QueryInfo queryInfo) {
        return new FromValueImpl<>(entityManager, queryType, configurer, queryInfo);
    }

// Narrowing overrides (QueryStream)

    @Override
    public FromValue<X, S> bind(Ref<X, ? super S> ref) {
        return (FromValue<X, S>)super.bind(ref);
    }

    @Override
    public FromValue<X, S> peek(Consumer<? super S> peeker) {
        return (FromValue<X, S>)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> FromValue<X, S> bind(
      Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction) {
        return (FromValue<X, S>)super.bind(ref, refFunction);
    }

    @Override
    public FromValue<X, S> filter(SingularAttribute<? super X, Boolean> attribute) {
        return (FromValue<X, S>)super.filter(attribute);
    }

    @Override
    public FromValue<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder) {
        return (FromValue<X, S>)super.filter(predicateBuilder);
    }

    @Override
    public FromValue<X, S> withFlushMode(FlushModeType flushMode) {
        return (FromValue<X, S>)super.withFlushMode(flushMode);
    }

    @Override
    public FromValue<X, S> withLockMode(LockModeType lockMode) {
        return (FromValue<X, S>)super.withLockMode(lockMode);
    }

    @Override
    public FromValue<X, S> withHint(String name, Object value) {
        return (FromValue<X, S>)super.withHint(name, value);
    }

    @Override
    public FromValue<X, S> withHints(Map<String, Object> hints) {
        return (FromValue<X, S>)super.withHints(hints);
    }

    @Override
    public <T> FromValue<X, S> withParam(Parameter<T> parameter, T value) {
        return (FromValue<X, S>)super.withParam(parameter, value);
    }

    @Override
    public FromValue<X, S> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType) {
        return (FromValue<X, S>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public FromValue<X, S> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType) {
        return (FromValue<X, S>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public FromValue<X, S> withParams(Iterable<? extends ParamBinding<?>> params) {
        return (FromValue<X, S>)super.withParams(params);
    }

    @Override
    public FromValue<X, S> withLoadGraph(String name) {
        return (FromValue<X, S>)super.withLoadGraph(name);
    }

    @Override
    public FromValue<X, S> withFetchGraph(String name) {
        return (FromValue<X, S>)super.withFetchGraph(name);
    }
}

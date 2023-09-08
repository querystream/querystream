
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
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.dellroad.querystream.jpa.querytype.SearchType;

class RootValueImpl<X> extends RootStreamImpl<X> implements RootValue<X> {

// Constructors

    RootValueImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends Root<X>> configurer, QueryInfo queryInfo) {
        super(entityManager, queryType, configurer, queryInfo);
    }

// Subclass required methods

    @Override
    RootValue<X> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends Root<X>> configurer, QueryInfo queryInfo) {
        return new RootValueImpl<>(entityManager, queryType, configurer, queryInfo);
    }

// Narrowing overrides (QueryStream)

    @Override
    public RootValue<X> bind(Ref<X, ? super Root<X>> ref) {
        return (RootValue<X>)super.bind(ref);
    }

    @Override
    public RootValue<X> peek(Consumer<? super Root<X>> peeker) {
        return (RootValue<X>)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> RootValue<X> bind(
      Ref<X2, ? super S2> ref, Function<? super Root<X>, ? extends S2> refFunction) {
        return (RootValue<X>)super.bind(ref, refFunction);
    }

    @Override
    public RootValue<X> filter(SingularAttribute<? super X, Boolean> attribute) {
        return (RootValue<X>)super.filter(attribute);
    }

    @Override
    public RootValue<X> filter(Function<? super Root<X>, ? extends Expression<Boolean>> predicateBuilder) {
        return (RootValue<X>)super.filter(predicateBuilder);
    }

    @Override
    public RootValue<X> withFlushMode(FlushModeType flushMode) {
        return (RootValue<X>)super.withFlushMode(flushMode);
    }

    @Override
    public RootValue<X> withLockMode(LockModeType lockMode) {
        return (RootValue<X>)super.withLockMode(lockMode);
    }

    @Override
    public RootValue<X> withHint(String name, Object value) {
        return (RootValue<X>)super.withHint(name, value);
    }

    @Override
    public RootValue<X> withHints(Map<String, Object> hints) {
        return (RootValue<X>)super.withHints(hints);
    }

    @Override
    public <T> RootValue<X> withParam(Parameter<T> parameter, T value) {
        return (RootValue<X>)super.withParam(parameter, value);
    }

    @Override
    public RootValue<X> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType) {
        return (RootValue<X>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public RootValue<X> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType) {
        return (RootValue<X>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public RootValue<X> withParams(Iterable<? extends ParamBinding<?>> params) {
        return (RootValue<X>)super.withParams(params);
    }

    @Override
    public RootValue<X> withLoadGraph(String name) {
        return (RootValue<X>)super.withLoadGraph(name);
    }

    @Override
    public RootValue<X> withFetchGraph(String name) {
        return (RootValue<X>)super.withFetchGraph(name);
    }
}

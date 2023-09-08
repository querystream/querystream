
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
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.dellroad.querystream.jpa.querytype.SearchType;

class PathValueImpl<X, S extends Path<X>> extends PathStreamImpl<X, S> implements PathValue<X, S> {

// Constructors

    PathValueImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, QueryInfo queryInfo) {
        super(entityManager, queryType, configurer, queryInfo);
    }

// Subclass required methods

    @Override
    PathValue<X, S> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, QueryInfo queryInfo) {
        return new PathValueImpl<>(entityManager, queryType, configurer, queryInfo);
    }

// Narrowing overrides (QueryStream)

    @Override
    public PathValue<X, S> bind(Ref<X, ? super S> ref) {
        return (PathValue<X, S>)super.bind(ref);
    }

    @Override
    public PathValue<X, S> peek(Consumer<? super S> peeker) {
        return (PathValue<X, S>)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> PathValue<X, S> bind(
      Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction) {
        return (PathValue<X, S>)super.bind(ref, refFunction);
    }

    @Override
    public PathValue<X, S> filter(SingularAttribute<? super X, Boolean> attribute) {
        return (PathValue<X, S>)super.filter(attribute);
    }

    @Override
    public PathValue<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder) {
        return (PathValue<X, S>)super.filter(predicateBuilder);
    }

    @Override
    public PathValue<X, S> withFlushMode(FlushModeType flushMode) {
        return (PathValue<X, S>)super.withFlushMode(flushMode);
    }

    @Override
    public PathValue<X, S> withLockMode(LockModeType lockMode) {
        return (PathValue<X, S>)super.withLockMode(lockMode);
    }

    @Override
    public PathValue<X, S> withHint(String name, Object value) {
        return (PathValue<X, S>)super.withHint(name, value);
    }

    @Override
    public PathValue<X, S> withHints(Map<String, Object> hints) {
        return (PathValue<X, S>)super.withHints(hints);
    }

    @Override
    public <T> PathValue<X, S> withParam(Parameter<T> parameter, T value) {
        return (PathValue<X, S>)super.withParam(parameter, value);
    }

    @Override
    public PathValue<X, S> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType) {
        return (PathValue<X, S>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public PathValue<X, S> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType) {
        return (PathValue<X, S>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public PathValue<X, S> withParams(Iterable<? extends ParamBinding<?>> params) {
        return (PathValue<X, S>)super.withParams(params);
    }

    @Override
    public PathValue<X, S> withLoadGraph(String name) {
        return (PathValue<X, S>)super.withLoadGraph(name);
    }

    @Override
    public PathValue<X, S> withFetchGraph(String name) {
        return (PathValue<X, S>)super.withFetchGraph(name);
    }
}

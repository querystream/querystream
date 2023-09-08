
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
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.dellroad.querystream.jpa.querytype.SearchType;

class ExprValueImpl<X, S extends Expression<X>> extends ExprStreamImpl<X, S> implements ExprValue<X, S> {

// Constructors

    ExprValueImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, QueryInfo queryInfo) {
        super(entityManager, queryType, configurer, queryInfo);
    }

// Subclass required methods

    @Override
    ExprValue<X, S> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, QueryInfo queryInfo) {
        return new ExprValueImpl<>(entityManager, queryType, configurer, queryInfo);
    }

// Narrowing overrides (QueryStream)

    @Override
    public ExprValue<X, S> bind(Ref<X, ? super S> ref) {
        return (ExprValue<X, S>)super.bind(ref);
    }

    @Override
    public ExprValue<X, S> peek(Consumer<? super S> peeker) {
        return (ExprValue<X, S>)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> ExprValue<X, S> bind(
      Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction) {
        return (ExprValue<X, S>)super.bind(ref, refFunction);
    }

    @Override
    public ExprValue<X, S> filter(SingularAttribute<? super X, Boolean> attribute) {
        return (ExprValue<X, S>)super.filter(attribute);
    }

    @Override
    public ExprValue<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder) {
        return (ExprValue<X, S>)super.filter(predicateBuilder);
    }

    @Override
    public ExprValue<X, S> withFlushMode(FlushModeType flushMode) {
        return (ExprValue<X, S>)super.withFlushMode(flushMode);
    }

    @Override
    public ExprValue<X, S> withLockMode(LockModeType lockMode) {
        return (ExprValue<X, S>)super.withLockMode(lockMode);
    }

    @Override
    public ExprValue<X, S> withHint(String name, Object value) {
        return (ExprValue<X, S>)super.withHint(name, value);
    }

    @Override
    public ExprValue<X, S> withHints(Map<String, Object> hints) {
        return (ExprValue<X, S>)super.withHints(hints);
    }

    @Override
    public <T> ExprValue<X, S> withParam(Parameter<T> parameter, T value) {
        return (ExprValue<X, S>)super.withParam(parameter, value);
    }

    @Override
    public ExprValue<X, S> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType) {
        return (ExprValue<X, S>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public ExprValue<X, S> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType) {
        return (ExprValue<X, S>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public ExprValue<X, S> withParams(Iterable<? extends ParamBinding<?>> params) {
        return (ExprValue<X, S>)super.withParams(params);
    }

    @Override
    public ExprValue<X, S> withLoadGraph(String name) {
        return (ExprValue<X, S>)super.withLoadGraph(name);
    }

    @Override
    public ExprValue<X, S> withFetchGraph(String name) {
        return (ExprValue<X, S>)super.withFetchGraph(name);
    }
}

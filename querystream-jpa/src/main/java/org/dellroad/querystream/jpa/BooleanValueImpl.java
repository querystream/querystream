
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.TemporalType;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.Expression;

import org.dellroad.querystream.jpa.querytype.SearchType;

class BooleanValueImpl extends ExprValueImpl<Boolean, Expression<Boolean>> implements BooleanValue {

// Constructors

    BooleanValueImpl(EntityManager entityManager,
      QueryConfigurer<AbstractQuery<?>, Boolean, ? extends Expression<Boolean>> configurer, QueryInfo queryInfo) {
        super(entityManager, new SearchType<>(Boolean.class), configurer, queryInfo);
    }

// BooleanValue

    @Override
    public BooleanValue not() {
        return new BooleanValueImpl(this.entityManager,
          (builder, query) -> builder.not(this.configurer.configure(builder, query)), this.queryInfo);
    }

// Subclass required methods

    @Override
    BooleanValue create(EntityManager entityManager, SearchType<Boolean> queryType,
      QueryConfigurer<AbstractQuery<?>, Boolean, ? extends Expression<Boolean>> configurer, QueryInfo queryInfo) {
        return new BooleanValueImpl(entityManager, configurer, queryInfo);
    }

// Narrowing overrides (QueryStream)

    @Override
    public BooleanValue bind(Ref<Boolean, ? super Expression<Boolean>> ref) {
        return (BooleanValue)super.bind(ref);
    }

    @Override
    public BooleanValue peek(Consumer<? super Expression<Boolean>> peeker) {
        return (BooleanValue)super.peek(peeker);
    }

    @Override
    public BooleanValue filter(Function<? super Expression<Boolean>, ? extends Expression<Boolean>> predicateBuilder) {
        return (BooleanValue)super.filter(predicateBuilder);
    }

    @Override
    public BooleanValue withFlushMode(FlushModeType flushMode) {
        return (BooleanValue)super.withFlushMode(flushMode);
    }

    @Override
    public BooleanValue withLockMode(LockModeType lockMode) {
        return (BooleanValue)super.withLockMode(lockMode);
    }

    @Override
    public BooleanValue withHint(String name, Object value) {
        return (BooleanValue)super.withHint(name, value);
    }

    @Override
    public BooleanValue withHints(Map<String, Object> hints) {
        return (BooleanValue)super.withHints(hints);
    }

    @Override
    public <T> BooleanValue withParam(Parameter<T> parameter, T value) {
        return (BooleanValue)super.withParam(parameter, value);
    }

    @Override
    public BooleanValue withParam(Parameter<Date> parameter, Date value, TemporalType temporalType) {
        return (BooleanValue)super.withParam(parameter, value, temporalType);
    }

    @Override
    public BooleanValue withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType) {
        return (BooleanValue)super.withParam(parameter, value, temporalType);
    }

    @Override
    public BooleanValue withParams(Iterable<? extends ParamBinding<?>> params) {
        return (BooleanValue)super.withParams(params);
    }

    @Override
    public BooleanValue withLoadGraph(String name) {
        return (BooleanValue)super.withLoadGraph(name);
    }

    @Override
    public BooleanValue withFetchGraph(String name) {
        return (BooleanValue)super.withFetchGraph(name);
    }
}

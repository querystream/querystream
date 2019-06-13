
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

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.TemporalType;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

class IntValueImpl extends IntStreamImpl implements IntValue {

// Constructors

    IntValueImpl(EntityManager entityManager,
      QueryConfigurer<AbstractQuery<?>, Integer, ? extends Expression<Integer>> configurer, QueryInfo queryInfo) {
        super(entityManager, configurer, queryInfo);
    }

// Subclass required methods

    @Override
    IntValue create(EntityManager entityManager, SearchType<Integer> queryType,
      QueryConfigurer<AbstractQuery<?>, Integer, ? extends Expression<Integer>> configurer, QueryInfo queryInfo) {
        return new IntValueImpl(entityManager, configurer, queryInfo);
    }

// Narrowing overrides (QueryStream)

    @Override
    public IntValue bind(Ref<Integer, ? super Expression<Integer>> ref) {
        return (IntValue)super.bind(ref);
    }

    @Override
    public IntValue peek(Consumer<? super Expression<Integer>> peeker) {
        return (IntValue)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> IntValue bind(
      Ref<X2, ? super S2> ref, Function<? super Expression<Integer>, ? extends S2> refFunction) {
        return (IntValue)super.bind(ref, refFunction);
    }

    @Override
    public IntValue filter(SingularAttribute<? super Integer, Boolean> attribute) {         // makes no sense but needed for API
        return (IntValue)super.filter(attribute);
    }

    @Override
    public IntValue filter(Function<? super Expression<Integer>, ? extends Expression<Boolean>> predicateBuilder) {
        return (IntValue)super.filter(predicateBuilder);
    }

    @Override
    public IntValue withFlushMode(FlushModeType flushMode) {
        return (IntValue)super.withFlushMode(flushMode);
    }

    @Override
    public IntValue withLockMode(LockModeType lockMode) {
        return (IntValue)super.withLockMode(lockMode);
    }

    @Override
    public IntValue withHint(String name, Object value) {
        return (IntValue)super.withHint(name, value);
    }

    @Override
    public IntValue withHints(Map<String, Object> hints) {
        return (IntValue)super.withHints(hints);
    }

    @Override
    public <T> IntValue withParam(Parameter<T> parameter, T value) {
        return (IntValue)super.withParam(parameter, value);
    }

    @Override
    public IntValue withParam(Parameter<Date> parameter, Date value, TemporalType temporalType) {
        return (IntValue)super.withParam(parameter, value, temporalType);
    }

    @Override
    public IntValue withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType) {
        return (IntValue)super.withParam(parameter, value, temporalType);
    }

    @Override
    public IntValue withParams(Set<ParamBinding<?>> params) {
        return (IntValue)super.withParams(params);
    }

    @Override
    public IntValue withLoadGraph(String name) {
        return (IntValue)super.withLoadGraph(name);
    }

    @Override
    public IntValue withFetchGraph(String name) {
        return (IntValue)super.withFetchGraph(name);
    }
}

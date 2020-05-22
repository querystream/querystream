
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
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

class DoubleValueImpl extends DoubleStreamImpl implements DoubleValue {

// Constructors

    DoubleValueImpl(EntityManager entityManager,
      QueryConfigurer<AbstractQuery<?>, Double, ? extends Expression<Double>> configurer, QueryInfo queryInfo) {
        super(entityManager, configurer, queryInfo);
    }

// Subclass required methods

    @Override
    DoubleValue create(EntityManager entityManager, SearchType<Double> queryType,
      QueryConfigurer<AbstractQuery<?>, Double, ? extends Expression<Double>> configurer, QueryInfo queryInfo) {
        return new DoubleValueImpl(entityManager, configurer, queryInfo);
    }

// Narrowing overrides (QueryStream)

    @Override
    public DoubleValue bind(Ref<Double, ? super Expression<Double>> ref) {
        return (DoubleValue)super.bind(ref);
    }

    @Override
    public DoubleValue peek(Consumer<? super Expression<Double>> peeker) {
        return (DoubleValue)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> DoubleValue bind(
      Ref<X2, ? super S2> ref, Function<? super Expression<Double>, ? extends S2> refFunction) {
        return (DoubleValue)super.bind(ref, refFunction);
    }

    @Override
    public DoubleValue filter(SingularAttribute<? super Double, Boolean> attribute) {       // makes no sense but needed for API
        return (DoubleValue)super.filter(attribute);
    }

    @Override
    public DoubleValue filter(Function<? super Expression<Double>, ? extends Expression<Boolean>> predicateBuilder) {
        return (DoubleValue)super.filter(predicateBuilder);
    }

    @Override
    public DoubleValue withFlushMode(FlushModeType flushMode) {
        return (DoubleValue)super.withFlushMode(flushMode);
    }

    @Override
    public DoubleValue withLockMode(LockModeType lockMode) {
        return (DoubleValue)super.withLockMode(lockMode);
    }

    @Override
    public DoubleValue withHint(String name, Object value) {
        return (DoubleValue)super.withHint(name, value);
    }

    @Override
    public DoubleValue withHints(Map<String, Object> hints) {
        return (DoubleValue)super.withHints(hints);
    }

    @Override
    public <T> DoubleValue withParam(Parameter<T> parameter, T value) {
        return (DoubleValue)super.withParam(parameter, value);
    }

    @Override
    public DoubleValue withParam(Parameter<Date> parameter, Date value, TemporalType temporalType) {
        return (DoubleValue)super.withParam(parameter, value, temporalType);
    }

    @Override
    public DoubleValue withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType) {
        return (DoubleValue)super.withParam(parameter, value, temporalType);
    }

    @Override
    public DoubleValue withParams(Iterable<? extends ParamBinding<?>> params) {
        return (DoubleValue)super.withParams(params);
    }

    @Override
    public DoubleValue withLoadGraph(String name) {
        return (DoubleValue)super.withLoadGraph(name);
    }

    @Override
    public DoubleValue withFetchGraph(String name) {
        return (DoubleValue)super.withFetchGraph(name);
    }
}

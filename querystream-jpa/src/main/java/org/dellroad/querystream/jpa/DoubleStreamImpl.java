
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
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.dellroad.querystream.jpa.querytype.SearchType;

class DoubleStreamImpl extends ExprStreamImpl<Double, Expression<Double>> implements DoubleStream {

// Constructors

    DoubleStreamImpl(EntityManager entityManager,
      QueryConfigurer<AbstractQuery<?>, Double, ? extends Expression<Double>> configurer, QueryInfo queryInfo) {
        super(entityManager, new SearchType<Double>(Double.class), configurer, queryInfo);
    }

// Aggregation

    @Override
    public DoubleValue average() {
        QueryStreamImpl.checkOffsetLimit(this, "average()");
        return new DoubleValueImpl(this.entityManager,
          (builder, query) -> builder.avg(this.configurer.configure(builder, query)), this.queryInfo);
    }

    @Override
    public DoubleValue max() {
        QueryStreamImpl.checkOffsetLimit(this, "max()");
        return new DoubleValueImpl(this.entityManager,
          (builder, query) -> builder.max(this.configurer.configure(builder, query)), this.queryInfo);
    }

    @Override
    public DoubleValue min() {
        QueryStreamImpl.checkOffsetLimit(this, "min()");
        return new DoubleValueImpl(this.entityManager,
          (builder, query) -> builder.min(this.configurer.configure(builder, query)), this.queryInfo);
    }

    @Override
    public DoubleValue sum() {
        QueryStreamImpl.checkOffsetLimit(this, "sum()");
        return new DoubleValueImpl(this.entityManager,
          (builder, query) -> builder.sum(this.configurer.configure(builder, query)), this.queryInfo);
    }

// Narrowing overrides (SearchStreamImpl)

    @Override
    DoubleStream create(EntityManager entityManager, SearchType<Double> queryType,
      QueryConfigurer<AbstractQuery<?>, Double, ? extends Expression<Double>> configurer, QueryInfo queryInfo) {
        return new DoubleStreamImpl(entityManager, configurer, queryInfo);
    }

    @Override
    DoubleValue toValue() {
        return this.toValue(false);
    }

    @Override
    DoubleValue toValue(boolean forceLimit) {
        return new DoubleValueImpl(this.entityManager,
          this.configurer, forceLimit ? this.queryInfo.withMaxResults(1) : this.queryInfo);
    }

    @Override
    public DoubleStream distinct() {
        return (DoubleStream)super.distinct();
    }

    @Override
    public DoubleStream orderBy(Ref<?, ? extends Expression<?>> ref, boolean asc) {
        return (DoubleStream)super.orderBy(ref, asc);
    }

    @Override
    public DoubleStream orderBy(Function<? super Expression<Double>, ? extends Expression<?>> orderExprFunction, boolean asc) {
        return (DoubleStream)super.orderBy(orderExprFunction, asc);
    }

    @Override
    public DoubleStream orderBy(Order... orders) {
        return (DoubleStream)super.orderBy(orders);
    }

    @Override
    public DoubleStream orderByMulti(Function<? super Expression<Double>, ? extends List<? extends Order>> orderListFunction) {
        return (DoubleStream)super.orderByMulti(orderListFunction);
    }

    @Override
    public DoubleStream thenOrderBy(Ref<?, ? extends Expression<?>> ref, boolean asc) {
        return (DoubleStream)super.thenOrderBy(ref, asc);
    }

    @Override
    public DoubleStream thenOrderBy(Order... orders) {
        return (DoubleStream)super.thenOrderBy(orders);
    }

    @Override
    public DoubleStream thenOrderBy(Function<? super Expression<Double>, ? extends Expression<?>> orderExprFunction, boolean asc) {
        return (DoubleStream)super.thenOrderBy(orderExprFunction, asc);
    }

    @Override
    public DoubleStream groupBy(Ref<?, ? extends Expression<?>> ref) {
        return (DoubleStream)super.groupBy(ref);
    }

    @Override
    public DoubleStream groupBy(Function<? super Expression<Double>, ? extends Expression<?>> groupFunction) {
        return (DoubleStream)super.groupBy(groupFunction);
    }

    @Override
    public DoubleStream groupByMulti(Function<? super Expression<Double>, ? extends List<Expression<?>>> groupFunction) {
        return (DoubleStream)super.groupByMulti(groupFunction);
    }

    @Override
    public DoubleStream having(Function<? super Expression<Double>, ? extends Expression<Boolean>> havingFunction) {
        return (DoubleStream)super.having(havingFunction);
    }

    @Override
    public DoubleValue findAny() {
        return (DoubleValue)super.findAny();
    }

    @Override
    public DoubleValue findFirst() {
        return (DoubleValue)super.findFirst();
    }

    @Override
    public DoubleValue findSingle() {
        return (DoubleValue)super.findSingle();
    }

    @Override
    public <R> DoubleStream addRoot(Ref<R, ? super Root<R>> ref, Class<R> type) {
        return (DoubleStream)super.addRoot(ref, type);
    }

// Narrowing overrides (QueryStreamImpl)

    @Override
    public DoubleStream bind(Ref<Double, ? super Expression<Double>> ref) {
        return (DoubleStream)super.bind(ref);
    }

    @Override
    public DoubleStream peek(Consumer<? super Expression<Double>> peeker) {
        return (DoubleStream)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> DoubleStream bind(
      Ref<X2, ? super S2> ref, Function<? super Expression<Double>, ? extends S2> refFunction) {
        return (DoubleStream)super.bind(ref, refFunction);
    }

    @Override
    public DoubleStream filter(Function<? super Expression<Double>, ? extends Expression<Boolean>> predicateBuilder) {
        return (DoubleStream)super.filter(predicateBuilder);
    }

    @Override
    public DoubleStream limit(int limit) {
        return (DoubleStream)super.limit(limit);
    }

    @Override
    public DoubleStream skip(int skip) {
        return (DoubleStream)super.skip(skip);
    }

    @Override
    public DoubleStream withFlushMode(FlushModeType flushMode) {
        return (DoubleStream)super.withFlushMode(flushMode);
    }

    @Override
    public DoubleStream withLockMode(LockModeType lockMode) {
        return (DoubleStream)super.withLockMode(lockMode);
    }

    @Override
    public DoubleStream withHint(String name, Object value) {
        return (DoubleStream)super.withHint(name, value);
    }

    @Override
    public DoubleStream withHints(Map<String, Object> hints) {
        return (DoubleStream)super.withHints(hints);
    }

    @Override
    public <T> DoubleStream withParam(Parameter<T> parameter, T value) {
        return (DoubleStream)super.withParam(parameter, value);
    }

    @Override
    public DoubleStream withParam(Parameter<Date> parameter, Date value, TemporalType temporalType) {
        return (DoubleStream)super.withParam(parameter, value, temporalType);
    }

    @Override
    public DoubleStream withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType) {
        return (DoubleStream)super.withParam(parameter, value, temporalType);
    }

    @Override
    public DoubleStream withParams(Iterable<? extends ParamBinding<?>> params) {
        return (DoubleStream)super.withParams(params);
    }

    @Override
    public DoubleStream withLoadGraph(String name) {
        return (DoubleStream)super.withLoadGraph(name);
    }

    @Override
    public DoubleStream withFetchGraph(String name) {
        return (DoubleStream)super.withFetchGraph(name);
    }
}

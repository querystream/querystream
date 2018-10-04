
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.dellroad.querystream.jpa.querytype.SearchType;

class DoubleStreamImpl extends ExprStreamImpl<Double, Expression<Double>> implements DoubleStream {

// Constructors

    DoubleStreamImpl(EntityManager entityManager,
      QueryConfigurer<AbstractQuery<?>, Double, ? extends Expression<Double>> configurer, int firstResult, int maxResults) {
        super(entityManager, new SearchType<Double>(Double.class), configurer, firstResult, maxResults);
    }

// Aggregation

    @Override
    public DoubleValue average() {
        QueryStreamImpl.checkOffsetLimit(this, "average()");
        return new DoubleValueImpl(this.entityManager,
          (builder, query) -> builder.avg(this.configurer.configure(builder, query)), -1, -1);
    }

    @Override
    public DoubleValue max() {
        QueryStreamImpl.checkOffsetLimit(this, "max()");
        return new DoubleValueImpl(this.entityManager,
          (builder, query) -> builder.max(this.configurer.configure(builder, query)), -1, -1);
    }

    @Override
    public DoubleValue min() {
        QueryStreamImpl.checkOffsetLimit(this, "min()");
        return new DoubleValueImpl(this.entityManager,
          (builder, query) -> builder.min(this.configurer.configure(builder, query)), -1, -1);
    }

    @Override
    public DoubleValue sum() {
        QueryStreamImpl.checkOffsetLimit(this, "sum()");
        return new DoubleValueImpl(this.entityManager,
          (builder, query) -> builder.sum(this.configurer.configure(builder, query)), -1, -1);
    }

// Narrowing overrides (SearchStreamImpl)

    @Override
    DoubleStream create(EntityManager entityManager, SearchType<Double> queryType,
      QueryConfigurer<AbstractQuery<?>, Double, ? extends Expression<Double>> configurer, int firstResult, int maxResults) {
        return new DoubleStreamImpl(entityManager, configurer, firstResult, maxResults);
    }

    @Override
    DoubleValue toValue() {
        return this.toValue(false);
    }

    @Override
    DoubleValue toValue(boolean forceLimit) {
        return new DoubleValueImpl(this.entityManager, this.configurer, this.firstResult, forceLimit ? 1 : this.maxResults);
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
}

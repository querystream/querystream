
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.List;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;

import org.dellroad.querystream.jpa.querytype.SearchType;

class DoubleStreamImpl extends ExprStreamImpl<Double, Expression<Double>> implements DoubleStream {

// Constructors

    DoubleStreamImpl(EntityManager entityManager,
      QueryConfigurer<AbstractQuery<?>, Double, ? extends Expression<Double>> configurer) {
        super(entityManager, new SearchType<Double>(Double.class), configurer);
    }

// Aggregation

    @Override
    public DoubleValue average() {
        return new DoubleValueImpl(this.entityManager, (builder, query) -> builder.avg(this.configurer.configure(builder, query)));
    }

    @Override
    public DoubleValue max() {
        return new DoubleValueImpl(this.entityManager, (builder, query) -> builder.max(this.configurer.configure(builder, query)));
    }

    @Override
    public DoubleValue min() {
        return new DoubleValueImpl(this.entityManager, (builder, query) -> builder.min(this.configurer.configure(builder, query)));
    }

    @Override
    public DoubleValue sum() {
        return new DoubleValueImpl(this.entityManager, (builder, query) -> builder.sum(this.configurer.configure(builder, query)));
    }

// Narrowing overrides (SearchStreamImpl)

    @Override
    DoubleStream create(EntityManager entityManager, SearchType<Double> queryType,
      QueryConfigurer<AbstractQuery<?>, Double, ? extends Expression<Double>> configurer) {
        return new DoubleStreamImpl(entityManager, configurer);
    }

    @Override
    DoubleValue toValue() {
        return new DoubleValueImpl(this.entityManager, this.configurer);
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

// Narrowing overrides (QueryStreamImpl)

    @Override
    public DoubleStream bind(Ref<Double, ? super Expression<Double>> ref) {
        return (DoubleStream)super.bind(ref);
    }

    @Override
    public DoubleStream filter(Function<? super Expression<Double>, ? extends Expression<Boolean>> predicateBuilder) {
        return (DoubleStream)super.filter(predicateBuilder);
    }
}

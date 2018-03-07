
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

class IntStreamImpl extends ExprStreamImpl<Integer, Expression<Integer>> implements IntStream {

// Constructors

    IntStreamImpl(EntityManager entityManager,
      QueryConfigurer<AbstractQuery<?>, Integer, ? extends Expression<Integer>> configurer) {
        super(entityManager, new SearchType<Integer>(Integer.class), configurer);
    }

// Aggregation

    @Override
    public DoubleValue average() {
        return new DoubleValueImpl(this.entityManager, (builder, query) -> builder.avg(this.configurer.configure(builder, query)));
    }

    @Override
    public IntValue max() {
        return new IntValueImpl(this.entityManager, (builder, query) -> builder.max(this.configurer.configure(builder, query)));
    }

    @Override
    public IntValue min() {
        return new IntValueImpl(this.entityManager, (builder, query) -> builder.min(this.configurer.configure(builder, query)));
    }

    @Override
    public IntValue sum() {
        return new IntValueImpl(this.entityManager, (builder, query) -> builder.sum(this.configurer.configure(builder, query)));
    }

// Narrowing overrides (SearchStreamImpl)

    @Override
    IntStream create(EntityManager entityManager, SearchType<Integer> queryType,
      QueryConfigurer<AbstractQuery<?>, Integer, ? extends Expression<Integer>> configurer) {
        return new IntStreamImpl(entityManager, configurer);
    }

    @Override
    IntValue toValue() {
        return new IntValueImpl(this.entityManager, this.configurer);
    }

    @Override
    public IntStream distinct() {
        return (IntStream)super.distinct();
    }

    @Override
    public IntStream orderBy(Ref<?, ? extends Expression<?>> ref, boolean asc) {
        return (IntStream)super.orderBy(ref, asc);
    }

    @Override
    public IntStream orderBy(Function<? super Expression<Integer>, ? extends Expression<?>> orderExprFunction, boolean asc) {
        return (IntStream)super.orderBy(orderExprFunction, asc);
    }

    @Override
    public IntStream orderByMulti(Function<? super Expression<Integer>, ? extends List<? extends Order>> orderListFunction) {
        return (IntStream)super.orderByMulti(orderListFunction);
    }

    @Override
    public IntStream groupBy(Ref<?, ? extends Expression<?>> ref) {
        return (IntStream)super.groupBy(ref);
    }

    @Override
    public IntStream groupBy(Function<? super Expression<Integer>, ? extends Expression<?>> groupFunction) {
        return (IntStream)super.groupBy(groupFunction);
    }

    @Override
    public IntStream groupByMulti(Function<? super Expression<Integer>, ? extends List<Expression<?>>> groupFunction) {
        return (IntStream)super.groupByMulti(groupFunction);
    }

    @Override
    public IntStream having(Function<? super Expression<Integer>, ? extends Expression<Boolean>> havingFunction) {
        return (IntStream)super.having(havingFunction);
    }

    @Override
    public IntValue findAny() {
        return (IntValue)super.findAny();
    }

    @Override
    public IntValue findFirst() {
        return (IntValue)super.findFirst();
    }

// Narrowing overrides (QueryStreamImpl)

    @Override
    public IntStream bind(Ref<Integer, ? super Expression<Integer>> ref) {
        return (IntStream)super.bind(ref);
    }

    @Override
    public IntStream filter(Function<? super Expression<Integer>, ? extends Expression<Boolean>> predicateBuilder) {
        return (IntStream)super.filter(predicateBuilder);
    }
}


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

class IntStreamImpl extends ExprStreamImpl<Integer, Expression<Integer>> implements IntStream {

// Constructors

    IntStreamImpl(EntityManager entityManager,
      QueryConfigurer<AbstractQuery<?>, Integer, ? extends Expression<Integer>> configurer, int firstResult, int maxResults) {
        super(entityManager, new SearchType<Integer>(Integer.class), configurer, firstResult, maxResults);
    }

// Mapping

    @Override
    public LongStream asLongStream() {
        return new LongStreamImpl(this.getEntityManager(),
          (builder, query) -> builder.toLong(this.configure(builder, query)), this.firstResult, this.maxResults);
    }

    @Override
    public DoubleStream asDoubleStream() {
        return new DoubleStreamImpl(this.getEntityManager(),
          (builder, query) -> builder.toDouble(this.configure(builder, query)), this.firstResult, this.maxResults);
    }

// Aggregation

    @Override
    public DoubleValue average() {
        QueryStreamImpl.checkOffsetLimit(this, "average()");
        return new DoubleValueImpl(this.entityManager,
          (builder, query) -> builder.avg(this.configurer.configure(builder, query)), -1, -1);
    }

    @Override
    public IntValue max() {
        QueryStreamImpl.checkOffsetLimit(this, "max()");
        return new IntValueImpl(this.entityManager,
          (builder, query) -> builder.max(this.configurer.configure(builder, query)), -1, -1);
    }

    @Override
    public IntValue min() {
        QueryStreamImpl.checkOffsetLimit(this, "min()");
        return new IntValueImpl(this.entityManager,
          (builder, query) -> builder.min(this.configurer.configure(builder, query)), -1, -1);
    }

    @Override
    public IntValue sum() {
        QueryStreamImpl.checkOffsetLimit(this, "sum()");
        return new IntValueImpl(this.entityManager,
          (builder, query) -> builder.sum(this.configurer.configure(builder, query)), -1, -1);
    }

// Narrowing overrides (SearchStreamImpl)

    @Override
    IntStream create(EntityManager entityManager, SearchType<Integer> queryType,
      QueryConfigurer<AbstractQuery<?>, Integer, ? extends Expression<Integer>> configurer, int firstResult, int maxResults) {
        return new IntStreamImpl(entityManager, configurer, firstResult, maxResults);
    }

    @Override
    IntValue toValue() {
        return this.toValue(false);
    }

    @Override
    IntValue toValue(boolean forceLimit) {
        return new IntValueImpl(this.entityManager, this.configurer, this.firstResult, forceLimit ? 1 : this.maxResults);
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
    public IntStream orderBy(Order... orders) {
        return (IntStream)super.orderBy(orders);
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

    @Override
    public <R> IntStream addRoot(Ref<R, ? super Root<R>> ref, Class<R> type) {
        return (IntStream)super.addRoot(ref, type);
    }

// Narrowing overrides (QueryStreamImpl)

    @Override
    public IntStream bind(Ref<Integer, ? super Expression<Integer>> ref) {
        return (IntStream)super.bind(ref);
    }

    @Override
    public IntStream peek(Consumer<? super Expression<Integer>> peeker) {
        return (IntStream)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> IntStream bind(
      Ref<X2, ? super S2> ref, Function<? super Expression<Integer>, ? extends S2> refFunction) {
        return (IntStream)super.bind(ref, refFunction);
    }

    @Override
    public IntStream filter(Function<? super Expression<Integer>, ? extends Expression<Boolean>> predicateBuilder) {
        return (IntStream)super.filter(predicateBuilder);
    }

    @Override
    public IntStream limit(int limit) {
        return (IntStream)super.limit(limit);
    }

    @Override
    public IntStream skip(int skip) {
        return (IntStream)super.skip(skip);
    }
}

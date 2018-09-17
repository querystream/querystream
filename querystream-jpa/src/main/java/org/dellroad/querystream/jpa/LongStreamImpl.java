
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

class LongStreamImpl extends ExprStreamImpl<Long, Expression<Long>> implements LongStream {

// Constructors

    LongStreamImpl(EntityManager entityManager,
      QueryConfigurer<AbstractQuery<?>, Long, ? extends Expression<Long>> configurer, int firstResult, int maxResults) {
        super(entityManager, new SearchType<Long>(Long.class), configurer, firstResult, maxResults);
    }

// Mapping

    @Override
    public DoubleStream asDoubleStream() {
        return new DoubleStreamImpl(this.getEntityManager(),
          (builder, query) -> builder.toDouble(this.configure(builder, query)), this.firstResult, this.maxResults);
    }

// Aggregation

    @Override
    public DoubleValue average() {
        QueryStreamImpl.checkOffsetLimit(this, "average() must be performed prior to skip() or limit()");
        return new DoubleValueImpl(this.entityManager,
          (builder, query) -> builder.avg(this.configurer.configure(builder, query)), -1, -1);
    }

    @Override
    public LongValue max() {
        QueryStreamImpl.checkOffsetLimit(this, "max() must be performed prior to skip() or limit()");
        return new LongValueImpl(this.entityManager,
          (builder, query) -> builder.max(this.configurer.configure(builder, query)), -1, -1);
    }

    @Override
    public LongValue min() {
        QueryStreamImpl.checkOffsetLimit(this, "min() must be performed prior to skip() or limit()");
        return new LongValueImpl(this.entityManager,
          (builder, query) -> builder.min(this.configurer.configure(builder, query)), -1, -1);
    }

    @Override
    public LongValue sum() {
        QueryStreamImpl.checkOffsetLimit(this, "sum() must be performed prior to skip() or limit()");
        return new LongValueImpl(this.entityManager,
          (builder, query) -> builder.sum(this.configurer.configure(builder, query)), -1, -1);
    }

// Narrowing overrides (SearchStreamImpl)

    @Override
    LongStream create(EntityManager entityManager, SearchType<Long> queryType,
      QueryConfigurer<AbstractQuery<?>, Long, ? extends Expression<Long>> configurer, int firstResult, int maxResults) {
        return new LongStreamImpl(entityManager, configurer, firstResult, maxResults);
    }

    @Override
    LongValue toValue() {
        return this.toValue(false);
    }

    @Override
    LongValue toValue(boolean forceLimit) {
        return new LongValueImpl(this.entityManager, this.configurer, this.firstResult, forceLimit ? 1 : this.maxResults);
    }

    @Override
    public LongStream distinct() {
        return (LongStream)super.distinct();
    }

    @Override
    public LongStream orderBy(Ref<?, ? extends Expression<?>> ref, boolean asc) {
        return (LongStream)super.orderBy(ref, asc);
    }

    @Override
    public LongStream orderBy(Function<? super Expression<Long>, ? extends Expression<?>> orderExprFunction, boolean asc) {
        return (LongStream)super.orderBy(orderExprFunction, asc);
    }

    @Override
    public LongStream orderByMulti(Function<? super Expression<Long>, ? extends List<? extends Order>> orderListFunction) {
        return (LongStream)super.orderByMulti(orderListFunction);
    }

    @Override
    public LongStream groupBy(Ref<?, ? extends Expression<?>> ref) {
        return (LongStream)super.groupBy(ref);
    }

    @Override
    public LongStream groupBy(Function<? super Expression<Long>, ? extends Expression<?>> groupFunction) {
        return (LongStream)super.groupBy(groupFunction);
    }

    @Override
    public LongStream groupByMulti(Function<? super Expression<Long>, ? extends List<Expression<?>>> groupFunction) {
        return (LongStream)super.groupByMulti(groupFunction);
    }

    @Override
    public LongStream having(Function<? super Expression<Long>, ? extends Expression<Boolean>> havingFunction) {
        return (LongStream)super.having(havingFunction);
    }

    @Override
    public LongValue findAny() {
        return (LongValue)super.findAny();
    }

    @Override
    public LongValue findFirst() {
        return (LongValue)super.findFirst();
    }

    @Override
    public <R> LongStream addRoot(Ref<R, ? super Root<R>> ref, Class<R> type) {
        return (LongStream)super.addRoot(ref, type);
    }

// Narrowing overrides (QueryStreamImpl)

    @Override
    public LongStream bind(Ref<Long, ? super Expression<Long>> ref) {
        return (LongStream)super.bind(ref);
    }

    @Override
    public LongStream peek(Consumer<? super Expression<Long>> peeker) {
        return (LongStream)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> LongStream bind(
      Ref<X2, ? super S2> ref, Function<? super Expression<Long>, ? extends S2> refFunction) {
        return (LongStream)super.bind(ref, refFunction);
    }

    @Override
    public LongStream filter(Function<? super Expression<Long>, ? extends Expression<Boolean>> predicateBuilder) {
        return (LongStream)super.filter(predicateBuilder);
    }

    @Override
    public LongStream limit(int limit) {
        return (LongStream)super.limit(limit);
    }

    @Override
    public LongStream skip(int skip) {
        return (LongStream)super.skip(skip);
    }
}

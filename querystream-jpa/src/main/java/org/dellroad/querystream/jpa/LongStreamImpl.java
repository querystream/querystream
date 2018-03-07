
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

class LongStreamImpl extends ExprStreamImpl<Long, Expression<Long>> implements LongStream {

// Constructors

    LongStreamImpl(EntityManager entityManager,
      QueryConfigurer<AbstractQuery<?>, Long, ? extends Expression<Long>> configurer) {
        super(entityManager, new SearchType<Long>(Long.class), configurer);
    }

// Aggregation

    @Override
    public DoubleValue average() {
        return new DoubleValueImpl(this.entityManager, (builder, query) -> builder.avg(this.configurer.configure(builder, query)));
    }

    @Override
    public LongValue max() {
        return new LongValueImpl(this.entityManager, (builder, query) -> builder.max(this.configurer.configure(builder, query)));
    }

    @Override
    public LongValue min() {
        return new LongValueImpl(this.entityManager, (builder, query) -> builder.min(this.configurer.configure(builder, query)));
    }

    @Override
    public LongValue sum() {
        return new LongValueImpl(this.entityManager, (builder, query) -> builder.sum(this.configurer.configure(builder, query)));
    }

// Narrowing overrides (SearchStreamImpl)

    @Override
    LongStream create(EntityManager entityManager, SearchType<Long> queryType,
      QueryConfigurer<AbstractQuery<?>, Long, ? extends Expression<Long>> configurer) {
        return new LongStreamImpl(entityManager, configurer);
    }

    @Override
    LongValue toValue() {
        return new LongValueImpl(this.entityManager, this.configurer);
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

// Narrowing overrides (QueryStreamImpl)

    @Override
    public LongStream bind(Ref<Long, ? super Expression<Long>> ref) {
        return (LongStream)super.bind(ref);
    }

    @Override
    public LongStream filter(Function<? super Expression<Long>, ? extends Expression<Boolean>> predicateBuilder) {
        return (LongStream)super.filter(predicateBuilder);
    }
}

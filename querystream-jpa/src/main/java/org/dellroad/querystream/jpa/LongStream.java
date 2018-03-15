
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.List;
import java.util.function.Function;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Selection;

/**
 * {@link SearchStream} containing {@link Long} values.
 */
public interface LongStream extends ExprStream<Long, Expression<Long>> {

// Aggregation

    DoubleValue average();

    LongValue max();

    LongValue min();

    LongValue sum();

// Mapping

    default DoubleStream asDoubleStream() {
        return new DoubleStreamImpl(this.getEntityManager(), (builder, query) -> builder.toDouble(this.configure(builder, query)));
    }

// Narrowing overrides (SearchStream)

    @Override
    LongStream distinct();

    @Override
    LongStream orderBy(Ref<?, ? extends Expression<?>> ref, boolean asc);

    @Override
    LongStream orderBy(Function<? super Expression<Long>, ? extends Expression<?>> orderExprFunction, boolean asc);

    @Override
    LongStream orderByMulti(Function<? super Expression<Long>, ? extends List<? extends Order>> orderListFunction);

    @Override
    LongStream groupBy(Ref<?, ? extends Expression<?>> ref);

    @Override
    LongStream groupBy(Function<? super Expression<Long>, ? extends Expression<?>> groupFunction);

    @Override
    LongStream groupByMulti(Function<? super Expression<Long>, ? extends List<Expression<?>>> groupFunction);

    @Override
    LongStream having(Function<? super Expression<Long>, ? extends Expression<Boolean>> havingFunction);

    @Override
    LongValue findAny();

    @Override
    LongValue findFirst();

// Narrowing overrides (QueryStream)

    @Override
    LongStream bind(Ref<Long, ? super Expression<Long>> ref);

    @Override
    <X2, S2 extends Selection<X2>> LongStream bind(
      Ref<X2, ? super S2> ref, Function<? super Expression<Long>, ? extends S2> refFunction);

    @Override
    LongStream filter(Function<? super Expression<Long>, ? extends Expression<Boolean>> predicateBuilder);
}

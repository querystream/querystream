
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.List;
import java.util.function.Function;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

/**
 * {@link SearchStream} containing {@link Double} values.
 */
public interface DoubleStream extends ExprStream<Double, Expression<Double>> {

// Aggregation

    DoubleValue average();

    DoubleValue max();

    DoubleValue min();

    DoubleValue sum();

// Narrowing overrides (SearchStream)

    @Override
    DoubleStream distinct();

    @Override
    DoubleStream orderBy(Ref<?, ? extends Expression<?>> ref, boolean asc);

    @Override
    DoubleStream orderBy(Function<? super Expression<Double>, ? extends Expression<?>> orderExprFunction, boolean asc);

    @Override
    DoubleStream orderByMulti(Function<? super Expression<Double>, ? extends List<? extends Order>> orderListFunction);

    @Override
    DoubleStream groupBy(Ref<?, ? extends Expression<?>> ref);

    @Override
    DoubleStream groupBy(Function<? super Expression<Double>, ? extends Expression<?>> groupFunction);

    @Override
    DoubleStream groupByMulti(Function<? super Expression<Double>, ? extends List<Expression<?>>> groupFunction);

    @Override
    DoubleStream having(Function<? super Expression<Double>, ? extends Expression<Boolean>> havingFunction);

    @Override
    DoubleValue findAny();

    @Override
    DoubleValue findFirst();

// Narrowing overrides (QueryStream)

    @Override
    DoubleStream bind(Ref<Double, ? super Expression<Double>> ref);

    @Override
    <X2, S2 extends Selection<X2>> DoubleStream bind(
      Ref<X2, ? super S2> ref, Function<? super Expression<Double>, ? extends S2> refFunction);

    @Override
    <R> DoubleStream addRoot(Ref<R, ? super Root<R>> ref, Class<R> type);

    @Override
    DoubleStream filter(Function<? super Expression<Double>, ? extends Expression<Boolean>> predicateBuilder);
}


/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
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

    DoubleStream asDoubleStream();

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
    LongStream orderBy(Order... orders);

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
    LongStream peek(Consumer<? super Expression<Long>> peeker);

    @Override
    <X2, S2 extends Selection<X2>> LongStream bind(
      Ref<X2, ? super S2> ref, Function<? super Expression<Long>, ? extends S2> refFunction);

    @Override
    <R> LongStream addRoot(Ref<R, ? super Root<R>> ref, Class<R> type);

    @Override
    LongStream filter(Function<? super Expression<Long>, ? extends Expression<Boolean>> predicateBuilder);

    @Override
    LongStream limit(int maxSize);

    @Override
    LongStream skip(int num);

    @Override
    LongStream withFlushMode(FlushModeType flushMode);

    @Override
    LongStream withLockMode(LockModeType lockMode);

    @Override
    LongStream withHint(String name, Object value);

    @Override
    LongStream withHints(Map<String, Object> hints);

    @Override
    LongStream withLoadGraph(String name);

    @Override
    LongStream withFetchGraph(String name);
}

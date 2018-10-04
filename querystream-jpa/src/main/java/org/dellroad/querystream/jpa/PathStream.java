
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
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

/**
 * {@link SearchStream} containing items representable as {@link Path}s.
 */
public interface PathStream<X, S extends Path<X>> extends ExprStream<X, S> {

    /**
     * Map this stream into a stream whose elements are the result of applying the given narrowing cast.
     *
     * @param type new, narrower item type
     * @param <Y> narrower type
     * @return recast stream
     * @see javax.persistence.criteria.CriteriaBuilder#treat(Path, Class) CriteriaBuilder.treat()
     */
    <Y extends X> PathStream<Y, ? extends Path<Y>> cast(Class<Y> type);

// Narrowing overrides (SearchStream)

    @Override
    PathStream<X, S> distinct();

    @Override
    PathStream<X, S> orderBy(Ref<?, ? extends Expression<?>> ref, boolean asc);

    @Override
    PathStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute, boolean asc);

    @Override
    PathStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2);

    @Override
    PathStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2, SingularAttribute<? super X, ?> attribute3, boolean asc3);

    @Override
    PathStream<X, S> orderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc);

    @Override
    PathStream<X, S> orderBy(Order... orders);

    @Override
    PathStream<X, S> orderByMulti(Function<? super S, ? extends List<? extends Order>> orderListFunction);

    @Override
    PathStream<X, S> groupBy(Ref<?, ? extends Expression<?>> ref);

    @Override
    PathStream<X, S> groupBy(SingularAttribute<? super X, ?> attribute);

    @Override
    PathStream<X, S> groupBy(Function<? super S, ? extends Expression<?>> groupFunction);

    @Override
    PathStream<X, S> groupByMulti(Function<? super S, ? extends List<Expression<?>>> groupFunction);

    @Override
    PathStream<X, S> having(Function<? super S, ? extends Expression<Boolean>> havingFunction);

    @Override
    PathValue<X, S> findAny();

    @Override
    PathValue<X, S> findFirst();

// Narrowing overrides (QueryStream)

    @Override
    PathStream<X, S> bind(Ref<X, ? super S> ref);

    @Override
    PathStream<X, S> peek(Consumer<? super S> peeker);

    @Override
    <X2, S2 extends Selection<X2>> PathStream<X, S> bind(Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction);

    @Override
    <R> PathStream<X, S> addRoot(Ref<R, ? super Root<R>> ref, Class<R> type);

    @Override
    PathStream<X, S> filter(SingularAttribute<? super X, Boolean> attribute);

    @Override
    PathStream<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder);

    @Override
    PathStream<X, S> limit(int maxSize);

    @Override
    PathStream<X, S> skip(int num);

    @Override
    PathStream<X, S> withFlushMode(FlushModeType flushMode);

    @Override
    PathStream<X, S> withLockMode(LockModeType lockMode);

    @Override
    PathStream<X, S> withHint(String name, Object value);

    @Override
    PathStream<X, S> withHints(Map<String, Object> hints);

    @Override
    PathStream<X, S> withLoadGraph(String name);

    @Override
    PathStream<X, S> withFetchGraph(String name);
}

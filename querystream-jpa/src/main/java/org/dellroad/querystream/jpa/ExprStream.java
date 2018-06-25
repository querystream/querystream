
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.SingularAttribute;

/**
 * {@link SearchStream} containing items representable as {@link Expression}s.
 */
public interface ExprStream<X, S extends Expression<X>> extends SearchStream<X, S> {

// Subqueries

    /**
     * Convert this instance into a subquery that can be used within an intermediate step of an outer query.
     *
     * @return criteria subquery object corresponding to this stream
     * @throws IllegalStateException if not invoked during a terminal operation on an outer query
     */
    Subquery<X> asSubquery();

    /**
     * Convert this instance into an "exists" subquery that can be used within an intermediate step of an outer query.
     *
     * @return boolean single-valued stream determining the existence of any items in this stream
     * @throws IllegalStateException if not invoked during a terminal operation on an outer query
     */
    Predicate exists();

// Aggregation

    /**
     * Create value returning the number of instances in this stream.
     *
     * <b>Warning:</b> don't use in combination with {@code groupBy()}, because SQL's {@code COUNT()} returns a non-unique
     * result in grouped queries, or else a {@link javax.persistence.NonUniqueResultException} can result.
     *
     * @return single-valued stream counting instances in this stream
     */
    LongValue count();

    /**
     * Create value returning the number of distinct instances in this stream.
     *
     * <b>Warning:</b> don't use in combination with {@code groupBy()}, because SQL's {@code COUNT()} returns a non-unique
     * result in grouped queries, or else a {@link javax.persistence.NonUniqueResultException} can result.
     *
     * @return single-valued stream counting distinct instances in this stream
     */
    LongValue countDistinct();

// Narrowing overrides (SearchStream)

    @Override
    ExprStream<X, S> distinct();

    @Override
    ExprStream<X, S> orderBy(Ref<?, ? extends Expression<?>> ref, boolean asc);

    @Override
    ExprStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute, boolean asc);

    @Override
    ExprStream<X, S> orderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc);

    @Override
    ExprStream<X, S> orderByMulti(Function<? super S, ? extends List<? extends Order>> orderListFunction);

    @Override
    ExprStream<X, S> groupBy(Ref<?, ? extends Expression<?>> ref);

    @Override
    ExprStream<X, S> groupBy(SingularAttribute<? super X, ?> attribute);

    @Override
    ExprStream<X, S> groupBy(Function<? super S, ? extends Expression<?>> groupFunction);

    @Override
    ExprStream<X, S> groupByMulti(Function<? super S, ? extends List<Expression<?>>> groupFunction);

    @Override
    ExprStream<X, S> having(Function<? super S, ? extends Expression<Boolean>> havingFunction);

    @Override
    ExprValue<X, S> findAny();

    @Override
    ExprValue<X, S> findFirst();

// Narrowing overrides (QueryStream)

    @Override
    ExprStream<X, S> bind(Ref<X, ? super S> ref);

    @Override
    ExprStream<X, S> peek(Consumer<? super S> peeker);

    @Override
    <X2, S2 extends Selection<X2>> ExprStream<X, S> bind(Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction);

    @Override
    <R> ExprStream<X, S> addRoot(Ref<R, ? super Root<R>> ref, Class<R> type);

    @Override
    ExprStream<X, S> filter(SingularAttribute<? super X, Boolean> attribute);

    @Override
    ExprStream<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder);
}

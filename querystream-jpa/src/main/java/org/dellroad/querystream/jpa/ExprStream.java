
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Parameter;
import jakarta.persistence.TemporalType;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

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
     * <p>
     * Note: to perform the equivalent of an "exists" operation on the outermost query, use {@link #isEmpty}.
     *
     * @return boolean single-valued stream determining the existence of any items in this stream
     * @throws IllegalStateException if invoked on a stream which is not being used as a subquery
     * @see #isEmpty
     */
    Predicate exists();

// Aggregation

    /**
     * Create value returning the number of instances in this stream.
     *
     * <p>
     * <b>Warning:</b> don't use in combination with {@code groupBy()}, because SQL's {@code COUNT()} returns a non-unique
     * result in grouped queries, or else a {@link NonUniqueResultException} can result.
     *
     * @return single-valued stream counting instances in this stream
     */
    LongValue count();

    /**
     * Create value returning the number of distinct instances in this stream.
     *
     * <p>
     * <b>Warning:</b> don't use in combination with {@code groupBy()}, because SQL's {@code COUNT()} returns a non-unique
     * result in grouped queries, or else a {@link NonUniqueResultException} can result.
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
    ExprStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2);

    @Override
    ExprStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2, SingularAttribute<? super X, ?> attribute3, boolean asc3);

    @Override
    ExprStream<X, S> orderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc);

    @Override
    ExprStream<X, S> orderBy(Order... orders);

    @Override
    ExprStream<X, S> orderByMulti(Function<? super S, ? extends List<? extends Order>> orderListFunction);

    @Override
    ExprStream<X, S> thenOrderBy(SingularAttribute<? super X, ?> attribute, boolean asc);

    @Override
    ExprStream<X, S> thenOrderBy(Ref<?, ? extends Expression<?>> ref, boolean asc);

    @Override
    ExprStream<X, S> thenOrderBy(Order... orders);

    @Override
    ExprStream<X, S> thenOrderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc);

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

    @Override
    ExprValue<X, S> findSingle();

    @Override
    ExprStream<X, S> fetch(SingularAttribute<? super X, ?> attribute);

    @Override
    ExprStream<X, S> fetch(SingularAttribute<? super X, ?> attribute, JoinType joinType);

    @Override
    ExprStream<X, S> fetch(PluralAttribute<? super X, ?, ?> attribute);

    @Override
    ExprStream<X, S> fetch(PluralAttribute<? super X, ?, ?> attribute, JoinType joinType);

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

    @Override
    ExprStream<X, S> limit(int maxSize);

    @Override
    ExprStream<X, S> skip(int num);

    @Override
    ExprStream<X, S> withFlushMode(FlushModeType flushMode);

    @Override
    ExprStream<X, S> withLockMode(LockModeType lockMode);

    @Override
    ExprStream<X, S> withHint(String name, Object value);

    @Override
    ExprStream<X, S> withHints(Map<String, Object> hints);

    @Override
    <T> ExprStream<X, S> withParam(Parameter<T> parameter, T value);

    @Override
    ExprStream<X, S> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType);

    @Override
    ExprStream<X, S> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType);

    @Override
    ExprStream<X, S> withParams(Iterable<? extends ParamBinding<?>> params);

    @Override
    ExprStream<X, S> withLoadGraph(String name);

    @Override
    ExprStream<X, S> withFetchGraph(String name);
}

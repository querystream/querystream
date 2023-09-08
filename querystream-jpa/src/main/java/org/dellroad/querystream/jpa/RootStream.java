
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Parameter;
import jakarta.persistence.TemporalType;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * {@link SearchStream} containing items representable as {@link Root}s.
 */
public interface RootStream<X> extends FromStream<X, Root<X>> {

// Narrowing overrides (PathStream)

    @Override
    <Y extends X> RootStream<Y> cast(Class<Y> type);

// Narrowing overrides (SearchStream)

    @Override
    RootStream<X> distinct();

    @Override
    RootStream<X> orderBy(Ref<?, ? extends Expression<?>> ref, boolean asc);

    @Override
    RootStream<X> orderBy(SingularAttribute<? super X, ?> attribute, boolean asc);

    @Override
    RootStream<X> orderBy(SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2);

    @Override
    RootStream<X> orderBy(SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2, SingularAttribute<? super X, ?> attribute3, boolean asc3);

    @Override
    RootStream<X> orderBy(Function<? super Root<X>, ? extends Expression<?>> orderExprFunction, boolean asc);

    @Override
    RootStream<X> orderBy(Order... orders);

    @Override
    RootStream<X> orderByMulti(Function<? super Root<X>, ? extends List<? extends Order>> orderListFunction);

    @Override
    RootStream<X> thenOrderBy(SingularAttribute<? super X, ?> attribute, boolean asc);

    @Override
    RootStream<X> thenOrderBy(Ref<?, ? extends Expression<?>> ref, boolean asc);

    @Override
    RootStream<X> thenOrderBy(Order... orders);

    @Override
    RootStream<X> thenOrderBy(Function<? super Root<X>, ? extends Expression<?>> orderExprFunction, boolean asc);

    @Override
    RootStream<X> groupBy(Ref<?, ? extends Expression<?>> ref);

    @Override
    RootStream<X> groupBy(SingularAttribute<? super X, ?> attribute);

    @Override
    RootStream<X> groupBy(Function<? super Root<X>, ? extends Expression<?>> groupFunction);

    @Override
    RootStream<X> groupByMulti(Function<? super Root<X>, ? extends List<Expression<?>>> groupFunction);

    @Override
    RootStream<X> having(Function<? super Root<X>, ? extends Expression<Boolean>> havingFunction);

    @Override
    RootValue<X> findAny();

    @Override
    RootValue<X> findFirst();

    @Override
    RootValue<X> findSingle();

// Narrowing overrides (QueryStream)

    @Override
    RootStream<X> bind(Ref<X, ? super Root<X>> ref);

    @Override
    RootStream<X> peek(Consumer<? super Root<X>> peeker);

    @Override
    <X2, S2 extends Selection<X2>> RootStream<X> bind(Ref<X2, ? super S2> ref, Function<? super Root<X>, ? extends S2> refFunction);

    @Override
    <R> RootStream<X> addRoot(Ref<R, ? super Root<R>> ref, Class<R> type);

    @Override
    RootStream<X> filter(SingularAttribute<? super X, Boolean> attribute);

    @Override
    RootStream<X> filter(Function<? super Root<X>, ? extends Expression<Boolean>> predicateBuilder);

    @Override
    RootStream<X> limit(int maxSize);

    @Override
    RootStream<X> skip(int num);

    @Override
    RootStream<X> withFlushMode(FlushModeType flushMode);

    @Override
    RootStream<X> withLockMode(LockModeType lockMode);

    @Override
    RootStream<X> withHint(String name, Object value);

    @Override
    RootStream<X> withHints(Map<String, Object> hints);

    @Override
    <T> RootStream<X> withParam(Parameter<T> parameter, T value);

    @Override
    RootStream<X> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType);

    @Override
    RootStream<X> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType);

    @Override
    RootStream<X> withParams(Iterable<? extends ParamBinding<?>> params);

    @Override
    RootStream<X> withLoadGraph(String name);

    @Override
    RootStream<X> withFetchGraph(String name);

    @Override
    RootStream<X> fetch(SingularAttribute<? super X, ?> attribute);

    @Override
    RootStream<X> fetch(SingularAttribute<? super X, ?> attribute, JoinType joinType);

    @Override
    RootStream<X> fetch(PluralAttribute<? super X, ?, ?> attribute);

    @Override
    RootStream<X> fetch(PluralAttribute<? super X, ?, ?> attribute, JoinType joinType);
}

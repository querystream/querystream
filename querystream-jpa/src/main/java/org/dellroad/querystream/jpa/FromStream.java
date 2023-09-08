
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Parameter;
import jakarta.persistence.TemporalType;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
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
 * {@link SearchStream} containing items representable as {@link From}s.
 */
public interface FromStream<X, S extends From<?, X>> extends PathStream<X, S> {

// Narrowing overrides (PathStream)

    // can't do this because there's no method CriteriaBuilder.treat(From, Class)
    // https://github.com/jakartaee/persistence/issues/190
    //@Override
    //<Y extends X> FromStream<Y, ? extends From<?, Y>> cast(Class<Y> type);

// Narrowing overrides (SearchStream)

    @Override
    FromStream<X, S> distinct();

    @Override
    FromStream<X, S> orderBy(Ref<?, ? extends Expression<?>> ref, boolean asc);

    @Override
    FromStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute, boolean asc);

    @Override
    FromStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2);

    @Override
    FromStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2, SingularAttribute<? super X, ?> attribute3, boolean asc3);

    @Override
    FromStream<X, S> orderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc);

    @Override
    FromStream<X, S> orderBy(Order... orders);

    @Override
    FromStream<X, S> orderByMulti(Function<? super S, ? extends List<? extends Order>> orderListFunction);

    @Override
    FromStream<X, S> thenOrderBy(SingularAttribute<? super X, ?> attribute, boolean asc);

    @Override
    FromStream<X, S> thenOrderBy(Ref<?, ? extends Expression<?>> ref, boolean asc);

    @Override
    FromStream<X, S> thenOrderBy(Order... orders);

    @Override
    FromStream<X, S> thenOrderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc);

    @Override
    FromStream<X, S> groupBy(Ref<?, ? extends Expression<?>> ref);

    @Override
    FromStream<X, S> groupBy(SingularAttribute<? super X, ?> attribute);

    @Override
    FromStream<X, S> groupBy(Function<? super S, ? extends Expression<?>> groupFunction);

    @Override
    FromStream<X, S> groupByMulti(Function<? super S, ? extends List<Expression<?>>> groupFunction);

    @Override
    FromStream<X, S> having(Function<? super S, ? extends Expression<Boolean>> havingFunction);

    @Override
    FromValue<X, S> findAny();

    @Override
    FromValue<X, S> findFirst();

    @Override
    FromValue<X, S> findSingle();

    @Override
    FromStream<X, S> fetch(SingularAttribute<? super X, ?> attribute);

    @Override
    FromStream<X, S> fetch(SingularAttribute<? super X, ?> attribute, JoinType joinType);

    @Override
    FromStream<X, S> fetch(PluralAttribute<? super X, ?, ?> attribute);

    @Override
    FromStream<X, S> fetch(PluralAttribute<? super X, ?, ?> attribute, JoinType joinType);

// Narrowing overrides (QueryStream)

    @Override
    FromStream<X, S> bind(Ref<X, ? super S> ref);

    @Override
    FromStream<X, S> peek(Consumer<? super S> peeker);

    @Override
    <X2, S2 extends Selection<X2>> FromStream<X, S> bind(Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction);

    @Override
    <R> FromStream<X, S> addRoot(Ref<R, ? super Root<R>> ref, Class<R> type);

    @Override
    FromStream<X, S> filter(SingularAttribute<? super X, Boolean> attribute);

    @Override
    FromStream<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder);

    @Override
    FromStream<X, S> limit(int maxSize);

    @Override
    FromStream<X, S> skip(int num);

    @Override
    FromStream<X, S> withFlushMode(FlushModeType flushMode);

    @Override
    FromStream<X, S> withLockMode(LockModeType lockMode);

    @Override
    FromStream<X, S> withHint(String name, Object value);

    @Override
    FromStream<X, S> withHints(Map<String, Object> hints);

    @Override
    <T> FromStream<X, S> withParam(Parameter<T> parameter, T value);

    @Override
    FromStream<X, S> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType);

    @Override
    FromStream<X, S> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType);

    @Override
    FromStream<X, S> withParams(Iterable<? extends ParamBinding<?>> params);

    @Override
    FromStream<X, S> withLoadGraph(String name);

    @Override
    FromStream<X, S> withFetchGraph(String name);
}

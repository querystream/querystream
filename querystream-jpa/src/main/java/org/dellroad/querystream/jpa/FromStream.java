
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.List;
import java.util.function.Function;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

/**
 * {@link SearchStream} containing items representable as {@link From}s.
 */
public interface FromStream<X, S extends From<?, X>> extends PathStream<X, S> {

// Narrowing overrides (SearchStream)

    @Override
    FromStream<X, S> distinct();

    @Override
    FromStream<X, S> orderBy(Ref<?, ? extends Expression<?>> ref, boolean asc);

    @Override
    FromStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute, boolean asc);

    @Override
    FromStream<X, S> orderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc);

    @Override
    FromStream<X, S> orderByMulti(Function<? super S, ? extends List<? extends Order>> orderListFunction);

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

// Narrowing overrides (QueryStream)

    @Override
    FromStream<X, S> bind(Ref<X, ? super S> ref);

    @Override
    <X2, S2 extends Selection<X2>> FromStream<X, S> bind(Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction);

    @Override
    <R> FromStream<X, S> addRoot(Ref<R, ? super Root<R>> ref, Class<R> type);

    @Override
    FromStream<X, S> filter(SingularAttribute<? super X, Boolean> attribute);

    @Override
    FromStream<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder);
}

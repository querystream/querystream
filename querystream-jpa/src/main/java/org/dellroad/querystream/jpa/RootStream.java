
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.List;
import java.util.function.Function;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

/**
 * {@link SearchStream} containing items representable as {@link Root}s.
 */
public interface RootStream<X> extends FromStream<X, Root<X>> {

// Narrowing overrides (SearchStream)

    @Override
    RootStream<X> distinct();

    @Override
    RootStream<X> orderBy(Ref<?, ? extends Expression<?>> ref, boolean asc);

    @Override
    RootStream<X> orderBy(SingularAttribute<? super X, ?> attribute, boolean asc);

    @Override
    RootStream<X> orderBy(Function<? super Root<X>, ? extends Expression<?>> orderExprFunction, boolean asc);

    @Override
    RootStream<X> orderByMulti(Function<? super Root<X>, ? extends List<? extends Order>> orderListFunction);

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

// Narrowing overrides (QueryStream)

    @Override
    RootStream<X> bind(Ref<X, ? super Root<X>> ref);

    @Override
    RootStream<X> filter(SingularAttribute<? super X, Boolean> attribute);

    @Override
    RootStream<X> filter(Function<? super Root<X>, ? extends Expression<Boolean>> predicateBuilder);
}


/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.List;
import java.util.function.Function;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.metamodel.SingularAttribute;

/**
 * {@link SearchStream} containing items representable as {@link Path}s.
 */
public interface PathStream<X, S extends Path<X>> extends ExprStream<X, S> {

// Narrowing overrides (SearchStream)

    @Override
    PathStream<X, S> distinct();

    @Override
    PathStream<X, S> orderBy(Ref<?, ? extends Expression<?>> ref, boolean asc);

    @Override
    PathStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute, boolean asc);

    @Override
    PathStream<X, S> orderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc);

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
    PathStream<X, S> filter(SingularAttribute<? super X, Boolean> attribute);

    @Override
    PathStream<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder);
}

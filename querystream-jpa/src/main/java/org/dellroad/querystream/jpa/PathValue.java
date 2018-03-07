
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.Function;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.metamodel.SingularAttribute;

/**
 * A {@link PathStream} that is guaranteed to return at most a single result.
 */
public interface PathValue<X, S extends Path<X>> extends ExprValue<X, S>, PathStream<X, S> {

// Narrowing overrides (QueryStream)

    @Override
    PathValue<X, S> bind(Ref<X, ? super S> ref);

    @Override
    PathValue<X, S> filter(SingularAttribute<? super X, Boolean> attribute);

    @Override
    PathValue<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder);
}

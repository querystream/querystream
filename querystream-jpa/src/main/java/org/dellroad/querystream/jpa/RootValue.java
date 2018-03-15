
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.Function;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

/**
 * A {@link RootStream} that is guaranteed to return at most a single result.
 */
public interface RootValue<X> extends FromValue<X, Root<X>>, RootStream<X> {

// Narrowing overrides (QueryStream)

    @Override
    RootValue<X> bind(Ref<X, ? super Root<X>> ref);

    @Override
    <X2, S2 extends Selection<X2>> RootValue<X> bind(Ref<X2, ? super S2> ref, Function<? super Root<X>, ? extends S2> refFunction);

    @Override
    RootValue<X> filter(SingularAttribute<? super X, Boolean> attribute);

    @Override
    RootValue<X> filter(Function<? super Root<X>, ? extends Expression<Boolean>> predicateBuilder);
}

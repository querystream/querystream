
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.Function;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Selection;

/**
 * A int {@link ExprValue}.
 */
public interface IntValue extends ExprValue<Integer, Expression<Integer>>, IntStream {

// Narrowing overrides (QueryStream)

    @Override
    IntValue bind(Ref<Integer, ? super Expression<Integer>> ref);

    @Override
    <X2, S2 extends Selection<X2>> IntValue bind(
      Ref<X2, ? super S2> ref, Function<? super Expression<Integer>, ? extends S2> refFunction);

    @Override
    IntValue filter(Function<? super Expression<Integer>, ? extends Expression<Boolean>> predicateBuilder);
}

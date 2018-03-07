
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.Function;

import javax.persistence.criteria.Expression;

/**
 * A int {@link ExprValue}.
 */
public interface IntValue extends ExprValue<Integer, Expression<Integer>>, IntStream {

// Narrowing overrides (QueryStream)

    @Override
    IntValue bind(Ref<Integer, ? super Expression<Integer>> ref);

    @Override
    IntValue filter(Function<? super Expression<Integer>, ? extends Expression<Boolean>> predicateBuilder);
}

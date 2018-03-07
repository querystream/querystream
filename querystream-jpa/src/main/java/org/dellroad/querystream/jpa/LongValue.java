
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.Function;

import javax.persistence.criteria.Expression;

/**
 * A long {@link ExprValue}.
 */
public interface LongValue extends ExprValue<Long, Expression<Long>>, LongStream {

// Narrowing overrides (QueryStream)

    @Override
    LongValue bind(Ref<Long, ? super Expression<Long>> ref);

    @Override
    LongValue filter(Function<? super Expression<Long>, ? extends Expression<Boolean>> predicateBuilder);
}

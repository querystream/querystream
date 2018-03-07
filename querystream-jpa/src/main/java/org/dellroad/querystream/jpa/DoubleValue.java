
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.Function;

import javax.persistence.criteria.Expression;

/**
 * A double {@link ExprValue}.
 */
public interface DoubleValue extends ExprValue<Double, Expression<Double>>, DoubleStream {

// Narrowing overrides (QueryStream)

    @Override
    DoubleValue bind(Ref<Double, ? super Expression<Double>> ref);

    @Override
    DoubleValue filter(Function<? super Expression<Double>, ? extends Expression<Boolean>> predicateBuilder);
}

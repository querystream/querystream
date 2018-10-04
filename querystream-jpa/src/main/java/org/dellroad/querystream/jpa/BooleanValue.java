
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.criteria.Expression;

/**
 * A boolean {@link ExprValue}.
 */
public interface BooleanValue extends ExprValue<Boolean, Expression<Boolean>> {

    /**
     * Invert this instance.
     *
     * @return inverse value
     */
    BooleanValue not();

// Narrowing overrides (QueryStream)

    @Override
    BooleanValue bind(Ref<Boolean, ? super Expression<Boolean>> ref);

    @Override
    BooleanValue peek(Consumer<? super Expression<Boolean>> peeker);

    @Override
    BooleanValue filter(Function<? super Expression<Boolean>, ? extends Expression<Boolean>> predicateBuilder);

    @Override
    BooleanValue withFlushMode(FlushModeType flushMode);

    @Override
    BooleanValue withLockMode(LockModeType lockMode);

    @Override
    BooleanValue withHint(String name, Object value);

    @Override
    BooleanValue withHints(Map<String, Object> hints);

    @Override
    BooleanValue withLoadGraph(String name);

    @Override
    BooleanValue withFetchGraph(String name);
}

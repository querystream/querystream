
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import jakarta.persistence.criteria.Expression;

/**
 * A {@link Ref} that's known to be an {@link Expression}.
 *
 * @param <X> stream item type
 */
public class ExprRef<X> extends AbstractRef<X, Expression<X>> {

    /**
     * Create an instance with no name.
     */
    public ExprRef() {
    }

    /**
     * Create an instance with the given name.
     *
     * <p>
     * The name is only used for debugging purposes.
     *
     * @param name reference name, or null for none
     */
    public ExprRef(String name) {
        super(name);
    }
}

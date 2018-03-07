
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import javax.persistence.criteria.From;

/**
 * A {@link Ref} that's known to be a {@link From}.
 *
 * @param <X> stream item type
 */
public class FromRef<X> extends AbstractRef<X, From<?, X>> {

    /**
     * Create an instance with no name.
     */
    public FromRef() {
    }

    /**
     * Create an instance with the given name.
     *
     * <p>
     * The name is only used for debugging purposes.
     *
     * @param name reference name, or null for none
     */
    public FromRef(String name) {
        super(name);
    }
}

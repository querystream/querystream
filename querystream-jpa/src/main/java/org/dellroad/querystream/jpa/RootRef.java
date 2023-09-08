
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import jakarta.persistence.criteria.Root;

/**
 * A {@link Ref} that's known to be a {@link Root}.
 *
 * @param <X> stream item type
 */
public class RootRef<X> extends AbstractRef<X, Root<X>> {

    /**
     * Create an instance with no name.
     */
    public RootRef() {
    }

    /**
     * Create an instance with the given name.
     *
     * <p>
     * The name is only used for debugging purposes.
     *
     * @param name reference name, or null for none
     */
    public RootRef(String name) {
        super(name);
    }
}

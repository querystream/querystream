
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import javax.persistence.criteria.Path;

/**
 * A {@link Ref} that's known to be a {@link Path}.
 *
 * @param <X> stream item type
 */
public class PathRef<X> extends AbstractRef<X, Path<X>> {

    /**
     * Create an instance with no name.
     */
    public PathRef() {
    }

    /**
     * Create an instance with the given name.
     *
     * <p>
     * The name is only used for debugging purposes.
     *
     * @param name reference name, or null for none
     */
    public PathRef(String name) {
        super(name);
    }
}

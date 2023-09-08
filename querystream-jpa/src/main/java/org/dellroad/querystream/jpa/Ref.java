
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import jakarta.persistence.criteria.Selection;

/**
 * A reference to items in a {@link QueryStream}.
 *
 * <p>
 * {@link Ref}s are created and then <b>bound</b> to the items in a {@link QueryStream}
 * via {@link QueryStream#bind QueryStream.bind()}. Once bound, they may be accessed
 * in a subsequent filter predicate, subquery {@link QueryStream}, etc., via {@link #get}.
 *
 * @param <X> stream item type
 * @param <S> criteria type for stream item
 */
public interface Ref<X, S extends Selection<X>> {

    /**
     * Bind the given value to this reference.
     *
     * @param value value to bind
     * @return same {@code value}
     * @throws IllegalStateException if another value is already bound
     * @throws IllegalArgumentException if {@code value} is null
     */
    S bind(S value);

    /**
     * Get the bound value.
     *
     * @return bound value
     * @throws IllegalStateException if this instance is not bound
     */
    S get();

    /**
     * Unbind the bound value, if any.
     */
    void unbind();

    /**
     * Determine if this instance is bound.
     *
     * @return true if this instance is bound, otherwise false
     */
    boolean isBound();
}

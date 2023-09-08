
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import jakarta.persistence.criteria.Selection;

class AbstractRef<X, S extends Selection<X>> implements Ref<X, S> {

    private final String name;

    private S value;

// Constructors

    protected AbstractRef() {
        this(null);
    }

    protected AbstractRef(String name) {
        this.name = name;
    }

// Ref

    @Override
    public S bind(S value) {
        if (value == null)
            throw new IllegalArgumentException("null value");
        if (this.value != null)
            throw new IllegalStateException(this + " is already bound (to " + this.value + ")");
        this.value = value;
        return value;
    }

    @Override
    public S get() {
        if (this.value == null)
            throw new IllegalStateException(this + " is not bound");
        return this.value;
    }

    @Override
    public void unbind() {
        this.value = null;
    }

    @Override
    public boolean isBound() {
        return this.value != null;
    }

// Object

    @Override
    public String toString() {
        if (this.name == null)
            return super.toString();
        return this.getClass().getSimpleName() + "[\"" + this.name + "\"]";
    }
}

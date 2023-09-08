
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import jakarta.persistence.Parameter;
import jakarta.persistence.TemporalType;

abstract class TemporalParamBinding<T> extends ParamBinding<T> {

    private final TemporalType temporalType;

    TemporalParamBinding(final Parameter<T> parameter, final T value, final TemporalType temporalType) {
        super(parameter, value);
        if (temporalType == null)
            throw new IllegalArgumentException("null temporalType");
        this.temporalType = temporalType;
    }

    /**
     * Get the {@link TemporalType}.
     *
     * @return temporal type to use when binding parameter value
     */
    public TemporalType getTemporalType() {
        return this.temporalType;
    }

// Object

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;
        final TemporalParamBinding<?> that = (TemporalParamBinding<?>)obj;
        return this.temporalType.equals(that.temporalType);
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ this.temporalType.hashCode();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()
          + "[parameter=" + ParamBinding.describeParameter(this.getParameter())
          + ",value=" + this.getValue()
          + ",type=" + this.getTemporalType()
          + "]";
    }
}

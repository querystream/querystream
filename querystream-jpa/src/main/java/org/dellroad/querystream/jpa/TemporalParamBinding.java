
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import javax.persistence.Parameter;
import javax.persistence.TemporalType;

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
}

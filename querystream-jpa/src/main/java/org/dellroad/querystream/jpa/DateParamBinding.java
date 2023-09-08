
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import jakarta.persistence.Parameter;
import jakarta.persistence.Query;
import jakarta.persistence.TemporalType;

import java.util.Date;

/**
 * Represents the binding of a {@link Date} value to a {@link Parameter}.
 *
 * @see Query#setParameter(Parameter, Date, TemporalType)
 */
public class DateParamBinding extends TemporalParamBinding<Date> {

    /**
     * Constructor.
     *
     * @param parameter the parameter to set
     * @param value parameter value
     * @param temporalType temporal type for {@code value}
     * @throws IllegalArgumentException if {@code parameter} or {@code temporalType} is null
     */
    public DateParamBinding(final Parameter<Date> parameter, final Date value, final TemporalType temporalType) {
        super(parameter, value, temporalType);
    }

    @Override
    void doApplyTo(Query query) {
        query.setParameter(this.getParameter(), this.getValue(), this.getTemporalType());
    }
}

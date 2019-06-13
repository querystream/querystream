
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Date;

import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 * Represents the binding of a {@link Date} value to a {@link Parameter}.
 *
 * @see Query#setParameter(javax.persistence.Parameter, Date, TemporalType)
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
    public DateParamBinding(final Parameter<Date> parameter, final Date date, final TemporalType temporalType) {
        super(parameter, date, temporalType);
    }

    @Override
    void doApplyTo(Query query) {
        query.setParameter(this.getParameter(), this.getValue(), this.getTemporalType());
    }
}

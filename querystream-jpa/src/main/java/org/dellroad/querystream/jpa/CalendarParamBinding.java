
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Calendar;

import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 * Represents the binding of a {@link Calendar} value to a {@link Parameter}.
 *
 * @see Query#setParameter(javax.persistence.Parameter, Calendar, TemporalType)
 */
public class CalendarParamBinding extends TemporalParamBinding<Calendar> {

    /**
     * Constructor.
     *
     * @param parameter the parameter to set
     * @param value parameter value
     * @param temporalType temporal type for {@code value}
     * @throws IllegalArgumentException if {@code parameter} or {@code temporalType} is null
     */
    public CalendarParamBinding(final Parameter<Calendar> parameter, final Calendar value, final TemporalType temporalType) {
        super(parameter, value, temporalType);
    }

    @Override
    void doApplyTo(Query query) {
        query.setParameter(this.getParameter(), this.getValue(), this.getTemporalType());
    }
}

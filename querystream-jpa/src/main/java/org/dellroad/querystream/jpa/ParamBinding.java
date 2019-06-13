
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import javax.persistence.Parameter;
import javax.persistence.Query;

/**
 * Represents the binding of a value to a {@link Parameter}.
 *
 * @see Query#setParameter(javax.persistence.Parameter, Object)
 */
public class ParamBinding<T> {

    private final Parameter<T> parameter;
    private final T value;

    /**
     * Constructor.
     *
     * @param parameter the parameter to set
     * @param value parameter value
     * @throws IllegalArgumentException if {@code parameter} is null
     */
    public ParamBinding(final Parameter<T> parameter, final T value) {
        if (parameter == null)
            throw new IllegalArgumentException("null parameter");
        this.parameter = parameter;
        this.value = value;
    }

    /**
     * Get the parameter.
     *
     * @return parameter to be bound
     */
    public Parameter<T> getParameter() {
        return this.parameter;
    }

    /**
     * Get the parameter value.
     *
     * @return value to be bound to parameter
     */
    public T getValue() {
        return this.value;
    }

    /**
     * Apply this parameter binding to the given query.
     *
     * @param query the query to configure
     * @throws IllegalArgumentException if {@code query} is null
     */
    public void applyTo(Query query) {
        if (query == null)
            throw new IllegalArgumentException("null query");
        this.doApplyTo(query);
    }

    void doApplyTo(Query query) {
        query.setParameter(this.getParameter(), this.getValue());
    }
}

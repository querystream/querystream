
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa.querytype;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

/**
 * Represents the JPA Criteria API bulk update query type.
 *
 * @param <X> query target type
 */
public class UpdateType<X> extends QueryType<X, CriteriaUpdate<X>, CriteriaUpdate<X>, Query> {

    /**
     * Constructor.
     *
     * @param type query result type
     * @throws IllegalArgumentException if {@code type} is null
     */
    public UpdateType(Class<X> type) {
        super(type);
    }

    @Override
    public CriteriaUpdate<X> createCriteriaQuery(CriteriaBuilder builder) {
        if (builder == null)
            throw new IllegalArgumentException("null builder");
        return builder.createCriteriaUpdate(this.type);
    }

    @Override
    public Query createQuery(EntityManager entityManager, CriteriaUpdate<X> query) {
        if (entityManager == null)
            throw new IllegalArgumentException("null entityManager");
        if (query == null)
            throw new IllegalArgumentException("null query");
        return entityManager.createQuery(query);
    }

    @Override
    public void where(CriteriaUpdate<X> query, Expression<Boolean> restriction) {
        if (query == null)
            throw new IllegalArgumentException("null query");
        if (restriction == null)
            throw new IllegalArgumentException("null restriction");
        query.where(restriction);
    }

    @Override
    public void where(CriteriaUpdate<X> query, Predicate restriction) {
        if (query == null)
            throw new IllegalArgumentException("null query");
        if (restriction == null)
            throw new IllegalArgumentException("null restriction");
        query.where(restriction);
    }
}

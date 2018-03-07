
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa.querytype;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

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
        return builder.createCriteriaUpdate(this.type);
    }

    @Override
    public Query createQuery(EntityManager entityManager, CriteriaUpdate<X> query) {
        return entityManager.createQuery(query);
    }

    @Override
    public void where(CriteriaUpdate<X> query, Expression<Boolean> restriction) {
        query.where(restriction);
    }

    @Override
    public void where(CriteriaUpdate<X> query, Predicate restriction) {
        query.where(restriction);
    }
}

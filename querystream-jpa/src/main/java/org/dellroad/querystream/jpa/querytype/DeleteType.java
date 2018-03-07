
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa.querytype;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

/**
 * Represents the JPA Criteria API bulk delete query type.
 *
 * @param <X> query target type
 */
public class DeleteType<X> extends QueryType<X, CriteriaDelete<X>, CriteriaDelete<X>, Query> {

    /**
     * Constructor.
     *
     * @param type query result type
     * @throws IllegalArgumentException if {@code type} is null
     */
    public DeleteType(Class<X> type) {
        super(type);
    }

    @Override
    public CriteriaDelete<X> createCriteriaQuery(CriteriaBuilder builder) {
        return builder.createCriteriaDelete(this.type);
    }

    @Override
    public Query createQuery(EntityManager entityManager, CriteriaDelete<X> query) {
        return entityManager.createQuery(query);
    }

    @Override
    public void where(CriteriaDelete<X> query, Expression<Boolean> restriction) {
        query.where(restriction);
    }

    @Override
    public void where(CriteriaDelete<X> query, Predicate restriction) {
        query.where(restriction);
    }
}

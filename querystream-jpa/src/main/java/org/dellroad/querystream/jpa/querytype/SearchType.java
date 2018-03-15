
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa.querytype;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Selection;

/**
 * Represents the JPA Criteria API search query type.
 *
 * @param <X> query result type
 */
public class SearchType<X> extends QueryType<X, AbstractQuery<?>, CriteriaQuery<X>, TypedQuery<X>> {

    /**
     * Constructor.
     *
     * @param type query result type
     * @throws IllegalArgumentException if {@code type} is null
     */
    public SearchType(Class<X> type) {
        super(type);
    }

    @Override
    public CriteriaQuery<X> createCriteriaQuery(CriteriaBuilder builder) {
        if (builder == null)
            throw new IllegalArgumentException("null builder");
        return builder.createQuery(this.type);
    }

    @Override
    public TypedQuery<X> createQuery(EntityManager entityManager, CriteriaQuery<X> query) {
        if (entityManager == null)
            throw new IllegalArgumentException("null entityManager");
        if (query == null)
            throw new IllegalArgumentException("null query");
        return entityManager.createQuery(query);
    }

    @Override
    public void where(AbstractQuery<?> query, Expression<Boolean> restriction) {
        if (query == null)
            throw new IllegalArgumentException("null query");
        if (restriction == null)
            throw new IllegalArgumentException("null restriction");
        query.where(restriction);
    }

    @Override
    public void where(AbstractQuery<?> query, Predicate restriction) {
        if (query == null)
            throw new IllegalArgumentException("null query");
        if (restriction == null)
            throw new IllegalArgumentException("null restriction");
        query.where(restriction);
    }

    /**
     * Configure the result expression associated with a query.
     *
     * @param query criteria query object
     * @param expression query result
     * @return updated criteria query object
     */
    public CriteriaQuery<X> select(CriteriaQuery<X> query, Selection<X> expression) {
        return query.select(expression);
    }
}

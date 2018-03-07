
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa.querytype;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CommonAbstractCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

/**
 * Instances represent one of the three types of JPA criteria API queries: {@linkplain SearchType search},
 * {@linkplain UpdateType update}, or {@linkplain DeleteType delete}, for a specific target type.
 *
 * @param <X> query result/target type
 * @param <C> configured criteria API query type
 * @param <C2> final criteria API query type
 * @param <Q> final JPA query type
 */
public abstract class QueryType<X, C extends CommonAbstractCriteria, C2 extends C, Q extends Query> {

    protected final Class<X> type;

    /**
     * Constructor.
     *
     * @param type query result/target type
     * @throws IllegalArgumentException if {@code type} is null
     */
    protected QueryType(Class<X> type) {
        if (type == null)
            throw new IllegalArgumentException("null type");
        this.type = type;
    }

    /**
     * Get the query result/target type associated with this instance.
     *
     * @return query result/target type
     */
    public Class<X> getType() {
        return this.type;
    }

    /**
     * Create a new Criteria API query object of the appropriate type.
     *
     * @param builder criteria builder
     * @return new criteria query object
     * @throws IllegalArgumentException if {@code builder} is null
     */
    public abstract C2 createCriteriaQuery(CriteriaBuilder builder);

    /**
     * Create a JPA query object of the appropriate type.
     *
     * @param entityManager JPA entity manager
     * @param query criteria query object
     * @return new executable JPA query object
     * @throws IllegalArgumentException if {@code entityManager} or {@code query} is null
     */
    public abstract Q createQuery(EntityManager entityManager, C2 query);

    /**
     * Configure the restriction associated with the given query.
     *
     * @param query criteria query object
     * @param restriction query restriction
     * @throws IllegalArgumentException if {@code query} or {@code expression} is null
     */
    public abstract void where(C query, Expression<Boolean> restriction);

    /**
     * Configure the restriction associated with the given query.
     *
     * @param query criteria query object
     * @param restriction query restriction
     * @throws IllegalArgumentException if {@code query} or {@code expression} is null
     */
    public abstract void where(C query, Predicate restriction);
}


/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import javax.persistence.criteria.CommonAbstractCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Subquery;

/**
 * Holds the current Criteria API query object under construction while a stream is being realized by
 * {@link QueryStream#toCriteriaQuery}.
 *
 * <p>
 * Because we can create {@link Subquery} objects, during construction there is actually a stack of
 * {@link CurrentQuery} objects maintained. The first stack entry contains a {@link CommonAbstractCriteria}
 * appropriate for the query being constructed, while all other stack entries contain {@link Subquery} objects.
 *
 * <p>
 * For details, see {@link QueryStreamImpl#withCurrentQuery} and {@link ExprStreamImpl#asSubquery}.
 */
class CurrentQuery {

    private final CriteriaBuilder builder;
    private final CommonAbstractCriteria query;

    CurrentQuery(CriteriaBuilder builder, CommonAbstractCriteria query) {
        if (builder == null)
            throw new IllegalArgumentException("null builder");
        if (query == null)
            throw new IllegalArgumentException("null query");
        this.builder = builder;
        this.query = query;
    }

    public CriteriaBuilder getBuilder() {
        return this.builder;
    }

    public CommonAbstractCriteria getQuery() {
        return this.query;
    }

    public Subquery<?> getSubquery() {
        try {
            return (Subquery<?>)this.query;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("streams built with QueryBuilder.substream() can only be used in subqueries");
        }
    }
}

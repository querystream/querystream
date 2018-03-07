
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import javax.persistence.criteria.CommonAbstractCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Selection;

/**
 * Configures a critiera query of some kind.
 *
 * @param <C> criteria query type
 * @param <X> target/result type
 * @param <S> target/result criteria type
 */
@FunctionalInterface
public interface QueryConfigurer<C extends CommonAbstractCriteria, X, S extends Selection<X>> {

    /**
     * Configure the given query.
     *
     * @param builder criteria builder
     * @param query the query to configure
     * @return selection for query target/result
     */
    S configure(CriteriaBuilder builder, C query);
}

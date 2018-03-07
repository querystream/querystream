
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

class SearchValueImpl<X, S extends Selection<X>> extends SearchStreamImpl<X, S> implements SearchValue<X, S> {

// Constructors

    SearchValueImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer) {
        super(entityManager, queryType, configurer);
    }

// Subclass required methods

    @Override
    SearchValue<X, S> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer) {
        return new SearchValueImpl<>(entityManager, queryType, configurer);
    }

// Narrowing overrides (QueryStream)

    @Override
    public SearchValue<X, S> bind(Ref<X, ? super S> ref) {
        return (SearchValue<X, S>)super.bind(ref);
    }

    @Override
    public SearchValue<X, S> filter(SingularAttribute<? super X, Boolean> attribute) {
        return (SearchValue<X, S>)super.filter(attribute);
    }

    @Override
    public SearchValue<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder) {
        return (SearchValue<X, S>)super.filter(predicateBuilder);
    }
}

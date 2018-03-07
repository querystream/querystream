
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

class PathValueImpl<X, S extends Path<X>> extends PathStreamImpl<X, S> implements PathValue<X, S> {

// Constructors

    PathValueImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer) {
        super(entityManager, queryType, configurer);
    }

// Subclass required methods

    @Override
    PathValue<X, S> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer) {
        return new PathValueImpl<>(entityManager, queryType, configurer);
    }

// Narrowing overrides (QueryStream)

    @Override
    public PathValue<X, S> bind(Ref<X, ? super S> ref) {
        return (PathValue<X, S>)super.bind(ref);
    }

    @Override
    public PathValue<X, S> filter(SingularAttribute<? super X, Boolean> attribute) {
        return (PathValue<X, S>)super.filter(attribute);
    }

    @Override
    public PathValue<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder) {
        return (PathValue<X, S>)super.filter(predicateBuilder);
    }
}

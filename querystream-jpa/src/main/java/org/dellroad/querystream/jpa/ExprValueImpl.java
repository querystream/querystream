
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

class ExprValueImpl<X, S extends Expression<X>> extends ExprStreamImpl<X, S> implements ExprValue<X, S> {

// Constructors

    ExprValueImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer) {
        super(entityManager, queryType, configurer);
    }

// Subclass required methods

    @Override
    ExprValue<X, S> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer) {
        return new ExprValueImpl<>(entityManager, queryType, configurer);
    }

// Narrowing overrides (QueryStream)

    @Override
    public ExprValue<X, S> bind(Ref<X, ? super S> ref) {
        return (ExprValue<X, S>)super.bind(ref);
    }

    @Override
    public ExprValue<X, S> filter(SingularAttribute<? super X, Boolean> attribute) {
        return (ExprValue<X, S>)super.filter(attribute);
    }

    @Override
    public ExprValue<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder) {
        return (ExprValue<X, S>)super.filter(predicateBuilder);
    }
}

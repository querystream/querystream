
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

class FromValueImpl<X, S extends From<?, X>> extends FromStreamImpl<X, S> implements FromValue<X, S> {

// Constructors

    FromValueImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer) {
        super(entityManager, queryType, configurer);
    }

// Subclass required methods

    @Override
    FromValue<X, S> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer) {
        return new FromValueImpl<>(entityManager, queryType, configurer);
    }

// Narrowing overrides (QueryStream)

    @Override
    public FromValue<X, S> bind(Ref<X, ? super S> ref) {
        return (FromValue<X, S>)super.bind(ref);
    }

    @Override
    public <X2, S2 extends Selection<X2>> FromValue<X, S> bind(
      Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction) {
        return (FromValue<X, S>)super.bind(ref, refFunction);
    }

    @Override
    public FromValue<X, S> filter(SingularAttribute<? super X, Boolean> attribute) {
        return (FromValue<X, S>)super.filter(attribute);
    }

    @Override
    public FromValue<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder) {
        return (FromValue<X, S>)super.filter(predicateBuilder);
    }
}

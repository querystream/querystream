
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

class RootValueImpl<X> extends RootStreamImpl<X> implements RootValue<X> {

// Constructors

    RootValueImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends Root<X>> configurer) {
        super(entityManager, queryType, configurer);
    }

// Subclass required methods

    @Override
    RootValue<X> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends Root<X>> configurer) {
        return new RootValueImpl<>(entityManager, queryType, configurer);
    }

// Narrowing overrides (QueryStream)

    @Override
    public RootValue<X> bind(Ref<X, ? super Root<X>> ref) {
        return (RootValue<X>)super.bind(ref);
    }

    @Override
    public RootValue<X> peek(Consumer<? super Root<X>> peeker) {
        return (RootValue<X>)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> RootValue<X> bind(
      Ref<X2, ? super S2> ref, Function<? super Root<X>, ? extends S2> refFunction) {
        return (RootValue<X>)super.bind(ref, refFunction);
    }

    @Override
    public RootValue<X> filter(SingularAttribute<? super X, Boolean> attribute) {
        return (RootValue<X>)super.filter(attribute);
    }

    @Override
    public RootValue<X> filter(Function<? super Root<X>, ? extends Expression<Boolean>> predicateBuilder) {
        return (RootValue<X>)super.filter(predicateBuilder);
    }
}

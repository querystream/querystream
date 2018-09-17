
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

class IntValueImpl extends IntStreamImpl implements IntValue {

// Constructors

    IntValueImpl(EntityManager entityManager,
      QueryConfigurer<AbstractQuery<?>, Integer, ? extends Expression<Integer>> configurer, int firstResult, int maxResults) {
        super(entityManager, configurer, firstResult, maxResults);
    }

// Subclass required methods

    @Override
    IntValue create(EntityManager entityManager, SearchType<Integer> queryType,
      QueryConfigurer<AbstractQuery<?>, Integer, ? extends Expression<Integer>> configurer, int firstResult, int maxResults) {
        return new IntValueImpl(entityManager, configurer, firstResult, maxResults);
    }

// Narrowing overrides (QueryStream)

    @Override
    public IntValue bind(Ref<Integer, ? super Expression<Integer>> ref) {
        return (IntValue)super.bind(ref);
    }

    @Override
    public IntValue peek(Consumer<? super Expression<Integer>> peeker) {
        return (IntValue)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> IntValue bind(
      Ref<X2, ? super S2> ref, Function<? super Expression<Integer>, ? extends S2> refFunction) {
        return (IntValue)super.bind(ref, refFunction);
    }

    @Override
    public IntValue filter(SingularAttribute<? super Integer, Boolean> attribute) {         // makes no sense but needed for API
        return (IntValue)super.filter(attribute);
    }

    @Override
    public IntValue filter(Function<? super Expression<Integer>, ? extends Expression<Boolean>> predicateBuilder) {
        return (IntValue)super.filter(predicateBuilder);
    }
}

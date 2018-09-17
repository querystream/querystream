
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

class ExprValueImpl<X, S extends Expression<X>> extends ExprStreamImpl<X, S> implements ExprValue<X, S> {

// Constructors

    ExprValueImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, int firstResult, int maxResults) {
        super(entityManager, queryType, configurer, firstResult, maxResults);
    }

// Subclass required methods

    @Override
    ExprValue<X, S> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, int firstResult, int maxResults) {
        return new ExprValueImpl<>(entityManager, queryType, configurer, firstResult, maxResults);
    }

// Narrowing overrides (QueryStream)

    @Override
    public ExprValue<X, S> bind(Ref<X, ? super S> ref) {
        return (ExprValue<X, S>)super.bind(ref);
    }

    @Override
    public ExprValue<X, S> peek(Consumer<? super S> peeker) {
        return (ExprValue<X, S>)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> ExprValue<X, S> bind(
      Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction) {
        return (ExprValue<X, S>)super.bind(ref, refFunction);
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


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

class LongValueImpl extends LongStreamImpl implements LongValue {

// Constructors

    LongValueImpl(EntityManager entityManager,
      QueryConfigurer<AbstractQuery<?>, Long, ? extends Expression<Long>> configurer, int firstResult, int maxResults) {
        super(entityManager, configurer, firstResult, maxResults);
    }

// Subclass required methods

    @Override
    LongValue create(EntityManager entityManager, SearchType<Long> queryType,
      QueryConfigurer<AbstractQuery<?>, Long, ? extends Expression<Long>> configurer, int firstResult, int maxResults) {
        return new LongValueImpl(entityManager, configurer, firstResult, maxResults);
    }

// Narrowing overrides (QueryStream)

    @Override
    public LongValue bind(Ref<Long, ? super Expression<Long>> ref) {
        return (LongValue)super.bind(ref);
    }

    @Override
    public LongValue peek(Consumer<? super Expression<Long>> peeker) {
        return (LongValue)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> LongValue bind(
      Ref<X2, ? super S2> ref, Function<? super Expression<Long>, ? extends S2> refFunction) {
        return (LongValue)super.bind(ref, refFunction);
    }

    @Override
    public LongValue filter(SingularAttribute<? super Long, Boolean> attribute) {           // makes no sense but needed for API
        return (LongValue)super.filter(attribute);
    }

    @Override
    public LongValue filter(Function<? super Expression<Long>, ? extends Expression<Boolean>> predicateBuilder) {
        return (LongValue)super.filter(predicateBuilder);
    }
}

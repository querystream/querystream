
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.Expression;

import org.dellroad.querystream.jpa.querytype.SearchType;

class BooleanValueImpl extends ExprValueImpl<Boolean, Expression<Boolean>> implements BooleanValue {

// Constructors

    BooleanValueImpl(EntityManager entityManager,
      QueryConfigurer<AbstractQuery<?>, Boolean, ? extends Expression<Boolean>> configurer) {
        super(entityManager, new SearchType<>(Boolean.class), configurer);
    }

// BooleanValue

    @Override
    public BooleanValue not() {
        return new BooleanValueImpl(this.entityManager, (builder, query) -> builder.not(this.configurer.configure(builder, query)));
    }

// Subclass required methods

    @Override
    BooleanValue create(EntityManager entityManager, SearchType<Boolean> queryType,
      QueryConfigurer<AbstractQuery<?>, Boolean, ? extends Expression<Boolean>> configurer) {
        return new BooleanValueImpl(entityManager, configurer);
    }

// Narrowing overrides (QueryStream)

    @Override
    public BooleanValue bind(Ref<Boolean, ? super Expression<Boolean>> ref) {
        return (BooleanValue)super.bind(ref);
    }

    @Override
    public BooleanValue peek(Consumer<? super Expression<Boolean>> peeker) {
        return (BooleanValue)super.peek(peeker);
    }

    @Override
    public BooleanValue filter(Function<? super Expression<Boolean>, ? extends Expression<Boolean>> predicateBuilder) {
        return (BooleanValue)super.filter(predicateBuilder);
    }
}

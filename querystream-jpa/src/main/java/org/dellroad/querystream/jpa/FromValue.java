
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

/**
 * A {@link FromStream} that is guaranteed to return at most a single result.
 */
public interface FromValue<X, S extends From<?, X>> extends PathValue<X, S>, FromStream<X, S> {

// Narrowing overrides (FromStream)

    @Override
    default <Y> FromValue<Y, From<X, Y>> join(SingularAttribute<? super X, Y> attribute) {
        return this.join(attribute, JoinType.INNER);
    }

    @Override
    default <Y> FromValue<Y, From<X, Y>> join(SingularAttribute<? super X, Y> attribute, JoinType joinType) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        if (!attribute.isAssociation())
            throw new IllegalArgumentException("attribute is not an association: " + attribute);
        if (joinType == null)
            throw new IllegalArgumentException("null joinType");
        QueryStreamImpl.checkOffsetLimit(this, "join() must be performed prior to skip() or limit()");
        return new FromValueImpl<>(this.getEntityManager(), new SearchType<>(attribute.getJavaType()),
           (builder, query) -> this.configure(builder, query).join(attribute, joinType), -1, -1);
    }

// Narrowing overrides (QueryStream)

    @Override
    FromValue<X, S> bind(Ref<X, ? super S> ref);

    @Override
    FromValue<X, S> peek(Consumer<? super S> peeker);

    @Override
    <X2, S2 extends Selection<X2>> FromValue<X, S> bind(Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction);

    @Override
    FromValue<X, S> filter(SingularAttribute<? super X, Boolean> attribute);

    @Override
    FromValue<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder);
}

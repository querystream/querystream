
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.DeleteType;

/**
 * Builder for JPA criteria bulk delete queries using a {@link java.util.stream.Stream}-like API.
 */
class DeleteStreamImpl<X>
  extends QueryStreamImpl<X, Root<X>, CriteriaDelete<X>, CriteriaDelete<X>, Query, DeleteType<X>>
  implements DeleteStream<X> {

    DeleteStreamImpl(EntityManager entityManager, Class<X> type) {
        this(entityManager, new DeleteType<X>(type));
    }

    private DeleteStreamImpl(EntityManager entityManager, DeleteType<X> queryType) {
        this(entityManager, queryType, (builder, query) -> query.from(queryType.getType()));
    }

    private DeleteStreamImpl(EntityManager entityManager, DeleteType<X> queryType,
      QueryConfigurer<CriteriaDelete<X>, X, ? extends Root<X>> configurer) {
        super(entityManager, queryType, configurer);
    }

// DeleteStream

    @Override
    public int delete() {
        return this.toQuery().executeUpdate();
    }

// Subclass required methods

    @Override
    DeleteStream<X> create(EntityManager entityManager, DeleteType<X> queryType,
      QueryConfigurer<CriteriaDelete<X>, X, ? extends Root<X>> configurer) {
        return new DeleteStreamImpl<>(entityManager, queryType, configurer);
    }

    @Override
    CriteriaDelete<X> select(CriteriaDelete<X> query, Root<X> selection) {
        return query;
    }

// Narrowing overrides (QueryStreamImpl)

    @Override
    public DeleteStream<X> bind(Ref<X, ? super Root<X>> ref) {
        return (DeleteStream<X>)super.bind(ref);
    }

    @Override
    public DeleteStream<X> filter(SingularAttribute<? super X, Boolean> attribute) {
        return (DeleteStream<X>)super.filter(attribute);
    }

    @Override
    public DeleteStream<X> filter(Function<? super Root<X>, ? extends Expression<Boolean>> predicateBuilder) {
        return (DeleteStream<X>)super.filter(predicateBuilder);
    }
}

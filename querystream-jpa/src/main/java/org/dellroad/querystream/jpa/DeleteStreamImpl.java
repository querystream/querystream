
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.Consumer;
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
        this(entityManager, queryType, (builder, query) -> query.from(queryType.getType()), -1, -1);
    }

    private DeleteStreamImpl(EntityManager entityManager, DeleteType<X> queryType,
      QueryConfigurer<CriteriaDelete<X>, X, ? extends Root<X>> configurer, int firstResult, int maxResults) {
        super(entityManager, queryType, configurer, firstResult, maxResults);
    }

// DeleteStream

    @Override
    public int delete() {
        return this.toQuery().executeUpdate();
    }

// Subclass required methods

    @Override
    DeleteStream<X> create(EntityManager entityManager, DeleteType<X> queryType,
      QueryConfigurer<CriteriaDelete<X>, X, ? extends Root<X>> configurer, int firstResult, int maxResults) {
        return new DeleteStreamImpl<>(entityManager, queryType, configurer, firstResult, maxResults);
    }

    @Override
    CriteriaDelete<X> select(CriteriaDelete<X> query, Root<X> selection) {
        return query;
    }

// Narrowing overrides (QueryStreamImpl)

    @Override
    public DeleteType<X> getQueryType() {
        return this.queryType;
    }

    @Override
    public DeleteStream<X> bind(Ref<X, ? super Root<X>> ref) {
        return (DeleteStream<X>)super.bind(ref);
    }

    @Override
    public DeleteStream<X> peek(Consumer<? super Root<X>> peeker) {
        return (DeleteStream<X>)super.peek(peeker);
    }

    @Override
    public DeleteStream<X> filter(SingularAttribute<? super X, Boolean> attribute) {
        return (DeleteStream<X>)super.filter(attribute);
    }

    @Override
    public DeleteStream<X> filter(Function<? super Root<X>, ? extends Expression<Boolean>> predicateBuilder) {
        return (DeleteStream<X>)super.filter(predicateBuilder);
    }

    @Override
    public DeleteStream<X> limit(int limit) {
        return (DeleteStream<X>)super.limit(limit);
    }

    @Override
    public DeleteStream<X> skip(int skip) {
        return (DeleteStream<X>)super.skip(skip);
    }
}

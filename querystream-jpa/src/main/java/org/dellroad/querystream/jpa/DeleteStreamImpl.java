
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
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

    // Separate constructor to avoid bogus error ("cannot reference queryType before supertype constructor has been called")
    private DeleteStreamImpl(EntityManager entityManager, DeleteType<X> queryType) {
        this(entityManager, queryType, (builder, query) -> query.from(queryType.getType()), new QueryInfo());
    }

    private DeleteStreamImpl(EntityManager entityManager, DeleteType<X> queryType,
      QueryConfigurer<CriteriaDelete<X>, X, ? extends Root<X>> configurer, QueryInfo queryInfo) {
        super(entityManager, queryType, configurer, queryInfo);
    }

// DeleteStream

    @Override
    public int delete() {
        return this.toQuery().executeUpdate();
    }

// Subclass required methods

    @Override
    DeleteStream<X> create(EntityManager entityManager, DeleteType<X> queryType,
      QueryConfigurer<CriteriaDelete<X>, X, ? extends Root<X>> configurer, QueryInfo queryInfo) {
        return new DeleteStreamImpl<>(entityManager, queryType, configurer, queryInfo);
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
    public <X2, S2 extends Selection<X2>> DeleteStream<X> bind(
      Ref<X2, ? super S2> ref, Function<? super Root<X>, ? extends S2> refFunction) {
        return (DeleteStream<X>)super.bind(ref, refFunction);
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

    @Override
    public DeleteStream<X> withFlushMode(FlushModeType flushMode) {
        return (DeleteStream<X>)super.withFlushMode(flushMode);
    }

    @Override
    public DeleteStream<X> withLockMode(LockModeType lockMode) {
        return (DeleteStream<X>)super.withLockMode(lockMode);
    }

    @Override
    public DeleteStream<X> withHint(String name, Object value) {
        return (DeleteStream<X>)super.withHint(name, value);
    }

    @Override
    public DeleteStream<X> withHints(Map<String, Object> hints) {
        return (DeleteStream<X>)super.withHints(hints);
    }

    @Override
    public <T> DeleteStream<X> withParam(Parameter<T> parameter, T value) {
        return (DeleteStream<X>)super.withParam(parameter, value);
    }

    @Override
    public DeleteStream<X> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType) {
        return (DeleteStream<X>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public DeleteStream<X> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType) {
        return (DeleteStream<X>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public DeleteStream<X> withParams(Iterable<? extends ParamBinding<?>> params) {
        return (DeleteStream<X>)super.withParams(params);
    }

    @Override
    public DeleteStream<X> withLoadGraph(String name) {
        return (DeleteStream<X>)super.withLoadGraph(name);
    }

    @Override
    public DeleteStream<X> withFetchGraph(String name) {
        return (DeleteStream<X>)super.withFetchGraph(name);
    }
}

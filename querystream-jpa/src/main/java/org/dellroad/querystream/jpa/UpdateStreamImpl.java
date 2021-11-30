
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.UpdateType;

class UpdateStreamImpl<X>
  extends QueryStreamImpl<X, Root<X>, CriteriaUpdate<X>, CriteriaUpdate<X>, Query, UpdateType<X>>
  implements UpdateStream<X> {

// Constructors

    UpdateStreamImpl(EntityManager entityManager, Class<X> type) {
        this(entityManager, new UpdateType<X>(type));
    }

    // Separate constructor to avoid bogus error ("cannot reference queryType before supertype constructor has been called")
    private UpdateStreamImpl(EntityManager entityManager, UpdateType<X> queryType) {
        super(entityManager, queryType, (builder, query) -> query.from(queryType.getType()), new QueryInfo());
    }

    private UpdateStreamImpl(EntityManager entityManager, UpdateType<X> queryType,
      QueryConfigurer<CriteriaUpdate<X>, X, ? extends Root<X>> configurer, QueryInfo queryInfo) {
        super(entityManager, queryType, configurer, queryInfo);
    }

// UpdateStream

    @Override
    public int update() {
        return this.toQuery().executeUpdate();
    }

    @Override
    public <Y> UpdateStream<X> set(Path<Y> path, Expression<? extends Y> value) {
        if (path == null)
            throw new IllegalArgumentException("null path");
        if (value == null)
            throw new IllegalArgumentException("null value expression");
        QueryStreamImpl.checkOffsetLimit(this, "set()");
        return this.modQuery((query, selection) -> query.set(path, value));
    }

    @Override
    public <Y, V extends Y> UpdateStream<X> set(Path<Y> path, V value) {
        if (path == null)
            throw new IllegalArgumentException("null path");
        QueryStreamImpl.checkOffsetLimit(this, "set()");
        return this.modQuery((query, selection) -> query.set(path, value));
    }

    @Override
    public <Y> UpdateStream<X> set(SingularAttribute<? super X, Y> attribute, Expression<? extends Y> value) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        if (value == null)
            throw new IllegalArgumentException("null value expression");
        QueryStreamImpl.checkOffsetLimit(this, "set()");
        return this.modQuery((query, selection) -> query.set(attribute, value));
    }

    @Override
    public <Y, V extends Y> UpdateStream<X> set(SingularAttribute<? super X, Y> attribute, V value) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        QueryStreamImpl.checkOffsetLimit(this, "set()");
        return this.modQuery((query, selection) -> query.set(attribute, value));
    }

    @Override
    public <Y> UpdateStream<X> set(Path<Y> path, Function<? super Root<X>, ? extends Expression<? extends Y>> expressionBuilder) {
        if (path == null)
            throw new IllegalArgumentException("null path");
        if (expressionBuilder == null)
            throw new IllegalArgumentException("null expressionBuilder");
        QueryStreamImpl.checkOffsetLimit(this, "set()");
        return this.modQuery((query, selection) -> query.set(path, expressionBuilder.apply(selection)));
    }

    @Override
    public <Y> UpdateStream<X> set(SingularAttribute<? super X, Y> attribute,
      Function<? super Root<X>, ? extends Expression<? extends Y>> expressionBuilder) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        if (expressionBuilder == null)
            throw new IllegalArgumentException("null expressionBuilder");
        QueryStreamImpl.checkOffsetLimit(this, "set()");
        return this.modQuery((query, selection) -> query.set(attribute, expressionBuilder.apply(selection)));
    }

// Subclass required methods

    @Override
    UpdateStream<X> create(EntityManager entityManager, UpdateType<X> queryType,
      QueryConfigurer<CriteriaUpdate<X>, X, ? extends Root<X>> configurer, QueryInfo queryInfo) {
        return new UpdateStreamImpl<>(entityManager, queryType, configurer, queryInfo);
    }

    @Override
    CriteriaUpdate<X> select(CriteriaUpdate<X> query, Root<X> selection) {
        return query;
    }

// Narrowing overrides (QueryStreamImpl)

    @Override
    public UpdateType<X> getQueryType() {
        return this.queryType;
    }

    @Override
    UpdateStream<X> modQuery(BiConsumer<? super CriteriaUpdate<X>, ? super Root<X>> modifier) {
        return (UpdateStream<X>)super.modQuery(modifier);
    }

    @Override
    public UpdateStream<X> bind(Ref<X, ? super Root<X>> ref) {
        return (UpdateStream<X>)super.bind(ref);
    }

    @Override
    public <X2, S2 extends Selection<X2>> UpdateStream<X> bind(
      Ref<X2, ? super S2> ref, Function<? super Root<X>, ? extends S2> refFunction) {
        return (UpdateStream<X>)super.bind(ref, refFunction);
    }

    @Override
    public UpdateStream<X> peek(Consumer<? super Root<X>> peeker) {
        return (UpdateStream<X>)super.peek(peeker);
    }

    @Override
    public UpdateStream<X> filter(SingularAttribute<? super X, Boolean> attribute) {
        return (UpdateStream<X>)super.filter(attribute);
    }

    @Override
    public UpdateStream<X> filter(Function<? super Root<X>, ? extends Expression<Boolean>> predicateBuilder) {
        return (UpdateStream<X>)super.filter(predicateBuilder);
    }

    @Override
    public UpdateStream<X> limit(int limit) {
        return (UpdateStream<X>)super.limit(limit);
    }

    @Override
    public UpdateStream<X> skip(int skip) {
        return (UpdateStream<X>)super.skip(skip);
    }

    @Override
    public UpdateStream<X> withFlushMode(FlushModeType flushMode) {
        return (UpdateStream<X>)super.withFlushMode(flushMode);
    }

    @Override
    public UpdateStream<X> withLockMode(LockModeType lockMode) {
        return (UpdateStream<X>)super.withLockMode(lockMode);
    }

    @Override
    public UpdateStream<X> withHint(String name, Object value) {
        return (UpdateStream<X>)super.withHint(name, value);
    }

    @Override
    public UpdateStream<X> withHints(Map<String, Object> hints) {
        return (UpdateStream<X>)super.withHints(hints);
    }

    @Override
    public <T> UpdateStream<X> withParam(Parameter<T> parameter, T value) {
        return (UpdateStream<X>)super.withParam(parameter, value);
    }

    @Override
    public UpdateStream<X> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType) {
        return (UpdateStream<X>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public UpdateStream<X> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType) {
        return (UpdateStream<X>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public UpdateStream<X> withParams(Iterable<? extends ParamBinding<?>> params) {
        return (UpdateStream<X>)super.withParams(params);
    }

    @Override
    public UpdateStream<X> withLoadGraph(String name) {
        return (UpdateStream<X>)super.withLoadGraph(name);
    }

    @Override
    public UpdateStream<X> withFetchGraph(String name) {
        return (UpdateStream<X>)super.withFetchGraph(name);
    }
}

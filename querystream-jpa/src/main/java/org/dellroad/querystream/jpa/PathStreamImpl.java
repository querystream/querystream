
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

class PathStreamImpl<X, S extends Path<X>> extends ExprStreamImpl<X, S> implements PathStream<X, S> {

// Constructors

    PathStreamImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, QueryInfo queryInfo) {
        super(entityManager, queryType, configurer, queryInfo);
    }

    @Override
    public <Y extends X> PathStream<Y, ? extends Path<Y>> cast(Class<Y> type) {
        return new PathStreamImpl<Y, Path<Y>>(this.getEntityManager(), new SearchType<>(type),
          (builder, query) -> builder.treat(this.configure(builder, query), type), this.queryInfo);
    }

// Narrowing overrides (SearchStreamImpl)

    @Override
    PathStream<X, S> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, QueryInfo queryInfo) {
        return new PathStreamImpl<>(entityManager, queryType, configurer, queryInfo);
    }

    @Override
    PathValue<X, S> toValue() {
        return this.toValue(false);
    }

    @Override
    PathValue<X, S> toValue(boolean forceLimit) {
        return new PathValueImpl<>(this.entityManager, this.queryType,
          this.configurer, forceLimit ? this.queryInfo.withMaxResults(1) : this.queryInfo);
    }

    @Override
    public PathStream<X, S> distinct() {
        return (PathStream<X, S>)super.distinct();
    }

    @Override
    public PathStream<X, S> orderBy(Ref<?, ? extends Expression<?>> ref, boolean asc) {
        return (PathStream<X, S>)super.orderBy(ref, asc);
    }

    @Override
    public PathStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute, boolean asc) {
        return (PathStream<X, S>)super.orderBy(attribute, asc);
    }

    @Override
    public PathStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2) {
        return (PathStream<X, S>)super.orderBy(attribute1, asc1, attribute2, asc2);
    }

    @Override
    public PathStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2, SingularAttribute<? super X, ?> attribute3, boolean asc3) {
        return (PathStream<X, S>)super.orderBy(attribute1, asc1, attribute2, asc2, attribute3, asc3);
    }

    @Override
    public PathStream<X, S> orderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc) {
        return (PathStream<X, S>)super.orderBy(orderExprFunction, asc);
    }

    @Override
    public PathStream<X, S> orderBy(Order... orders) {
        return (PathStream<X, S>)super.orderBy(orders);
    }

    @Override
    public PathStream<X, S> orderByMulti(Function<? super S, ? extends List<? extends Order>> orderListFunction) {
        return (PathStream<X, S>)super.orderByMulti(orderListFunction);
    }

    @Override
    public PathStream<X, S> groupBy(Ref<?, ? extends Expression<?>> ref) {
        return (PathStream<X, S>)super.groupBy(ref);
    }

    @Override
    public PathStream<X, S> groupBy(SingularAttribute<? super X, ?> attribute) {
        return (PathStream<X, S>)super.groupBy(attribute);
    }

    @Override
    public PathStream<X, S> groupBy(Function<? super S, ? extends Expression<?>> groupFunction) {
        return (PathStream<X, S>)super.groupBy(groupFunction);
    }

    @Override
    public PathStream<X, S> groupByMulti(Function<? super S, ? extends List<Expression<?>>> groupFunction) {
        return (PathStream<X, S>)super.groupByMulti(groupFunction);
    }

    @Override
    public PathStream<X, S> having(Function<? super S, ? extends Expression<Boolean>> havingFunction) {
        return (PathStream<X, S>)super.having(havingFunction);
    }

    @Override
    public PathValue<X, S> findAny() {
        return (PathValue<X, S>)super.findAny();
    }

    @Override
    public PathValue<X, S> findFirst() {
        return (PathValue<X, S>)super.findFirst();
    }

    @Override
    public <R> PathStream<X, S> addRoot(Ref<R, ? super Root<R>> ref, Class<R> type) {
        return (PathStream<X, S>)super.addRoot(ref, type);
    }

// Narrowing overrides (QueryStreamImpl)

    @Override
    public PathStream<X, S> bind(Ref<X, ? super S> ref) {
        return (PathStream<X, S>)super.bind(ref);
    }

    @Override
    public PathStream<X, S> peek(Consumer<? super S> peeker) {
        return (PathStream<X, S>)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> PathStream<X, S> bind(
      Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction) {
        return (PathStream<X, S>)super.bind(ref, refFunction);
    }

    @Override
    public PathStream<X, S> filter(SingularAttribute<? super X, Boolean> attribute) {
        return (PathStream<X, S>)super.filter(attribute);
    }

    @Override
    public PathStream<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder) {
        return (PathStream<X, S>)super.filter(predicateBuilder);
    }

    @Override
    public PathStream<X, S> limit(int limit) {
        return (PathStream<X, S>)super.limit(limit);
    }

    @Override
    public PathStream<X, S> skip(int skip) {
        return (PathStream<X, S>)super.skip(skip);
    }

    @Override
    public PathStream<X, S> withFlushMode(FlushModeType flushMode) {
        return (PathStream<X, S>)super.withFlushMode(flushMode);
    }

    @Override
    public PathStream<X, S> withLockMode(LockModeType lockMode) {
        return (PathStream<X, S>)super.withLockMode(lockMode);
    }

    @Override
    public PathStream<X, S> withHint(String name, Object value) {
        return (PathStream<X, S>)super.withHint(name, value);
    }

    @Override
    public PathStream<X, S> withHints(Map<String, Object> hints) {
        return (PathStream<X, S>)super.withHints(hints);
    }

    @Override
    public PathStream<X, S> withLoadGraph(String name) {
        return (PathStream<X, S>)super.withLoadGraph(name);
    }

    @Override
    public PathStream<X, S> withFetchGraph(String name) {
        return (PathStream<X, S>)super.withFetchGraph(name);
    }
}

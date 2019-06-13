
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.TemporalType;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

class RootStreamImpl<X> extends FromStreamImpl<X, Root<X>> implements RootStream<X> {

// Constructors

    RootStreamImpl(EntityManager entityManager, Class<X> type) {
        this(entityManager, new SearchType<X>(type));
    }

    // Separate constructor to avoid bogus error ("cannot reference queryType before supertype constructor has been called")
    private RootStreamImpl(EntityManager entityManager, SearchType<X> queryType) {
        this(entityManager, queryType, (builder, query) -> query.from(queryType.getType()), new QueryInfo());
    }

    RootStreamImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends Root<X>> configurer, QueryInfo queryInfo) {
        super(entityManager, queryType, configurer, queryInfo);
    }

// Narrowing overrides (PathStreamImpl)

    @Override
    public <Y extends X> RootStream<Y> cast(Class<Y> type) {
        return new RootStreamImpl<Y>(this.getEntityManager(), new SearchType<>(type),
          (builder, query) -> builder.treat(this.configure(builder, query), type), this.queryInfo);
    }

// Narrowing overrides (SearchStreamImpl)

    @Override
    RootStream<X> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends Root<X>> configurer, QueryInfo queryInfo) {
        return new RootStreamImpl<>(entityManager, queryType, configurer, queryInfo);
    }

    @Override
    RootValue<X> toValue() {
        return this.toValue(false);
    }

    @Override
    RootValue<X> toValue(boolean forceLimit) {
        return new RootValueImpl<>(this.entityManager, this.queryType,
          this.configurer, forceLimit ? this.queryInfo.withMaxResults(1) : this.queryInfo);
    }

    @Override
    public RootStream<X> distinct() {
        return (RootStream<X>)super.distinct();
    }

    @Override
    public RootStream<X> orderBy(Ref<?, ? extends Expression<?>> ref, boolean asc) {
        return (RootStream<X>)super.orderBy(ref, asc);
    }

    @Override
    public RootStream<X> orderBy(SingularAttribute<? super X, ?> attribute, boolean asc) {
        return (RootStream<X>)super.orderBy(attribute, asc);
    }

    @Override
    public RootStream<X> orderBy(SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2) {
        return (RootStream<X>)super.orderBy(attribute1, asc1, attribute2, asc2);
    }

    @Override
    public RootStream<X> orderBy(SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2, SingularAttribute<? super X, ?> attribute3, boolean asc3) {
        return (RootStream<X>)super.orderBy(attribute1, asc1, attribute2, asc2, attribute3, asc3);
    }

    @Override
    public RootStream<X> orderBy(Function<? super Root<X>, ? extends Expression<?>> orderExprFunction, boolean asc) {
        return (RootStream<X>)super.orderBy(orderExprFunction, asc);
    }

    @Override
    public RootStream<X> orderBy(Order... orders) {
        return (RootStream<X>)super.orderBy(orders);
    }

    @Override
    public RootStream<X> orderByMulti(Function<? super Root<X>, ? extends List<? extends Order>> orderListFunction) {
        return (RootStream<X>)super.orderByMulti(orderListFunction);
    }

    @Override
    public RootStream<X> thenOrderBy(Ref<?, ? extends Expression<?>> ref, boolean asc) {
        return (RootStream<X>)super.thenOrderBy(ref, asc);
    }

    @Override
    public RootStream<X> thenOrderBy(Order... orders) {
        return (RootStream<X>)super.thenOrderBy(orders);
    }

    @Override
    public RootStream<X> thenOrderBy(SingularAttribute<? super X, ?> attribute, boolean asc) {
        return (RootStream<X>)super.thenOrderBy(attribute, asc);
    }

    @Override
    public RootStream<X> thenOrderBy(Function<? super Root<X>, ? extends Expression<?>> orderExprFunction, boolean asc) {
        return (RootStream<X>)super.thenOrderBy(orderExprFunction, asc);
    }

    @Override
    public RootStream<X> groupBy(Ref<?, ? extends Expression<?>> ref) {
        return (RootStream<X>)super.groupBy(ref);
    }

    @Override
    public RootStream<X> groupBy(SingularAttribute<? super X, ?> attribute) {
        return (RootStream<X>)super.groupBy(attribute);
    }

    @Override
    public RootStream<X> groupBy(Function<? super Root<X>, ? extends Expression<?>> groupFunction) {
        return (RootStream<X>)super.groupBy(groupFunction);
    }

    @Override
    public RootStream<X> groupByMulti(Function<? super Root<X>, ? extends List<Expression<?>>> groupFunction) {
        return (RootStream<X>)super.groupByMulti(groupFunction);
    }

    @Override
    public RootStream<X> having(Function<? super Root<X>, ? extends Expression<Boolean>> havingFunction) {
        return (RootStream<X>)super.having(havingFunction);
    }

    @Override
    public RootValue<X> findAny() {
        return (RootValue<X>)super.findAny();
    }

    @Override
    public RootValue<X> findFirst() {
        return (RootValue<X>)super.findFirst();
    }

    @Override
    public <R> RootStream<X> addRoot(Ref<R, ? super Root<R>> ref, Class<R> type) {
        return (RootStream<X>)super.addRoot(ref, type);
    }

// Narrowing overrides (QueryStreamImpl)

    @Override
    public RootStream<X> bind(Ref<X, ? super Root<X>> ref) {
        return (RootStream<X>)super.bind(ref);
    }

    @Override
    public RootStream<X> peek(Consumer<? super Root<X>> peeker) {
        return (RootStream<X>)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> RootStream<X> bind(
      Ref<X2, ? super S2> ref, Function<? super Root<X>, ? extends S2> refFunction) {
        return (RootStream<X>)super.bind(ref, refFunction);
    }

    @Override
    public RootStream<X> filter(SingularAttribute<? super X, Boolean> attribute) {
        return (RootStream<X>)super.filter(attribute);
    }

    @Override
    public RootStream<X> filter(Function<? super Root<X>, ? extends Expression<Boolean>> predicateBuilder) {
        return (RootStream<X>)super.filter(predicateBuilder);
    }

    @Override
    public RootStream<X> limit(int limit) {
        return (RootStream<X>)super.limit(limit);
    }

    @Override
    public RootStream<X> skip(int skip) {
        return (RootStream<X>)super.skip(skip);
    }

    @Override
    public RootStream<X> withFlushMode(FlushModeType flushMode) {
        return (RootStream<X>)super.withFlushMode(flushMode);
    }

    @Override
    public RootStream<X> withLockMode(LockModeType lockMode) {
        return (RootStream<X>)super.withLockMode(lockMode);
    }

    @Override
    public RootStream<X> withHint(String name, Object value) {
        return (RootStream<X>)super.withHint(name, value);
    }

    @Override
    public RootStream<X> withHints(Map<String, Object> hints) {
        return (RootStream<X>)super.withHints(hints);
    }

    @Override
    public <T> RootStream<X> withParam(Parameter<T> parameter, T value) {
        return (RootStream<X>)super.withParam(parameter, value);
    }

    @Override
    public RootStream<X> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType) {
        return (RootStream<X>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public RootStream<X> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType) {
        return (RootStream<X>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public RootStream<X> withParams(Set<ParamBinding<?>> params) {
        return (RootStream<X>)super.withParams(params);
    }

    @Override
    public RootStream<X> withLoadGraph(String name) {
        return (RootStream<X>)super.withLoadGraph(name);
    }

    @Override
    public RootStream<X> withFetchGraph(String name) {
        return (RootStream<X>)super.withFetchGraph(name);
    }
}


/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.TemporalType;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

class ExprStreamImpl<X, S extends Expression<X>> extends SearchStreamImpl<X, S> implements ExprStream<X, S> {

// Constructors

    ExprStreamImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, QueryInfo queryInfo) {
        super(entityManager, queryType, configurer, queryInfo);
    }

// Subqueries

    @Override
    public Subquery<X> asSubquery() {
        final CurrentQuery outer = QueryStreamImpl.getCurrentQuery();
        final CriteriaBuilder builder = outer.getBuilder();
        final Subquery<X> subquery = outer.getQuery().subquery(this.queryType.getType());
        final Subquery<X> subquery2 = subquery.select(QueryStreamImpl.withCurrentQuery(builder, subquery,
          () -> this.configurer.configure(builder, subquery)));
        QueryStreamImpl.mergeQueryInfo(this.queryInfo);         // propagage any parameters, etc., up to the outer query
        return subquery2;
    }

// Aggregation

    @Override
    public Predicate exists() {
        return QueryStreamImpl.getCurrentQuery().getBuilder().exists(this.asSubquery());
    }

    @Override
    public LongValue count() {
        QueryStreamImpl.checkOffsetLimit(this, "count()");
        return new LongValueImpl(this.entityManager,
          (builder, query) -> builder.count(this.configurer.configure(builder, query)), this.queryInfo);
    }

    @Override
    public LongValue countDistinct() {
        QueryStreamImpl.checkOffsetLimit(this, "countDistinct()");
        return new LongValueImpl(this.entityManager,
          (builder, query) -> builder.countDistinct(this.configurer.configure(builder, query)), this.queryInfo);
    }

// Narrowing overrides (SearchStreamImpl)

    @Override
    ExprStream<X, S> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, QueryInfo queryInfo) {
        return new ExprStreamImpl<>(entityManager, queryType, configurer, queryInfo);
    }

    @Override
    ExprValue<X, S> toValue() {
        return this.toValue(false);
    }

    @Override
    ExprValue<X, S> toValue(boolean forceLimit) {
        return new ExprValueImpl<>(this.entityManager, this.queryType,
          this.configurer, forceLimit ? this.queryInfo.withMaxResults(1) : this.queryInfo);
    }

    @Override
    public ExprStream<X, S> distinct() {
        return (ExprStream<X, S>)super.distinct();
    }

    @Override
    public ExprStream<X, S> orderBy(Ref<?, ? extends Expression<?>> ref, boolean asc) {
        return (ExprStream<X, S>)super.orderBy(ref, asc);
    }

    @Override
    public ExprStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute, boolean asc) {
        return (ExprStream<X, S>)super.orderBy(attribute, asc);
    }

    @Override
    public ExprStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2) {
        return (ExprStream<X, S>)super.orderBy(attribute1, asc1, attribute2, asc2);
    }

    @Override
    public ExprStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2, SingularAttribute<? super X, ?> attribute3, boolean asc3) {
        return (ExprStream<X, S>)super.orderBy(attribute1, asc1, attribute2, asc2, attribute3, asc3);
    }

    @Override
    public ExprStream<X, S> orderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc) {
        return (ExprStream<X, S>)super.orderBy(orderExprFunction, asc);
    }

    @Override
    public ExprStream<X, S> orderBy(Order... orders) {
        return (ExprStream<X, S>)super.orderBy(orders);
    }

    @Override
    public ExprStream<X, S> orderByMulti(Function<? super S, ? extends List<? extends Order>> orderListFunction) {
        return (ExprStream<X, S>)super.orderByMulti(orderListFunction);
    }

    @Override
    public ExprStream<X, S> thenOrderBy(Ref<?, ? extends Expression<?>> ref, boolean asc) {
        return (ExprStream<X, S>)super.thenOrderBy(ref, asc);
    }

    @Override
    public ExprStream<X, S> thenOrderBy(Order... orders) {
        return (ExprStream<X, S>)super.thenOrderBy(orders);
    }

    @Override
    public ExprStream<X, S> thenOrderBy(SingularAttribute<? super X, ?> attribute, boolean asc) {
        return (ExprStream<X, S>)super.thenOrderBy(attribute, asc);
    }

    @Override
    public ExprStream<X, S> thenOrderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc) {
        return (ExprStream<X, S>)super.thenOrderBy(orderExprFunction, asc);
    }

    @Override
    public ExprStream<X, S> groupBy(Ref<?, ? extends Expression<?>> ref) {
        return (ExprStream<X, S>)super.groupBy(ref);
    }

    @Override
    public ExprStream<X, S> groupBy(SingularAttribute<? super X, ?> attribute) {
        return (ExprStream<X, S>)super.groupBy(attribute);
    }

    @Override
    public ExprStream<X, S> groupBy(Function<? super S, ? extends Expression<?>> groupFunction) {
        return (ExprStream<X, S>)super.groupBy(groupFunction);
    }

    @Override
    public ExprStream<X, S> groupByMulti(Function<? super S, ? extends List<Expression<?>>> groupFunction) {
        return (ExprStream<X, S>)super.groupByMulti(groupFunction);
    }

    @Override
    public ExprStream<X, S> having(Function<? super S, ? extends Expression<Boolean>> havingFunction) {
        return (ExprStream<X, S>)super.having(havingFunction);
    }

    @Override
    public ExprValue<X, S> findAny() {
        return (ExprValue<X, S>)super.findAny();
    }

    @Override
    public ExprValue<X, S> findFirst() {
        return (ExprValue<X, S>)super.findFirst();
    }

    @Override
    public ExprValue<X, S> findSingle() {
        return (ExprValue<X, S>)super.findSingle();
    }

    @Override
    public <R> ExprStream<X, S> addRoot(Ref<R, ? super Root<R>> ref, Class<R> type) {
        return (ExprStream<X, S>)super.addRoot(ref, type);
    }

    @Override
    public ExprStream<X, S> fetch(SingularAttribute<? super X, ?> attribute) {
        return (ExprStream<X, S>)super.fetch(attribute);
    }

    @Override
    public ExprStream<X, S> fetch(SingularAttribute<? super X, ?> attribute, JoinType joinType) {
        return (ExprStream<X, S>)super.fetch(attribute, joinType);
    }

    @Override
    public ExprStream<X, S> fetch(PluralAttribute<? super X, ?, ?> attribute) {
        return (ExprStream<X, S>)super.fetch(attribute);
    }

    @Override
    public ExprStream<X, S> fetch(PluralAttribute<? super X, ?, ?> attribute, JoinType joinType) {
        return (ExprStream<X, S>)super.fetch(attribute, joinType);
    }

// Narrowing overrides (QueryStreamImpl)

    @Override
    public ExprStream<X, S> bind(Ref<X, ? super S> ref) {
        return (ExprStream<X, S>)super.bind(ref);
    }

    @Override
    public ExprStream<X, S> peek(Consumer<? super S> peeker) {
        return (ExprStream<X, S>)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> ExprStream<X, S> bind(
      Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction) {
        return (ExprStream<X, S>)super.bind(ref, refFunction);
    }

    @Override
    public ExprStream<X, S> filter(SingularAttribute<? super X, Boolean> attribute) {
        return (ExprStream<X, S>)super.filter(attribute);
    }

    @Override
    public ExprStream<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder) {
        return (ExprStream<X, S>)super.filter(predicateBuilder);
    }

    @Override
    public ExprStream<X, S> limit(int limit) {
        return (ExprStream<X, S>)super.limit(limit);
    }

    @Override
    public ExprStream<X, S> skip(int skip) {
        return (ExprStream<X, S>)super.skip(skip);
    }

    @Override
    public ExprStream<X, S> withFlushMode(FlushModeType flushMode) {
        return (ExprStream<X, S>)super.withFlushMode(flushMode);
    }

    @Override
    public ExprStream<X, S> withLockMode(LockModeType lockMode) {
        return (ExprStream<X, S>)super.withLockMode(lockMode);
    }

    @Override
    public ExprStream<X, S> withHint(String name, Object value) {
        return (ExprStream<X, S>)super.withHint(name, value);
    }

    @Override
    public ExprStream<X, S> withHints(Map<String, Object> hints) {
        return (ExprStream<X, S>)super.withHints(hints);
    }

    @Override
    public <T> ExprStream<X, S> withParam(Parameter<T> parameter, T value) {
        return (ExprStream<X, S>)super.withParam(parameter, value);
    }

    @Override
    public ExprStream<X, S> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType) {
        return (ExprStream<X, S>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public ExprStream<X, S> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType) {
        return (ExprStream<X, S>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public ExprStream<X, S> withParams(Iterable<? extends ParamBinding<?>> params) {
        return (ExprStream<X, S>)super.withParams(params);
    }

    @Override
    public ExprStream<X, S> withLoadGraph(String name) {
        return (ExprStream<X, S>)super.withLoadGraph(name);
    }

    @Override
    public ExprStream<X, S> withFetchGraph(String name) {
        return (ExprStream<X, S>)super.withFetchGraph(name);
    }
}

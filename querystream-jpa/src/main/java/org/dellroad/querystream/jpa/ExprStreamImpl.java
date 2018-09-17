
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

class ExprStreamImpl<X, S extends Expression<X>> extends SearchStreamImpl<X, S> implements ExprStream<X, S> {

// Constructors

    ExprStreamImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, int firstResult, int maxResults) {
        super(entityManager, queryType, configurer, firstResult, maxResults);
    }

// Subqueries

    @Override
    public Subquery<X> asSubquery() {
        final QueryInfo outer = QueryStreamImpl.getQueryInfo();
        final CriteriaBuilder builder = outer.getBuilder();
        final Subquery<X> subquery = outer.getQuery().subquery(this.queryType.getType());
        return subquery.select(QueryStreamImpl.withQueryInfo(builder, subquery,
          () -> this.configurer.configure(builder, subquery)));
    }

// Aggregation

    @Override
    public Predicate exists() {
        return QueryStreamImpl.getQueryInfo().getBuilder().exists(this.asSubquery());
    }

    @Override
    public LongValue count() {
        QueryStreamImpl.checkOffsetLimit(this, "count() must be performed prior to skip() or limit()");
        return new LongValueImpl(this.entityManager,
          (builder, query) -> builder.count(this.configurer.configure(builder, query)), -1, -1);
    }

    @Override
    public LongValue countDistinct() {
        QueryStreamImpl.checkOffsetLimit(this, "countDistinct() must be performed prior to skip() or limit()");
        return new LongValueImpl(this.entityManager,
          (builder, query) -> builder.countDistinct(this.configurer.configure(builder, query)), -1, -1);
    }

// Narrowing overrides (SearchStreamImpl)

    @Override
    ExprStream<X, S> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, int firstResult, int maxResults) {
        return new ExprStreamImpl<>(entityManager, queryType, configurer, firstResult, maxResults);
    }

    @Override
    ExprValue<X, S> toValue() {
        return this.toValue(false);
    }

    @Override
    ExprValue<X, S> toValue(boolean forceLimit) {
        return new ExprValueImpl<>(this.entityManager, this.queryType,
          this.configurer, this.firstResult, forceLimit ? 1 : this.maxResults);
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
    public ExprStream<X, S> orderByMulti(Function<? super S, ? extends List<? extends Order>> orderListFunction) {
        return (ExprStream<X, S>)super.orderByMulti(orderListFunction);
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
    public <R> ExprStream<X, S> addRoot(Ref<R, ? super Root<R>> ref, Class<R> type) {
        return (ExprStream<X, S>)super.addRoot(ref, type);
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
}

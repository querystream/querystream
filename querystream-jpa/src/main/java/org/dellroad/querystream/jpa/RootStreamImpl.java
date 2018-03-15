
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.List;
import java.util.function.Function;

import javax.persistence.EntityManager;
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

    private RootStreamImpl(EntityManager entityManager, SearchType<X> queryType) {
        this(entityManager, queryType, (builder, query) -> query.from(queryType.getType()));
    }

    RootStreamImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends Root<X>> configurer) {
        super(entityManager, queryType, configurer);
    }

// Narrowing overrides (SearchStreamImpl)

    @Override
    RootStream<X> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends Root<X>> configurer) {
        return new RootStreamImpl<>(entityManager, queryType, configurer);
    }

    @Override
    RootValue<X> toValue() {
        return new RootValueImpl<>(this.entityManager, this.queryType, this.configurer);
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
    public RootStream<X> orderBy(Function<? super Root<X>, ? extends Expression<?>> orderExprFunction, boolean asc) {
        return (RootStream<X>)super.orderBy(orderExprFunction, asc);
    }

    @Override
    public RootStream<X> orderByMulti(Function<? super Root<X>, ? extends List<? extends Order>> orderListFunction) {
        return (RootStream<X>)super.orderByMulti(orderListFunction);
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
}

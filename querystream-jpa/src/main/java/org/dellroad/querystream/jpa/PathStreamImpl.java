
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
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

class PathStreamImpl<X, S extends Path<X>> extends ExprStreamImpl<X, S> implements PathStream<X, S> {

// Constructors

    PathStreamImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer) {
        super(entityManager, queryType, configurer);
    }

// Narrowing overrides (SearchStreamImpl)

    @Override
    PathStream<X, S> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer) {
        return new PathStreamImpl<>(entityManager, queryType, configurer);
    }

    @Override
    PathValue<X, S> toValue() {
        return new PathValueImpl<>(this.entityManager, this.queryType, this.configurer);
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
    public PathStream<X, S> orderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc) {
        return (PathStream<X, S>)super.orderBy(orderExprFunction, asc);
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

// Narrowing overrides (QueryStreamImpl)

    @Override
    public PathStream<X, S> bind(Ref<X, ? super S> ref) {
        return (PathStream<X, S>)super.bind(ref);
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
}

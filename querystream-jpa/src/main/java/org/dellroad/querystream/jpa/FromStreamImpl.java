
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.List;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

class FromStreamImpl<X, S extends From<?, X>> extends PathStreamImpl<X, S> implements FromStream<X, S> {

// Constructors

    FromStreamImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer) {
        super(entityManager, queryType, configurer);
    }

/*

// Plural Joins

    public <E> SearchStream<E> flatMap(CollectionAttribute<? super X, E> attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        return this.flatMap(attribute.getElementType().getJavaType(), from -> from.join(attribute));
    }

    public <E> SearchStream<E> flatMap(ListAttribute<? super X, E> attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        return this.flatMap(attribute.getElementType().getJavaType(), from -> from.join(attribute));
    }

    public <K, V> SearchStream<V> flatMapValues(MapAttribute<? super X, K, V> attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        return this.flatMap(attribute.getElementType().getJavaType(), from -> from.join(attribute));
    }

    public <K, V> SearchStream<K> flatMapKeys(MapAttribute<? super X, K, V> attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        return this.<K>flatMap(attribute.getKeyJavaType(), from -> from.join(attribute).key());
    }

    public <E> SearchStream<E> flatMap(SetAttribute<? super X, E> attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        return this.flatMap(attribute.getElementType().getJavaType(), from -> from.join(attribute));
    }

    // This is used for plural joins, where limit and offset may need to be "recalculated" (which we can't do; see below)
    private <E> SearchStream<E> flatMap(Class<E> joinedType, Function<From<?, X>, ? extends Expression<E>> joiner) {

        // If there is an offset or limit configured, we need to do a sub-query, so that the offset & limit apply to the
        // owners of the collection, not the collection elements themselves. Oops! The Criteria API doesn't support setting
        // a row offset or row count limit on a subquery (why not?):
        //  https://stackoverflow.com/questions/37187193/criteria-api-limit-results-in-subquery
        if (this.offset != 0 || this.limit != -1) {
            throw new UnsupportedOperationException("sorry, can't perform plural join after skip() or limit() because"
              + " the JPA Criteria API does not support setting a row offset or row count limit in subqueries");
        }

        // Apply "flat map" via join
        return new FromStream<E>(this.entityManager, new SearchType<>(joinedType),
          (builder, query) -> joiner.apply(this.configurer.configureFrom(builder, query)));
    }
*/

// Narrowing overrides (SearchStreamImpl)

    @Override
    FromStream<X, S> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer) {
        return new FromStreamImpl<>(entityManager, queryType, configurer);
    }

    @Override
    FromValue<X, S> toValue() {
        return new FromValueImpl<>(this.entityManager, this.queryType, this.configurer);
    }

    @Override
    public FromStream<X, S> distinct() {
        return (FromStream<X, S>)super.distinct();
    }

    @Override
    public FromStream<X, S> orderBy(Ref<?, ? extends Expression<?>> ref, boolean asc) {
        return (FromStream<X, S>)super.orderBy(ref, asc);
    }

    @Override
    public FromStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute, boolean asc) {
        return (FromStream<X, S>)super.orderBy(attribute, asc);
    }

    @Override
    public FromStream<X, S> orderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc) {
        return (FromStream<X, S>)super.orderBy(orderExprFunction, asc);
    }

    @Override
    public FromStream<X, S> orderByMulti(Function<? super S, ? extends List<? extends Order>> orderListFunction) {
        return (FromStream<X, S>)super.orderByMulti(orderListFunction);
    }

    @Override
    public FromStream<X, S> groupBy(Ref<?, ? extends Expression<?>> ref) {
        return (FromStream<X, S>)super.groupBy(ref);
    }

    @Override
    public FromStream<X, S> groupBy(SingularAttribute<? super X, ?> attribute) {
        return (FromStream<X, S>)super.groupBy(attribute);
    }

    @Override
    public FromStream<X, S> groupBy(Function<? super S, ? extends Expression<?>> groupFunction) {
        return (FromStream<X, S>)super.groupBy(groupFunction);
    }

    @Override
    public FromStream<X, S> groupByMulti(Function<? super S, ? extends List<Expression<?>>> groupFunction) {
        return (FromStream<X, S>)super.groupByMulti(groupFunction);
    }

    @Override
    public FromStream<X, S> having(Function<? super S, ? extends Expression<Boolean>> havingFunction) {
        return (FromStream<X, S>)super.having(havingFunction);
    }

    @Override
    public FromValue<X, S> findAny() {
        return (FromValue<X, S>)super.findAny();
    }

    @Override
    public FromValue<X, S> findFirst() {
        return (FromValue<X, S>)super.findFirst();
    }

    @Override
    public <R> FromStream<X, S> addRoot(Ref<R, ? super Root<R>> ref, Class<R> type) {
        return (FromStream<X, S>)super.addRoot(ref, type);
    }

// Narrowing overrides (QueryStreamImpl)

    @Override
    public FromStream<X, S> bind(Ref<X, ? super S> ref) {
        return (FromStream<X, S>)super.bind(ref);
    }

    @Override
    public <X2, S2 extends Selection<X2>> FromStream<X, S> bind(
      Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction) {
        return (FromStream<X, S>)super.bind(ref, refFunction);
    }

    @Override
    public FromStream<X, S> filter(SingularAttribute<? super X, Boolean> attribute) {
        return (FromStream<X, S>)super.filter(attribute);
    }

    @Override
    public FromStream<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder) {
        return (FromStream<X, S>)super.filter(predicateBuilder);
    }
}

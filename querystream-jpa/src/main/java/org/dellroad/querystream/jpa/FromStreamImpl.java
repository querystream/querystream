
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
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

class FromStreamImpl<X, S extends From<?, X>> extends PathStreamImpl<X, S> implements FromStream<X, S> {

// Constructors

    FromStreamImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, QueryInfo queryInfo) {
        super(entityManager, queryType, configurer, queryInfo);
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
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, QueryInfo queryInfo) {
        return new FromStreamImpl<>(entityManager, queryType, configurer, queryInfo);
    }

    @Override
    FromStream<X, S> withConfig(QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer) {
        return (FromStream<X, S>)super.withConfig(configurer);
    }

    @Override
    FromValue<X, S> toValue() {
        return this.toValue(false);
    }

    @Override
    FromValue<X, S> toValue(boolean forceLimit) {
        return new FromValueImpl<>(this.entityManager, this.queryType,
          this.configurer, forceLimit ? this.queryInfo.withMaxResults(1) : this.queryInfo);
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
    public FromStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2) {
        return (FromStream<X, S>)super.orderBy(attribute1, asc1, attribute2, asc2);
    }

    @Override
    public FromStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2, SingularAttribute<? super X, ?> attribute3, boolean asc3) {
        return (FromStream<X, S>)super.orderBy(attribute1, asc1, attribute2, asc2, attribute3, asc3);
    }

    @Override
    public FromStream<X, S> orderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc) {
        return (FromStream<X, S>)super.orderBy(orderExprFunction, asc);
    }

    @Override
    public FromStream<X, S> orderBy(Order... orders) {
        return (FromStream<X, S>)super.orderBy(orders);
    }

    @Override
    public FromStream<X, S> orderByMulti(Function<? super S, ? extends List<? extends Order>> orderListFunction) {
        return (FromStream<X, S>)super.orderByMulti(orderListFunction);
    }

    @Override
    public FromStream<X, S> thenOrderBy(Ref<?, ? extends Expression<?>> ref, boolean asc) {
        return (FromStream<X, S>)super.thenOrderBy(ref, asc);
    }

    @Override
    public FromStream<X, S> thenOrderBy(Order... orders) {
        return (FromStream<X, S>)super.thenOrderBy(orders);
    }

    @Override
    public FromStream<X, S> thenOrderBy(SingularAttribute<? super X, ?> attribute, boolean asc) {
        return (FromStream<X, S>)super.thenOrderBy(attribute, asc);
    }

    @Override
    public FromStream<X, S> thenOrderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc) {
        return (FromStream<X, S>)super.thenOrderBy(orderExprFunction, asc);
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
    public FromValue<X, S> findSingle() {
        return (FromValue<X, S>)super.findSingle();
    }

    @Override
    public <R> FromStream<X, S> addRoot(Ref<R, ? super Root<R>> ref, Class<R> type) {
        return (FromStream<X, S>)super.addRoot(ref, type);
    }

// Fetches

    @Override
    public FromStream<X, S> fetch(SingularAttribute<? super X, ?> attribute) {
        return this.fetch(attribute, JoinType.INNER);
    }

    @Override
    public FromStream<X, S> fetch(SingularAttribute<? super X, ?> attribute, JoinType joinType) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        if (joinType == null)
            throw new IllegalArgumentException("null joinType");
        return this.withConfig((builder, query) -> {
            final S selection = this.configure(builder, query);
            selection.fetch(attribute, joinType);
            return selection;
        });
    }

    @Override
    public FromStream<X, S> fetch(PluralAttribute<? super X, ?, ?> attribute) {
        return this.fetch(attribute, JoinType.INNER);
    }

    @Override
    public FromStream<X, S> fetch(PluralAttribute<? super X, ?, ?> attribute, JoinType joinType) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        if (joinType == null)
            throw new IllegalArgumentException("null joinType");
        QueryStreamImpl.checkOffsetLimit(this, "plural fetch()");
        return this.withConfig((builder, query) -> {
            final S selection = this.configure(builder, query);
            selection.fetch(attribute, joinType);
            return selection;
        });
    }

// Narrowing overrides (QueryStreamImpl)

    @Override
    public FromStream<X, S> bind(Ref<X, ? super S> ref) {
        return (FromStream<X, S>)super.bind(ref);
    }

    @Override
    public FromStream<X, S> peek(Consumer<? super S> peeker) {
        return (FromStream<X, S>)super.peek(peeker);
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

    @Override
    public FromStream<X, S> limit(int limit) {
        return (FromStream<X, S>)super.limit(limit);
    }

    @Override
    public FromStream<X, S> skip(int skip) {
        return (FromStream<X, S>)super.skip(skip);
    }

    @Override
    public FromStream<X, S> withFlushMode(FlushModeType flushMode) {
        return (FromStream<X, S>)super.withFlushMode(flushMode);
    }

    @Override
    public FromStream<X, S> withLockMode(LockModeType lockMode) {
        return (FromStream<X, S>)super.withLockMode(lockMode);
    }

    @Override
    public FromStream<X, S> withHint(String name, Object value) {
        return (FromStream<X, S>)super.withHint(name, value);
    }

    @Override
    public FromStream<X, S> withHints(Map<String, Object> hints) {
        return (FromStream<X, S>)super.withHints(hints);
    }

    @Override
    public <T> FromStream<X, S> withParam(Parameter<T> parameter, T value) {
        return (FromStream<X, S>)super.withParam(parameter, value);
    }

    @Override
    public FromStream<X, S> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType) {
        return (FromStream<X, S>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public FromStream<X, S> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType) {
        return (FromStream<X, S>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public FromStream<X, S> withParams(Set<ParamBinding<?>> params) {
        return (FromStream<X, S>)super.withParams(params);
    }

    @Override
    public FromStream<X, S> withLoadGraph(String name) {
        return (FromStream<X, S>)super.withLoadGraph(name);
    }

    @Override
    public FromStream<X, S> withFetchGraph(String name) {
        return (FromStream<X, S>)super.withFetchGraph(name);
    }
}

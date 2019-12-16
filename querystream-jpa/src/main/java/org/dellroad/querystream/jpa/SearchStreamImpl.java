
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

class SearchStreamImpl<X, S extends Selection<X>>
  extends QueryStreamImpl<X, S, AbstractQuery<?>, CriteriaQuery<X>, TypedQuery<X>, SearchType<X>>
  implements SearchStream<X, S> {

// Constructors

    SearchStreamImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, QueryInfo queryInfo) {
        super(entityManager, queryType, configurer, queryInfo);
    }

// Superclass overrides

    @Override
    SearchStream<X, S> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, QueryInfo queryInfo) {
        return new SearchStreamImpl<>(entityManager, queryType, configurer, queryInfo);
    }

    @Override
    SearchStream<X, S> withConfig(QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer) {
        return (SearchStream<X, S>)super.withConfig(configurer);
    }

    @Override
    final CriteriaQuery<X> select(CriteriaQuery<X> query, S selection) {
        return query.select(selection);
    }

// Subclass required methods

    SearchValue<X, S> toValue() {
        return this.toValue(false);
    }

    SearchValue<X, S> toValue(boolean forceLimit) {
        return new SearchValueImpl<>(this.entityManager, this.queryType,
          this.configurer, forceLimit ? this.queryInfo.withMaxResults(1) : this.queryInfo);
    }

// CriteriaQuery stuff

    @Override
    public SearchStream<X, S> distinct() {
        QueryStreamImpl.checkOffsetLimit(this, "distinct()");
        return this.modQuery((query, selection) -> query.distinct(true));
    }

    @Override
    public SearchStream<X, S> orderBy(Ref<?, ? extends Expression<?>> ref, boolean asc) {
        if (ref == null)
            throw new IllegalArgumentException("null ref");
        return this.orderBy(selection -> ref.get(), asc, false);
    }

    @Override
    public SearchStream<X, S> thenOrderBy(Ref<?, ? extends Expression<?>> ref, boolean asc) {
        if (ref == null)
            throw new IllegalArgumentException("null ref");
        return this.orderBy(selection -> ref.get(), asc, true);
    }

    @Override
    public SearchStream<X, S> orderBy(Order... orders) {
        if (orders == null)
            throw new IllegalArgumentException("null orders");
        return this.orderByMulti(selection -> Arrays.asList(orders), false);
    }

    @Override
    public SearchStream<X, S> thenOrderBy(Order... orders) {
        if (orders == null)
            throw new IllegalArgumentException("null orders");
        return this.orderByMulti(selection -> Arrays.asList(orders), true);
    }

    @Override
    public SearchStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute, boolean asc) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        return this.orderBy(selection -> ((Path<X>)selection).get(attribute), asc, false);  // cast is valid if attribute exists
    }

    @Override
    public SearchStream<X, S> thenOrderBy(SingularAttribute<? super X, ?> attribute, boolean asc) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        return this.orderBy(selection -> ((Path<X>)selection).get(attribute), asc, true);   // cast is valid if attribute exists
    }

    @Override
    public SearchStream<X, S> orderBy(
      SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2) {
        if (attribute1 == null)
            throw new IllegalArgumentException("null attribute1");
        if (attribute2 == null)
            throw new IllegalArgumentException("null attribute2");
        return this.orderByMulti(selection -> {
            final Path<X> path = (Path<X>)selection;                                    // cast must be valid if attribute exists
            return Arrays.asList(
              asc1 ? this.builder().asc(path.get(attribute1)) : this.builder().desc(path.get(attribute1)),
              asc2 ? this.builder().asc(path.get(attribute2)) : this.builder().desc(path.get(attribute2)));
        }, false);
    }

    @Override
    public SearchStream<X, S> orderBy(
      SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2,
      SingularAttribute<? super X, ?> attribute3, boolean asc3) {
        if (attribute1 == null)
            throw new IllegalArgumentException("null attribute1");
        if (attribute2 == null)
            throw new IllegalArgumentException("null attribute2");
        if (attribute3 == null)
            throw new IllegalArgumentException("null attribute3");
        return this.orderByMulti(selection -> {
            final Path<X> path = (Path<X>)selection;                                    // cast must be valid if attribute exists
            return Arrays.asList(
              asc1 ? this.builder().asc(path.get(attribute1)) : this.builder().desc(path.get(attribute1)),
              asc2 ? this.builder().asc(path.get(attribute2)) : this.builder().desc(path.get(attribute2)),
              asc3 ? this.builder().asc(path.get(attribute3)) : this.builder().desc(path.get(attribute3)));
        }, false);
    }

    @Override
    public SearchStream<X, S> orderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc) {
        return this.orderBy(orderExprFunction, asc, false);
    }

    @Override
    public SearchStream<X, S> thenOrderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc) {
        return this.orderBy(orderExprFunction, asc, true);
    }

    private SearchStream<X, S> orderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc, boolean add) {
        if (orderExprFunction == null)
            throw new IllegalArgumentException("null orderExprFunction");
        return this.orderByMulti(selection -> {
            final Expression<?> expr = orderExprFunction.apply(selection);
            return Collections.singletonList(asc ? this.builder().asc(expr) : this.builder().desc(expr));
        }, add);
    }

    @Override
    public SearchStream<X, S> orderByMulti(Function<? super S, ? extends List<? extends Order>> orderListFunction) {
        return this.orderByMulti(orderListFunction, false);
    }

    private SearchStream<X, S> orderByMulti(Function<? super S, ? extends List<? extends Order>> orderListFunction, boolean add) {
        if (orderListFunction == null)
            throw new IllegalArgumentException("null orderListFunction");
        QueryStreamImpl.checkOffsetLimit(this, "sorting");
        return this.withConfig((builder, query) -> {
            if (!(query instanceof CriteriaQuery)) {
                throw new UnsupportedOperationException("sorry, can't sort a subquery because"
                  + " the JPA Criteria API doesn't support sorting in subqueries");
            }
            final CriteriaQuery<?> criteriaQuery = (CriteriaQuery<?>)query;
            final S selection = this.configure(builder, query);
            final ArrayList<Order> newOrders = new ArrayList<>();
            if (add)
                newOrders.addAll(criteriaQuery.getOrderList());
            newOrders.addAll(orderListFunction.apply(selection));
            criteriaQuery.orderBy(newOrders);
            return selection;
        });
    }

    @Override
    public SearchStream<X, S> groupBy(Ref<?, ? extends Expression<?>> ref) {
        if (ref == null)
            throw new IllegalArgumentException("null ref");
        return this.groupBy(selection -> ref.get());
    }

    @Override
    public SearchStream<X, S> groupBy(SingularAttribute<? super X, ?> attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        return this.groupBy(selection -> ((Path<X>)selection).get(attribute));           // cast must be valid if attribute exists
    }

    @Override
    public SearchStream<X, S> groupBy(Function<? super S, ? extends Expression<?>> groupFunction) {
        if (groupFunction == null)
            throw new IllegalArgumentException("null groupFunction");
        return this.groupByMulti(selection -> Collections.singletonList(groupFunction.apply(selection)));
    }

    @Override
    public SearchStream<X, S> groupByMulti(Function<? super S, ? extends List<Expression<?>>> groupFunction) {
        if (groupFunction == null)
            throw new IllegalArgumentException("null groupFunction");
        QueryStreamImpl.checkOffsetLimit(this, "grouping");
        return this.withConfig((builder, query) -> {
            final S selection = this.configure(builder, query);
            query.groupBy(groupFunction.apply(selection));
            return selection;
        });
    }

    @Override
    public SearchStream<X, S> having(Function<? super S, ? extends Expression<Boolean>> havingFunction) {
        if (havingFunction == null)
            throw new IllegalArgumentException("null havingFunction");
        QueryStreamImpl.checkOffsetLimit(this, "grouping");
        return this.withConfig((builder, query) -> {
            final S selection = this.configure(builder, query);
            query.having(havingFunction.apply(selection));
            return selection;
        });
    }

// Streamy stuff

    @Override
    public boolean allMatch(SingularAttribute<? super X, Boolean> attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        QueryStreamImpl.checkOffsetLimit(this, "allMatch()");
        return !this.withConfig((builder, query) -> {
            final S selection = this.configure(builder, query);
            this.and(builder, query, builder.not(((Path<X>)selection).get(attribute))); // cast must be valid if attribute exists
            return selection;
        }).getResultStream().findAny().isPresent();
    }

    @Override
    public boolean allMatch(Function<? super S, ? extends Expression<Boolean>> predicateBuilder) {
        if (predicateBuilder == null)
            throw new IllegalArgumentException("null predicateBuilder");
        QueryStreamImpl.checkOffsetLimit(this, "allMatch()");
        return !this.withConfig((builder, query) -> {
            final S selection = this.configure(builder, query);
            this.and(builder, query, builder.not(predicateBuilder.apply(selection)));
            return selection;
        }).getResultStream().findAny().isPresent();
    }

    @Override
    public boolean anyMatch(SingularAttribute<? super X, Boolean> attribute) {
        return this.filter(attribute).getResultStream().findAny().isPresent();
    }

    @Override
    public boolean anyMatch(Function<? super S, ? extends Expression<Boolean>> predicateBuilder) {
        return this.filter(predicateBuilder).getResultStream().findAny().isPresent();
    }

    @Override
    public boolean noneMatch(SingularAttribute<? super X, Boolean> attribute) {
        return !this.anyMatch(attribute);
    }

    @Override
    public boolean noneMatch(Function<? super S, ? extends Expression<Boolean>> predicateBuilder) {
        return !this.anyMatch(predicateBuilder);
    }

    @Override
    public boolean isEmpty() {
        return !this.getResultStream().findAny().isPresent();
    }

    @Override
    public SearchValue<X, S> findAny() {
        return this instanceof SearchValue ? (SearchValue<X, S>)this : this.toValue(true);
    }

    @Override
    public SearchValue<X, S> findFirst() {
        return this instanceof SearchValue ? (SearchValue<X, S>)this : this.toValue(true);
    }

    @Override
    public SearchValue<X, S> findSingle() {
        return this.toValue(false);
    }

// Binding

    @Override
    public <R> SearchStream<X, S> addRoot(Ref<R, ? super Root<R>> ref, Class<R> type) {
        if (ref == null)
            throw new IllegalArgumentException("null ref");
        if (type == null)
            throw new IllegalArgumentException("null type");
        QueryStreamImpl.checkOffsetLimit(this, "roots must be added prior to skip() or limit()");
        return this.withConfig((builder, query) -> {
            final S selection = this.configure(builder, query);
            ref.bind(query.from(type));
            return selection;
        });
    }

// Narrowing overrides (QueryStreamImpl)

    @Override
    public SearchType<X> getQueryType() {
        return this.queryType;
    }

    @Override
    SearchStream<X, S> modQuery(BiConsumer<? super AbstractQuery<?>, ? super S> modifier) {
        return (SearchStream<X, S>)super.modQuery(modifier);
    }

    @Override
    public SearchStream<X, S> bind(Ref<X, ? super S> ref) {
        return (SearchStream<X, S>)super.bind(ref);
    }

    @Override
    public SearchStream<X, S> peek(Consumer<? super S> peeker) {
        return (SearchStream<X, S>)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> SearchStream<X, S> bind(
      Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction) {
        return (SearchStream<X, S>)super.bind(ref, refFunction);
    }

    @Override
    public SearchStream<X, S> filter(SingularAttribute<? super X, Boolean> attribute) {
        return (SearchStream<X, S>)super.filter(attribute);
    }

    @Override
    public SearchStream<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder) {
        return (SearchStream<X, S>)super.filter(predicateBuilder);
    }

    @Override
    public SearchStream<X, S> limit(int limit) {
        return (SearchStream<X, S>)super.limit(limit);
    }

    @Override
    public SearchStream<X, S> skip(int skip) {
        return (SearchStream<X, S>)super.skip(skip);
    }

    @Override
    public SearchStream<X, S> withFlushMode(FlushModeType flushMode) {
        return (SearchStream<X, S>)super.withFlushMode(flushMode);
    }

    @Override
    public SearchStream<X, S> withLockMode(LockModeType lockMode) {
        return (SearchStream<X, S>)super.withLockMode(lockMode);
    }

    @Override
    public SearchStream<X, S> withHint(String name, Object value) {
        return (SearchStream<X, S>)super.withHint(name, value);
    }

    @Override
    public SearchStream<X, S> withHints(Map<String, Object> hints) {
        return (SearchStream<X, S>)super.withHints(hints);
    }

    @Override
    public <T> SearchStream<X, S> withParam(Parameter<T> parameter, T value) {
        return (SearchStream<X, S>)super.withParam(parameter, value);
    }

    @Override
    public SearchStream<X, S> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType) {
        return (SearchStream<X, S>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public SearchStream<X, S> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType) {
        return (SearchStream<X, S>)super.withParam(parameter, value, temporalType);
    }

    @Override
    public SearchStream<X, S> withParams(Set<ParamBinding<?>> params) {
        return (SearchStream<X, S>)super.withParams(params);
    }

    @Override
    public SearchStream<X, S> withLoadGraph(String name) {
        return (SearchStream<X, S>)super.withLoadGraph(name);
    }

    @Override
    public SearchStream<X, S> withFetchGraph(String name) {
        return (SearchStream<X, S>)super.withFetchGraph(name);
    }
}

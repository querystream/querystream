
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa.util;

import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CollectionJoin;
import jakarta.persistence.criteria.CompoundSelection;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.MapJoin;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.criteria.SetJoin;
import jakarta.persistence.criteria.Subquery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * {@link CriteriaBuilder} that forwards all methods to a delegate {@linkplain #getCriteriaBuilder provided} by the subclass.
 *
 * <p>
 * Subclasses must override {@link #getCriteriaBuilder} to provide the delegate.
 *
 * <p>
 * This class also adds some "fail fast" error checking for null values so that errors occur immediately, instead of
 * later (and more mysteriously) during query execution.
 */
public abstract class ForwardingCriteriaBuilder implements CriteriaBuilder {

    /**
     * Get the delegate {@link CriteriaBuilder} to whom all methods should be forwarded.
     *
     * @return the underlying {@link CriteriaBuilder}
     */
    protected abstract CriteriaBuilder getCriteriaBuilder();

    @Override
    public CriteriaQuery<Object> createQuery() {
        return this.getCriteriaBuilder().createQuery();
    }

    @Override
    public <T> CriteriaQuery<T> createQuery(Class<T> resultClass) {
        this.nullCheck("resultClass", resultClass);
        return this.getCriteriaBuilder().createQuery(resultClass);
    }

    @Override
    public CriteriaQuery<Tuple> createTupleQuery() {
        return this.getCriteriaBuilder().createTupleQuery();
    }

    @Override
    public <T> CriteriaUpdate<T> createCriteriaUpdate(Class<T> targetEntity) {
        this.nullCheck("targetEntity", targetEntity);
        return this.getCriteriaBuilder().createCriteriaUpdate(targetEntity);
    }

    @Override
    public <T> CriteriaDelete<T> createCriteriaDelete(Class<T> targetEntity) {
        this.nullCheck("targetEntity", targetEntity);
        return this.getCriteriaBuilder().createCriteriaDelete(targetEntity);
    }

    @Override
    public <Y> CompoundSelection<Y> construct(Class<Y> resultClass, Selection<?>... selections) {
        this.nullCheck("resultClass", resultClass);
        this.nullCheck("selections", selections);
        return this.getCriteriaBuilder().construct(resultClass, selections);
    }

    @Override
    public CompoundSelection<Tuple> tuple(Selection<?>... selections) {
        this.nullCheck("selections", selections);
        return this.getCriteriaBuilder().tuple(selections);
    }

    @Override
    public CompoundSelection<Object[]> array(Selection<?>... selections) {
        this.nullCheck("selections", selections);
        return this.getCriteriaBuilder().array(selections);
    }

    @Override
    public Order asc(Expression<?> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().asc(expr);
    }

    @Override
    public Order desc(Expression<?> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().desc(expr);
    }

    @Override
    public <N extends Number> Expression<Double> avg(Expression<N> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().avg(expr);
    }

    @Override
    public <N extends Number> Expression<N> sum(Expression<N> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().sum(expr);
    }

    @Override
    public Expression<Long> sumAsLong(Expression<Integer> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().sumAsLong(expr);
    }

    @Override
    public Expression<Double> sumAsDouble(Expression<Float> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().sumAsDouble(expr);
    }

    @Override
    public <N extends Number> Expression<N> max(Expression<N> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().max(expr);
    }

    @Override
    public <N extends Number> Expression<N> min(Expression<N> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().min(expr);
    }

    @Override
    public <X extends Comparable<? super X>> Expression<X> greatest(Expression<X> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().greatest(expr);
    }

    @Override
    public <X extends Comparable<? super X>> Expression<X> least(Expression<X> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().least(expr);
    }

    @Override
    public Expression<Long> count(Expression<?> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().count(expr);
    }

    @Override
    public Expression<Long> countDistinct(Expression<?> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().countDistinct(expr);
    }

    @Override
    public Predicate exists(Subquery<?> subquery) {
        this.nullCheck("subquery", subquery);
        return this.getCriteriaBuilder().exists(subquery);
    }

    @Override
    public <Y> Expression<Y> all(Subquery<Y> subquery) {
        this.nullCheck("subquery", subquery);
        return this.getCriteriaBuilder().all(subquery);
    }

    @Override
    public <Y> Expression<Y> some(Subquery<Y> subquery) {
        this.nullCheck("subquery", subquery);
        return this.getCriteriaBuilder().some(subquery);
    }

    @Override
    public <Y> Expression<Y> any(Subquery<Y> subquery) {
        this.nullCheck("subquery", subquery);
        return this.getCriteriaBuilder().any(subquery);
    }

    @Override
    public Predicate and(Expression<Boolean> x, Expression<Boolean> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().and(x, y);
    }

    @Override
    public Predicate and(Predicate... preds) {
        this.nullCheck("preds", preds);
        return this.getCriteriaBuilder().and(preds);
    }

    @Override
    public Predicate or(Expression<Boolean> x, Expression<Boolean> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().or(x, y);
    }

    @Override
    public Predicate or(Predicate... preds) {
        this.nullCheck("preds", preds);
        return this.getCriteriaBuilder().or(preds);
    }

    @Override
    public Predicate not(Expression<Boolean> pred) {
        this.nullCheck("pred", pred);
        return this.getCriteriaBuilder().not(pred);
    }

    @Override
    public Predicate conjunction() {
        return this.getCriteriaBuilder().conjunction();
    }

    @Override
    public Predicate disjunction() {
        return this.getCriteriaBuilder().disjunction();
    }

    @Override
    public Predicate isTrue(Expression<Boolean> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().isTrue(expr);
    }

    @Override
    public Predicate isFalse(Expression<Boolean> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().isFalse(expr);
    }

    @Override
    public Predicate isNull(Expression<?> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().isNull(expr);
    }

    @Override
    public Predicate isNotNull(Expression<?> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().isNotNull(expr);
    }

    @Override
    public Predicate equal(Expression<?> x, Expression<?> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().equal(x, y);
    }

    @Override
    public Predicate equal(Expression<?> x, Object y) {
        this.nullCheck("x", x);
        return this.getCriteriaBuilder().equal(x, y);
    }

    @Override
    public Predicate notEqual(Expression<?> x, Expression<?> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().notEqual(x, y);
    }

    @Override
    public Predicate notEqual(Expression<?> x, Object y) {
        this.nullCheck("x", x);
        return this.getCriteriaBuilder().notEqual(x, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate greaterThan(Expression<? extends Y> x, Expression<? extends Y> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().greaterThan(x, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate greaterThan(Expression<? extends Y> x, Y y) {
        this.nullCheck("x", x);
        return this.getCriteriaBuilder().greaterThan(x, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate greaterThanOrEqualTo(Expression<? extends Y> x, Expression<? extends Y> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().greaterThanOrEqualTo(x, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate greaterThanOrEqualTo(Expression<? extends Y> x, Y y) {
        this.nullCheck("x", x);
        return this.getCriteriaBuilder().greaterThanOrEqualTo(x, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate lessThan(Expression<? extends Y> x, Expression<? extends Y> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().lessThan(x, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate lessThan(Expression<? extends Y> x, Y y) {
        this.nullCheck("x", x);
        return this.getCriteriaBuilder().lessThan(x, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate lessThanOrEqualTo(Expression<? extends Y> x, Expression<? extends Y> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().lessThanOrEqualTo(x, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate lessThanOrEqualTo(Expression<? extends Y> x, Y y) {
        this.nullCheck("x", x);
        return this.getCriteriaBuilder().lessThanOrEqualTo(x, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate between(Expression<? extends Y> expr,
      Expression<? extends Y> lo, Expression<? extends Y> hi) {
        this.nullCheck("expr", expr);
        this.nullCheck("lo", lo);
        this.nullCheck("hi", hi);
        return this.getCriteriaBuilder().between(expr, lo, hi);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate between(Expression<? extends Y> expr, Y lo, Y hi) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().between(expr, lo, hi);
    }

    @Override
    public Predicate gt(Expression<? extends Number> x, Expression<? extends Number> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().gt(x, y);
    }

    @Override
    public Predicate gt(Expression<? extends Number> x, Number y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().gt(x, y);
    }

    @Override
    public Predicate ge(Expression<? extends Number> x, Expression<? extends Number> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().ge(x, y);
    }

    @Override
    public Predicate ge(Expression<? extends Number> x, Number y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().ge(x, y);
    }

    @Override
    public Predicate lt(Expression<? extends Number> x, Expression<? extends Number> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().lt(x, y);
    }

    @Override
    public Predicate lt(Expression<? extends Number> x, Number y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().lt(x, y);
    }

    @Override
    public Predicate le(Expression<? extends Number> x, Expression<? extends Number> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().le(x, y);
    }

    @Override
    public Predicate le(Expression<? extends Number> x, Number y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().le(x, y);
    }

    @Override
    public <N extends Number> Expression<N> neg(Expression<N> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().neg(expr);
    }

    @Override
    public <N extends Number> Expression<N> abs(Expression<N> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().abs(expr);
    }

    @Override
    public <N extends Number> Expression<N> sum(Expression<? extends N> x, Expression<? extends N> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().sum(x, y);
    }

    @Override
    public <N extends Number> Expression<N> sum(Expression<? extends N> x, N y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().sum(x, y);
    }

    @Override
    public <N extends Number> Expression<N> sum(N x, Expression<? extends N> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().sum(x, y);
    }

    @Override
    public <N extends Number> Expression<N> prod(Expression<? extends N> x, Expression<? extends N> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().prod(x, y);
    }

    @Override
    public <N extends Number> Expression<N> prod(Expression<? extends N> x, N y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().prod(x, y);
    }

    @Override
    public <N extends Number> Expression<N> prod(N x, Expression<? extends N> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().prod(x, y);
    }

    @Override
    public <N extends Number> Expression<N> diff(Expression<? extends N> x, Expression<? extends N> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().diff(x, y);
    }

    @Override
    public <N extends Number> Expression<N> diff(Expression<? extends N> x, N y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().diff(x, y);
    }

    @Override
    public <N extends Number> Expression<N> diff(N x, Expression<? extends N> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().diff(x, y);
    }

    @Override
    public Expression<Number> quot(Expression<? extends Number> x, Expression<? extends Number> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().quot(x, y);
    }

    @Override
    public Expression<Number> quot(Expression<? extends Number> x, Number y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().quot(x, y);
    }

    @Override
    public Expression<Number> quot(Number x, Expression<? extends Number> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().quot(x, y);
    }

    @Override
    public Expression<Integer> mod(Expression<Integer> x, Expression<Integer> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().mod(x, y);
    }

    @Override
    public Expression<Integer> mod(Expression<Integer> x, Integer y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().mod(x, y);
    }

    @Override
    public Expression<Integer> mod(Integer x, Expression<Integer> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().mod(x, y);
    }

    @Override
    public Expression<Double> sqrt(Expression<? extends Number> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().sqrt(expr);
    }

    @Override
    public Expression<Long> toLong(Expression<? extends Number> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().toLong(expr);
    }

    @Override
    public Expression<Integer> toInteger(Expression<? extends Number> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().toInteger(expr);
    }

    @Override
    public Expression<Float> toFloat(Expression<? extends Number> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().toFloat(expr);
    }

    @Override
    public Expression<Double> toDouble(Expression<? extends Number> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().toDouble(expr);
    }

    @Override
    public Expression<java.math.BigDecimal> toBigDecimal(Expression<? extends Number> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().toBigDecimal(expr);
    }

    @Override
    public Expression<java.math.BigInteger> toBigInteger(Expression<? extends Number> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().toBigInteger(expr);
    }

    @Override
    public Expression<String> toString(Expression<Character> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().toString(expr);
    }

    @Override
    public <T> Expression<T> literal(T expr) {
        return this.getCriteriaBuilder().literal(expr);
    }

    @Override
    public <T> Expression<T> nullLiteral(Class<T> type) {
        this.nullCheck("type", type);
        return this.getCriteriaBuilder().nullLiteral(type);
    }

    @Override
    public <T> ParameterExpression<T> parameter(Class<T> type) {
        this.nullCheck("type", type);
        return this.getCriteriaBuilder().parameter(type);
    }

    @Override
    public <T> ParameterExpression<T> parameter(Class<T> type, String name) {
        this.nullCheck("type", type);
        this.nullCheck("name", name);
        return this.getCriteriaBuilder().parameter(type, name);
    }

    @Override
    public <C extends Collection<?>> Predicate isEmpty(Expression<C> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().isEmpty(expr);
    }

    @Override
    public <C extends Collection<?>> Predicate isNotEmpty(Expression<C> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().isNotEmpty(expr);
    }

    @Override
    public <C extends Collection<?>> Expression<Integer> size(Expression<C> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().size(expr);
    }

    @Override
    public <C extends Collection<?>> Expression<Integer> size(C expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().size(expr);
    }

    @Override
    public <E, C extends Collection<E>> Predicate isMember(Expression<E> x, Expression<C> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().isMember(x, y);
    }

    @Override
    public <E, C extends Collection<E>> Predicate isMember(E x, Expression<C> y) {
        //this.nullCheck("x", x); ???
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().isMember(x, y);
    }

    @Override
    public <E, C extends Collection<E>> Predicate isNotMember(Expression<E> x, Expression<C> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().isNotMember(x, y);
    }

    @Override
    public <E, C extends Collection<E>> Predicate isNotMember(E x, Expression<C> y) {
        //this.nullCheck("x", x); ???
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().isNotMember(x, y);
    }

    @Override
    public <V, M extends Map<?, V>> Expression<Collection<V>> values(M map) {
        return this.getCriteriaBuilder().values(map);
    }

    @Override
    public <K, M extends Map<K, ?>> Expression<Set<K>> keys(M map) {
        return this.getCriteriaBuilder().keys(map);
    }

    @Override
    public Predicate like(Expression<String> x, Expression<String> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().like(x, y);
    }

    @Override
    public Predicate like(Expression<String> x, String y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().like(x, y);
    }

    @Override
    public Predicate like(Expression<String> x, Expression<String> y, Expression<Character> esc) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        this.nullCheck("esc", esc);
        return this.getCriteriaBuilder().like(x, y, esc);
    }

    @Override
    public Predicate like(Expression<String> x, Expression<String> y, char esc) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().like(x, y, esc);
    }

    @Override
    public Predicate like(Expression<String> x, String y, Expression<Character> esc) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        this.nullCheck("esc", esc);
        return this.getCriteriaBuilder().like(x, y, esc);
    }

    @Override
    public Predicate like(Expression<String> x, String y, char esc) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().like(x, y, esc);
    }

    @Override
    public Predicate notLike(Expression<String> x, Expression<String> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().notLike(x, y);
    }

    @Override
    public Predicate notLike(Expression<String> x, String y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().notLike(x, y);
    }

    @Override
    public Predicate notLike(Expression<String> x, Expression<String> y, Expression<Character> esc) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        this.nullCheck("esc", esc);
        return this.getCriteriaBuilder().notLike(x, y, esc);
    }

    @Override
    public Predicate notLike(Expression<String> x, Expression<String> y, char esc) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().notLike(x, y, esc);
    }

    @Override
    public Predicate notLike(Expression<String> x, String y, Expression<Character> esc) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        this.nullCheck("esc", esc);
        return this.getCriteriaBuilder().notLike(x, y, esc);
    }

    @Override
    public Predicate notLike(Expression<String> x, String y, char esc) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().notLike(x, y, esc);
    }

    @Override
    public Expression<String> concat(Expression<String> x, Expression<String> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().concat(x, y);
    }

    @Override
    public Expression<String> concat(Expression<String> x, String y) {
        this.nullCheck("x", x);
        //this.nullCheck("y", y);  ???
        return this.getCriteriaBuilder().concat(x, y);
    }

    @Override
    public Expression<String> concat(String x, Expression<String> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().concat(x, y);
    }

    @Override
    public Expression<String> substring(Expression<String> x, Expression<Integer> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().substring(x, y);
    }

    @Override
    public Expression<String> substring(Expression<String> x, int y) {
        this.nullCheck("x", x);
        return this.getCriteriaBuilder().substring(x, y);
    }

    @Override
    public Expression<String> substring(Expression<String> x, Expression<Integer> y, Expression<Integer> z) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        this.nullCheck("z", z);
        return this.getCriteriaBuilder().substring(x, y, z);
    }

    @Override
    public Expression<String> substring(Expression<String> x, int y, int z) {
        this.nullCheck("x", x);
        return this.getCriteriaBuilder().substring(x, y, z);
    }

    @Override
    public Expression<String> trim(Expression<String> x) {
        this.nullCheck("x", x);
        return this.getCriteriaBuilder().trim(x);
    }

    @Override
    public Expression<String> trim(CriteriaBuilder.Trimspec x, Expression<String> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().trim(x, y);
    }

    @Override
    public Expression<String> trim(Expression<Character> x, Expression<String> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().trim(x, y);
    }

    @Override
    public Expression<String> trim(CriteriaBuilder.Trimspec x, Expression<Character> y, Expression<String> z) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        this.nullCheck("z", z);
        return this.getCriteriaBuilder().trim(x, y, z);
    }

    @Override
    public Expression<String> trim(char x, Expression<String> y) {
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().trim(x, y);
    }

    @Override
    public Expression<String> trim(CriteriaBuilder.Trimspec x, char y, Expression<String> z) {
        this.nullCheck("x", x);
        this.nullCheck("z", z);
        return this.getCriteriaBuilder().trim(x, y, z);
    }

    @Override
    public Expression<String> lower(Expression<String> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().lower(expr);
    }

    @Override
    public Expression<String> upper(Expression<String> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().upper(expr);
    }

    @Override
    public Expression<Integer> length(Expression<String> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().length(expr);
    }

    @Override
    public Expression<Integer> locate(Expression<String> x, Expression<String> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().locate(x, y);
    }

    @Override
    public Expression<Integer> locate(Expression<String> x, String y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().locate(x, y);
    }

    @Override
    public Expression<Integer> locate(Expression<String> x, Expression<String> y, Expression<Integer> z) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        this.nullCheck("z", z);
        return this.getCriteriaBuilder().locate(x, y, z);
    }

    @Override
    public Expression<Integer> locate(Expression<String> x, String y, int z) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().locate(x, y, z);
    }

    @Override
    public Expression<java.sql.Date> currentDate() {
        return this.getCriteriaBuilder().currentDate();
    }

    @Override
    public Expression<java.sql.Timestamp> currentTimestamp() {
        return this.getCriteriaBuilder().currentTimestamp();
    }

    @Override
    public Expression<java.sql.Time> currentTime() {
        return this.getCriteriaBuilder().currentTime();
    }

    @Override
    public <T> CriteriaBuilder.In<T> in(Expression<? extends T> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().in(expr);
    }

    @Override
    public <Y> Expression<Y> coalesce(Expression<? extends Y> x, Expression<? extends Y> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().coalesce(x, y);
    }

    @Override
    public <Y> Expression<Y> coalesce(Expression<? extends Y> x, Y y) {
        this.nullCheck("x", x);
        return this.getCriteriaBuilder().coalesce(x, y);
    }

    @Override
    public <Y> Expression<Y> nullif(Expression<Y> x, Expression<?> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().nullif(x, y);
    }

    @Override
    public <Y> Expression<Y> nullif(Expression<Y> x, Y y) {
        this.nullCheck("x", x);
        return this.getCriteriaBuilder().nullif(x, y);
    }

    @Override
    public <T> CriteriaBuilder.Coalesce<T> coalesce() {
        return this.getCriteriaBuilder().coalesce();
    }

    @Override
    public <C, R> CriteriaBuilder.SimpleCase<C, R> selectCase(Expression<? extends C> expr) {
        this.nullCheck("expr", expr);
        return this.getCriteriaBuilder().selectCase(expr);
    }

    @Override
    public <R> CriteriaBuilder.Case<R> selectCase() {
        return this.getCriteriaBuilder().selectCase();
    }

    @Override
    public <T> Expression<T> function(String name, Class<T> type, Expression<?>... exprs) {
        this.nullCheck("name", name);
        this.nullCheck("type", type);
        this.nullCheck("exprs", exprs);
        return this.getCriteriaBuilder().function(name, type, exprs);
    }

    @Override
    public <X, T, V extends T> Join<X, V> treat(Join<X, T> join, Class<V> type) {
        this.nullCheck("join", join);
        this.nullCheck("type", type);
        return this.getCriteriaBuilder().treat(join, type);
    }

    @Override
    public <X, T, E extends T> CollectionJoin<X, E> treat(CollectionJoin<X, T> join, Class<E> type) {
        this.nullCheck("join", join);
        this.nullCheck("type", type);
        return this.getCriteriaBuilder().treat(join, type);
    }

    @Override
    public <X, T, E extends T> SetJoin<X, E> treat(SetJoin<X, T> join, Class<E> type) {
        this.nullCheck("join", join);
        this.nullCheck("type", type);
        return this.getCriteriaBuilder().treat(join, type);
    }

    @Override
    public <X, T, E extends T> ListJoin<X, E> treat(ListJoin<X, T> join, Class<E> type) {
        this.nullCheck("join", join);
        this.nullCheck("type", type);
        return this.getCriteriaBuilder().treat(join, type);
    }

    @Override
    public <X, K, T, V extends T> MapJoin<X, K, V> treat(MapJoin<X, K, T> join, Class<V> type) {
        this.nullCheck("join", join);
        this.nullCheck("type", type);
        return this.getCriteriaBuilder().treat(join, type);
    }

    @Override
    public <X, T extends X> Path<T> treat(Path<X> path, Class<T> type) {
        this.nullCheck("path", path);
        this.nullCheck("type", type);
        return this.getCriteriaBuilder().treat(path, type);
    }

    @Override
    public <X, T extends X> Root<T> treat(Root<X> root, Class<T> type) {
        this.nullCheck("root", root);
        this.nullCheck("type", type);
        return this.getCriteriaBuilder().treat(root, type);
    }

// JAVAEE 10

    @Override
    public Expression<LocalDate> localDate() {
        return this.getCriteriaBuilder().localDate();
    }

    @Override
    public Expression<LocalDateTime> localDateTime() {
        return this.getCriteriaBuilder().localDateTime();
    }

    @Override
    public Expression<LocalTime> localTime() {
        return this.getCriteriaBuilder().localTime();
    }

    @Override
    public <N extends Number> Expression<N> round(Expression<N> x, Integer n) {
        this.nullCheck("x", x);
        this.nullCheck("n", n);
        return this.getCriteriaBuilder().round(x, n);
    }

    @Override
    public Expression<Integer> sign(Expression<? extends Number> x) {
        this.nullCheck("x", x);
        return this.getCriteriaBuilder().sign(x);
    }

    @Override
    public <N extends Number> Expression<N> floor(Expression<N> x) {
        this.nullCheck("x", x);
        return this.getCriteriaBuilder().floor(x);
    }

    @Override
    public <N extends Number> Expression<N> ceiling(Expression<N> x) {
        this.nullCheck("x", x);
        return this.getCriteriaBuilder().ceiling(x);
    }

    @Override
    public Expression<Double> exp(Expression<? extends Number> x) {
        this.nullCheck("x", x);
        return this.getCriteriaBuilder().exp(x);
    }

    @Override
    public Expression<Double> ln(Expression<? extends Number> x) {
        this.nullCheck("x", x);
        return this.getCriteriaBuilder().ln(x);
    }

    @Override
    public Expression<Double> power(Expression<? extends Number> x, Expression<? extends Number> y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().power(x, y);
    }

    @Override
    public Expression<Double> power(Expression<? extends Number> x, Number y) {
        this.nullCheck("x", x);
        this.nullCheck("y", y);
        return this.getCriteriaBuilder().power(x, y);
    }

// Internal Methods

    private void nullCheck(String name, Object value) {
        if (value == null)
            throw new IllegalArgumentException("null " + name);
    }

    private void nullCheck(String name, Object[] values) {
        this.nullCheck(name, (Object)values);
        int index = 0;
        for (Object value : values)
            this.nullCheck(name + " #" + index++, value);
    }
}

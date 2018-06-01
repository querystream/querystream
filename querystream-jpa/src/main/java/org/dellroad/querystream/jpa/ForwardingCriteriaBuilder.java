
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.persistence.Tuple;
import javax.persistence.criteria.CollectionJoin;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.SetJoin;
import javax.persistence.criteria.Subquery;

/**
 * {@link CriteriaBuilder} that forwards all methods to a delegate {@linkplain #getCriteriaBuilder provided} by the subclass.
 */
abstract class ForwardingCriteriaBuilder implements CriteriaBuilder {

    protected abstract CriteriaBuilder getCriteriaBuilder();

    @Override
    public CriteriaQuery<Object> createQuery() {
        return this.getCriteriaBuilder().createQuery();
    }

    @Override
    public <T> CriteriaQuery<T> createQuery(Class<T> resultClass) {
        return this.getCriteriaBuilder().createQuery(resultClass);
    }

    @Override
    public CriteriaQuery<Tuple> createTupleQuery() {
        return this.getCriteriaBuilder().createTupleQuery();
    }

    @Override
    public <T> CriteriaUpdate<T> createCriteriaUpdate(Class<T> targetEntity) {
        return this.getCriteriaBuilder().createCriteriaUpdate(targetEntity);
    }

    @Override
    public <T> CriteriaDelete<T> createCriteriaDelete(Class<T> targetEntity) {
        return this.getCriteriaBuilder().createCriteriaDelete(targetEntity);
    }

    @Override
    public <Y> CompoundSelection<Y> construct(Class<Y> resultClass, Selection<?>... selections) {
        return this.getCriteriaBuilder().construct(resultClass, selections);
    }

    @Override
    public CompoundSelection<Tuple> tuple(Selection<?>... selections) {
        return this.getCriteriaBuilder().tuple(selections);
    }

    @Override
    public CompoundSelection<Object[]> array(Selection<?>... selections) {
        return this.getCriteriaBuilder().array(selections);
    }

    @Override
    public Order asc(Expression<?> expr) {
        return this.getCriteriaBuilder().asc(expr);
    }

    @Override
    public Order desc(Expression<?> expr) {
        return this.getCriteriaBuilder().desc(expr);
    }

    @Override
    public <N extends Number> Expression<Double> avg(Expression<N> expr) {
        return this.getCriteriaBuilder().avg(expr);
    }

    @Override
    public <N extends Number> Expression<N> sum(Expression<N> expr) {
        return this.getCriteriaBuilder().sum(expr);
    }

    @Override
    public Expression<Long> sumAsLong(Expression<Integer> expr) {
        return this.getCriteriaBuilder().sumAsLong(expr);
    }

    @Override
    public Expression<Double> sumAsDouble(Expression<Float> expr) {
        return this.getCriteriaBuilder().sumAsDouble(expr);
    }

    @Override
    public <N extends Number> Expression<N> max(Expression<N> expr) {
        return this.getCriteriaBuilder().max(expr);
    }

    @Override
    public <N extends Number> Expression<N> min(Expression<N> expr) {
        return this.getCriteriaBuilder().min(expr);
    }

    @Override
    public <X extends Comparable<? super X>> Expression<X> greatest(Expression<X> expr) {
        return this.getCriteriaBuilder().greatest(expr);
    }

    @Override
    public <X extends Comparable<? super X>> Expression<X> least(Expression<X> expr) {
        return this.getCriteriaBuilder().least(expr);
    }

    @Override
    public Expression<Long> count(Expression<?> expr) {
        return this.getCriteriaBuilder().count(expr);
    }

    @Override
    public Expression<Long> countDistinct(Expression<?> expr) {
        return this.getCriteriaBuilder().countDistinct(expr);
    }

    @Override
    public Predicate exists(Subquery<?> subquery) {
        return this.getCriteriaBuilder().exists(subquery);
    }

    @Override
    public <Y> Expression<Y> all(Subquery<Y> subquery) {
        return this.getCriteriaBuilder().all(subquery);
    }

    @Override
    public <Y> Expression<Y> some(Subquery<Y> subquery) {
        return this.getCriteriaBuilder().some(subquery);
    }

    @Override
    public <Y> Expression<Y> any(Subquery<Y> subquery) {
        return this.getCriteriaBuilder().any(subquery);
    }

    @Override
    public Predicate and(Expression<Boolean> x, Expression<Boolean> y) {
        return this.getCriteriaBuilder().and(x, y);
    }

    @Override
    public Predicate and(Predicate... preds) {
        return this.getCriteriaBuilder().and(preds);
    }

    @Override
    public Predicate or(Expression<Boolean> x, Expression<Boolean> y) {
        return this.getCriteriaBuilder().or(x, y);
    }

    @Override
    public Predicate or(Predicate... preds) {
        return this.getCriteriaBuilder().or(preds);
    }

    @Override
    public Predicate not(Expression<Boolean> pred) {
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
        return this.getCriteriaBuilder().isTrue(expr);
    }

    @Override
    public Predicate isFalse(Expression<Boolean> expr) {
        return this.getCriteriaBuilder().isFalse(expr);
    }

    @Override
    public Predicate isNull(Expression<?> expr) {
        return this.getCriteriaBuilder().isNull(expr);
    }

    @Override
    public Predicate isNotNull(Expression<?> expr) {
        return this.getCriteriaBuilder().isNotNull(expr);
    }

    @Override
    public Predicate equal(Expression<?> x, Expression<?> y) {
        return this.getCriteriaBuilder().equal(x, y);
    }

    @Override
    public Predicate equal(Expression<?> x, Object y) {
        return this.getCriteriaBuilder().equal(x, y);
    }

    @Override
    public Predicate notEqual(Expression<?> x, Expression<?> y) {
        return this.getCriteriaBuilder().notEqual(x, y);
    }

    @Override
    public Predicate notEqual(Expression<?> x, Object y) {
        return this.getCriteriaBuilder().notEqual(x, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate greaterThan(Expression<? extends Y> x, Expression<? extends Y> y) {
        return this.getCriteriaBuilder().greaterThan(x, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate greaterThan(Expression<? extends Y> x, Y y) {
        return this.getCriteriaBuilder().greaterThan(x, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate greaterThanOrEqualTo(Expression<? extends Y> x, Expression<? extends Y> y) {
        return this.getCriteriaBuilder().greaterThanOrEqualTo(x, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate greaterThanOrEqualTo(Expression<? extends Y> x, Y y) {
        return this.getCriteriaBuilder().greaterThanOrEqualTo(x, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate lessThan(Expression<? extends Y> x, Expression<? extends Y> y) {
        return this.getCriteriaBuilder().lessThan(x, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate lessThan(Expression<? extends Y> x, Y y) {
        return this.getCriteriaBuilder().lessThan(x, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate lessThanOrEqualTo(Expression<? extends Y> x, Expression<? extends Y> y) {
        return this.getCriteriaBuilder().lessThanOrEqualTo(x, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate lessThanOrEqualTo(Expression<? extends Y> x, Y y) {
        return this.getCriteriaBuilder().lessThanOrEqualTo(x, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate between(Expression<? extends Y> expr,
      Expression<? extends Y> lo, Expression<? extends Y> hi) {
        return this.getCriteriaBuilder().between(expr, lo, hi);
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate between(Expression<? extends Y> expr, Y lo, Y hi) {
        return this.getCriteriaBuilder().between(expr, lo, hi);
    }

    @Override
    public Predicate gt(Expression<? extends Number> x, Expression<? extends Number> y) {
        return this.getCriteriaBuilder().gt(x, y);
    }

    @Override
    public Predicate gt(Expression<? extends Number> x, Number y) {
        return this.getCriteriaBuilder().gt(x, y);
    }

    @Override
    public Predicate ge(Expression<? extends Number> x, Expression<? extends Number> y) {
        return this.getCriteriaBuilder().ge(x, y);
    }

    @Override
    public Predicate ge(Expression<? extends Number> x, Number y) {
        return this.getCriteriaBuilder().ge(x, y);
    }

    @Override
    public Predicate lt(Expression<? extends Number> x, Expression<? extends Number> y) {
        return this.getCriteriaBuilder().lt(x, y);
    }

    @Override
    public Predicate lt(Expression<? extends Number> x, Number y) {
        return this.getCriteriaBuilder().lt(x, y);
    }

    @Override
    public Predicate le(Expression<? extends Number> x, Expression<? extends Number> y) {
        return this.getCriteriaBuilder().le(x, y);
    }

    @Override
    public Predicate le(Expression<? extends Number> x, Number y) {
        return this.getCriteriaBuilder().le(x, y);
    }

    @Override
    public <N extends Number> Expression<N> neg(Expression<N> expr) {
        return this.getCriteriaBuilder().neg(expr);
    }

    @Override
    public <N extends Number> Expression<N> abs(Expression<N> expr) {
        return this.getCriteriaBuilder().abs(expr);
    }

    @Override
    public <N extends Number> Expression<N> sum(Expression<? extends N> x, Expression<? extends N> y) {
        return this.getCriteriaBuilder().sum(x, y);
    }

    @Override
    public <N extends Number> Expression<N> sum(Expression<? extends N> x, N y) {
        return this.getCriteriaBuilder().sum(x, y);
    }

    @Override
    public <N extends Number> Expression<N> sum(N x, Expression<? extends N> y) {
        return this.getCriteriaBuilder().sum(x, y);
    }

    @Override
    public <N extends Number> Expression<N> prod(Expression<? extends N> x, Expression<? extends N> y) {
        return this.getCriteriaBuilder().prod(x, y);
    }

    @Override
    public <N extends Number> Expression<N> prod(Expression<? extends N> x, N y) {
        return this.getCriteriaBuilder().prod(x, y);
    }

    @Override
    public <N extends Number> Expression<N> prod(N x, Expression<? extends N> y) {
        return this.getCriteriaBuilder().prod(x, y);
    }

    @Override
    public <N extends Number> Expression<N> diff(Expression<? extends N> x, Expression<? extends N> y) {
        return this.getCriteriaBuilder().diff(x, y);
    }

    @Override
    public <N extends Number> Expression<N> diff(Expression<? extends N> x, N y) {
        return this.getCriteriaBuilder().diff(x, y);
    }

    @Override
    public <N extends Number> Expression<N> diff(N x, Expression<? extends N> y) {
        return this.getCriteriaBuilder().diff(x, y);
    }

    @Override
    public Expression<Number> quot(Expression<? extends Number> x, Expression<? extends Number> y) {
        return this.getCriteriaBuilder().quot(x, y);
    }

    @Override
    public Expression<Number> quot(Expression<? extends Number> x, Number y) {
        return this.getCriteriaBuilder().quot(x, y);
    }

    @Override
    public Expression<Number> quot(Number x, Expression<? extends Number> y) {
        return this.getCriteriaBuilder().quot(x, y);
    }

    @Override
    public Expression<Integer> mod(Expression<Integer> x, Expression<Integer> y) {
        return this.getCriteriaBuilder().mod(x, y);
    }

    @Override
    public Expression<Integer> mod(Expression<Integer> x, Integer y) {
        return this.getCriteriaBuilder().mod(x, y);
    }

    @Override
    public Expression<Integer> mod(Integer x, Expression<Integer> y) {
        return this.getCriteriaBuilder().mod(x, y);
    }

    @Override
    public Expression<Double> sqrt(Expression<? extends Number> expr) {
        return this.getCriteriaBuilder().sqrt(expr);
    }

    @Override
    public Expression<Long> toLong(Expression<? extends Number> expr) {
        return this.getCriteriaBuilder().toLong(expr);
    }

    @Override
    public Expression<Integer> toInteger(Expression<? extends Number> expr) {
        return this.getCriteriaBuilder().toInteger(expr);
    }

    @Override
    public Expression<Float> toFloat(Expression<? extends Number> expr) {
        return this.getCriteriaBuilder().toFloat(expr);
    }

    @Override
    public Expression<Double> toDouble(Expression<? extends Number> expr) {
        return this.getCriteriaBuilder().toDouble(expr);
    }

    @Override
    public Expression<java.math.BigDecimal> toBigDecimal(Expression<? extends Number> expr) {
        return this.getCriteriaBuilder().toBigDecimal(expr);
    }

    @Override
    public Expression<java.math.BigInteger> toBigInteger(Expression<? extends Number> expr) {
        return this.getCriteriaBuilder().toBigInteger(expr);
    }

    @Override
    public Expression<String> toString(Expression<Character> expr) {
        return this.getCriteriaBuilder().toString(expr);
    }

    @Override
    public <T> Expression<T> literal(T expr) {
        return this.getCriteriaBuilder().literal(expr);
    }

    @Override
    public <T> Expression<T> nullLiteral(Class<T> type) {
        return this.getCriteriaBuilder().nullLiteral(type);
    }

    @Override
    public <T> ParameterExpression<T> parameter(Class<T> type) {
        return this.getCriteriaBuilder().parameter(type);
    }

    @Override
    public <T> ParameterExpression<T> parameter(Class<T> type, String name) {
        return this.getCriteriaBuilder().parameter(type, name);
    }

    @Override
    public <C extends Collection<?>> Predicate isEmpty(Expression<C> expr) {
        return this.getCriteriaBuilder().isEmpty(expr);
    }

    @Override
    public <C extends Collection<?>> Predicate isNotEmpty(Expression<C> expr) {
        return this.getCriteriaBuilder().isNotEmpty(expr);
    }

    @Override
    public <C extends Collection<?>> Expression<Integer> size(Expression<C> expr) {
        return this.getCriteriaBuilder().size(expr);
    }

    @Override
    public <C extends Collection<?>> Expression<Integer> size(C expr) {
        return this.getCriteriaBuilder().size(expr);
    }

    @Override
    public <E, C extends Collection<E>> Predicate isMember(Expression<E> x, Expression<C> y) {
        return this.getCriteriaBuilder().isMember(x, y);
    }

    @Override
    public <E, C extends Collection<E>> Predicate isMember(E x, Expression<C> y) {
        return this.getCriteriaBuilder().isMember(x, y);
    }

    @Override
    public <E, C extends Collection<E>> Predicate isNotMember(Expression<E> x, Expression<C> y) {
        return this.getCriteriaBuilder().isNotMember(x, y);
    }

    @Override
    public <E, C extends Collection<E>> Predicate isNotMember(E x, Expression<C> y) {
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
        return this.getCriteriaBuilder().like(x, y);
    }

    @Override
    public Predicate like(Expression<String> x, String y) {
        return this.getCriteriaBuilder().like(x, y);
    }

    @Override
    public Predicate like(Expression<String> x, Expression<String> y, Expression<Character> esc) {
        return this.getCriteriaBuilder().like(x, y, esc);
    }

    @Override
    public Predicate like(Expression<String> x, Expression<String> y, char esc) {
        return this.getCriteriaBuilder().like(x, y, esc);
    }

    @Override
    public Predicate like(Expression<String> x, String y, Expression<Character> esc) {
        return this.getCriteriaBuilder().like(x, y, esc);
    }

    @Override
    public Predicate like(Expression<String> x, String y, char esc) {
        return this.getCriteriaBuilder().like(x, y, esc);
    }

    @Override
    public Predicate notLike(Expression<String> x, Expression<String> y) {
        return this.getCriteriaBuilder().notLike(x, y);
    }

    @Override
    public Predicate notLike(Expression<String> x, String y) {
        return this.getCriteriaBuilder().notLike(x, y);
    }

    @Override
    public Predicate notLike(Expression<String> x, Expression<String> y, Expression<Character> esc) {
        return this.getCriteriaBuilder().notLike(x, y, esc);
    }

    @Override
    public Predicate notLike(Expression<String> x, Expression<String> y, char esc) {
        return this.getCriteriaBuilder().notLike(x, y, esc);
    }

    @Override
    public Predicate notLike(Expression<String> x, String y, Expression<Character> esc) {
        return this.getCriteriaBuilder().notLike(x, y, esc);
    }

    @Override
    public Predicate notLike(Expression<String> x, String y, char esc) {
        return this.getCriteriaBuilder().notLike(x, y, esc);
    }

    @Override
    public Expression<String> concat(Expression<String> x, Expression<String> y) {
        return this.getCriteriaBuilder().concat(x, y);
    }

    @Override
    public Expression<String> concat(Expression<String> x, String y) {
        return this.getCriteriaBuilder().concat(x, y);
    }

    @Override
    public Expression<String> concat(String x, Expression<String> y) {
        return this.getCriteriaBuilder().concat(x, y);
    }

    @Override
    public Expression<String> substring(Expression<String> x, Expression<Integer> y) {
        return this.getCriteriaBuilder().substring(x, y);
    }

    @Override
    public Expression<String> substring(Expression<String> x, int y) {
        return this.getCriteriaBuilder().substring(x, y);
    }

    @Override
    public Expression<String> substring(Expression<String> x, Expression<Integer> y, Expression<Integer> z) {
        return this.getCriteriaBuilder().substring(x, y, z);
    }

    @Override
    public Expression<String> substring(Expression<String> x, int y, int z) {
        return this.getCriteriaBuilder().substring(x, y, z);
    }

    @Override
    public Expression<String> trim(Expression<String> x) {
        return this.getCriteriaBuilder().trim(x);
    }

    @Override
    public Expression<String> trim(CriteriaBuilder.Trimspec x, Expression<String> y) {
        return this.getCriteriaBuilder().trim(x, y);
    }

    @Override
    public Expression<String> trim(Expression<Character> x, Expression<String> y) {
        return this.getCriteriaBuilder().trim(x, y);
    }

    @Override
    public Expression<String> trim(CriteriaBuilder.Trimspec x, Expression<Character> y, Expression<String> z) {
        return this.getCriteriaBuilder().trim(x, y, z);
    }

    @Override
    public Expression<String> trim(char x, Expression<String> y) {
        return this.getCriteriaBuilder().trim(x, y);
    }

    @Override
    public Expression<String> trim(CriteriaBuilder.Trimspec x, char y, Expression<String> z) {
        return this.getCriteriaBuilder().trim(x, y, z);
    }

    @Override
    public Expression<String> lower(Expression<String> expr) {
        return this.getCriteriaBuilder().lower(expr);
    }

    @Override
    public Expression<String> upper(Expression<String> expr) {
        return this.getCriteriaBuilder().upper(expr);
    }

    @Override
    public Expression<Integer> length(Expression<String> expr) {
        return this.getCriteriaBuilder().length(expr);
    }

    @Override
    public Expression<Integer> locate(Expression<String> x, Expression<String> y) {
        return this.getCriteriaBuilder().locate(x, y);
    }

    @Override
    public Expression<Integer> locate(Expression<String> x, String y) {
        return this.getCriteriaBuilder().locate(x, y);
    }

    @Override
    public Expression<Integer> locate(Expression<String> x, Expression<String> y, Expression<Integer> z) {
        return this.getCriteriaBuilder().locate(x, y, z);
    }

    @Override
    public Expression<Integer> locate(Expression<String> x, String y, int z) {
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
        return this.getCriteriaBuilder().in(expr);
    }

    @Override
    public <Y> Expression<Y> coalesce(Expression<? extends Y> x, Expression<? extends Y> y) {
        return this.getCriteriaBuilder().coalesce(x, y);
    }

    @Override
    public <Y> Expression<Y> coalesce(Expression<? extends Y> x, Y y) {
        return this.getCriteriaBuilder().coalesce(x, y);
    }

    @Override
    public <Y> Expression<Y> nullif(Expression<Y> x, Expression<?> y) {
        return this.getCriteriaBuilder().nullif(x, y);
    }

    @Override
    public <Y> Expression<Y> nullif(Expression<Y> x, Y y) {
        return this.getCriteriaBuilder().nullif(x, y);
    }

    @Override
    public <T> CriteriaBuilder.Coalesce<T> coalesce() {
        return this.getCriteriaBuilder().coalesce();
    }

    @Override
    public <C, R> CriteriaBuilder.SimpleCase<C, R> selectCase(Expression<? extends C> expr) {
        return this.getCriteriaBuilder().selectCase(expr);
    }

    @Override
    public <R> CriteriaBuilder.Case<R> selectCase() {
        return this.getCriteriaBuilder().selectCase();
    }

    @Override
    public <T> Expression<T> function(String name, Class<T> type, Expression<?>... exprs) {
        return this.getCriteriaBuilder().function(name, type, exprs);
    }

    @Override
    public <X, T, V extends T> Join<X, V> treat(Join<X, T> join, Class<V> type) {
        return this.getCriteriaBuilder().treat(join, type);
    }

    @Override
    public <X, T, E extends T> CollectionJoin<X, E> treat(CollectionJoin<X, T> join, Class<E> type) {
        return this.getCriteriaBuilder().treat(join, type);
    }

    @Override
    public <X, T, E extends T> SetJoin<X, E> treat(SetJoin<X, T> join, Class<E> type) {
        return this.getCriteriaBuilder().treat(join, type);
    }

    @Override
    public <X, T, E extends T> ListJoin<X, E> treat(ListJoin<X, T> join, Class<E> type) {
        return this.getCriteriaBuilder().treat(join, type);
    }

    @Override
    public <X, K, T, V extends T> MapJoin<X, K, V> treat(MapJoin<X, K, T> join, Class<V> type) {
        return this.getCriteriaBuilder().treat(join, type);
    }

    @Override
    public <X, T extends X> Path<T> treat(Path<X> path, Class<T> type) {
        return this.getCriteriaBuilder().treat(path, type);
    }

    @Override
    public <X, T extends X> Root<T> treat(Root<X> root, Class<T> type) {
        return this.getCriteriaBuilder().treat(root, type);
    }
}


/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CollectionJoin;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.SetJoin;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

/**
 * Builder for JPA criteria search queries, based on configuration through a {@link java.util.stream.Stream}-like API.
 *
 * @param <X> stream item type
 * @param <S> criteria type for stream item
 */
public interface SearchStream<X, S extends Selection<X>>
  extends QueryStream<X, S, AbstractQuery<?>, CriteriaQuery<X>, TypedQuery<X>> {

    /**
     * Build and evaluate a JPA query based on this instance and return the result list.
     *
     * <p>
     * Delegates to {@link TypedQuery#getResultList} and can throw any exception thrown by that method.
     *
     * @return result of executed query
     */
    default List<X> getResultList() {
        return this.toQuery().getResultList();
    }

// CriteriaQuery stuff

    /**
     * Suppress duplicates.
     *
     * @return a new stream with duplicates removed
     */
    SearchStream<X, S> distinct();

    /**
     * Order results using the specified property.
     *
     * <p>
     * Replaces any existing sort ordering.
     *
     * @param attribute associated property
     * @param asc true for ascending, false for descending
     * @return a new stream with specified ordering
     * @throws IllegalArgumentException if {@code attribute} is null
     */
    SearchStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute, boolean asc);

    /**
     * Order results using the specified expression reference.
     *
     * <p>
     * Replaces any existing sort ordering.
     *
     * @param ref previously bound expression reference
     * @param asc true for ascending, false for descending
     * @return a new stream with specified ordering
     * @throws IllegalArgumentException if {@code ref} is null
     */
    SearchStream<X, S> orderBy(Ref<?, ? extends Expression<?>> ref, boolean asc);

    /**
     * Order results using the {@link Expression} produced by the given {@link Function}.
     *
     * <p>
     * Replaces any existing sort ordering.
     *
     * @param orderExprFunction {@link Function} that produces an {@link Expression} to order on given an item expression
     * @param asc true for ascending, false for descending
     * @return a new stream with specified ordering
     * @throws IllegalArgumentException if {@code orderExprFunction} is null
     */
    SearchStream<X, S> orderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc);

    /**
     * Order results using the {@link Order} list produced by the given {@link Function}.
     *
     * <p>
     * Replaces any existing sort ordering.
     *
     * @param orderListFunction {@link Function} that produces the sort ordering given an item expression
     * @return a new stream with specified ordering
     * @throws IllegalArgumentException if {@code orderListFunction} is null
     */
    SearchStream<X, S> orderByMulti(Function<? super S, ? extends List<? extends Order>> orderListFunction);

    /**
     * Apply grouping based on an expression refernce.
     *
     * <p>
     * Adds to any previously specified groupings.
     *
     * @param ref previously bound expression reference
     * @return a new stream with additional grouping
     * @throws IllegalArgumentException if {@code ref} is null
     */
    SearchStream<X, S> groupBy(Ref<?, ? extends Expression<?>> ref);

    /**
     * Apply grouping based on the specified property.
     *
     * <p>
     * Adds to any previously specified groupings.
     *
     * @param attribute associated property
     * @return a new stream with additional grouping
     * @throws IllegalArgumentException if {@code attribute} is null
     */
    SearchStream<X, S> groupBy(SingularAttribute<? super X, ?> attribute);

    /**
     * Apply grouping based on a single expression.
     *
     * <p>
     * Adds to any previously specified groupings.
     *
     * @param groupFunction function returning an expression by which to group results
     * @return a new stream with additional grouping
     * @throws IllegalArgumentException if {@code groupFunction} is null
     */
    SearchStream<X, S> groupBy(Function<? super S, ? extends Expression<?>> groupFunction);

    /**
     * Apply grouping based on a list of expressions.
     *
     * <p>
     * Adds to any previously specified groupings.
     *
     * @param groupFunction function returning a list of expressions by which to group results
     * @return a new instance
     * @throws IllegalArgumentException if {@code groupFunction} is null
     */
    SearchStream<X, S> groupByMulti(Function<? super S, ? extends List<Expression<?>>> groupFunction);

    /**
     * Add a "having" restriction.
     *
     * <p>
     * Adds to any previously specified "having" restrictions.
     *
     * @param havingFunction function returning a test to apply to grouped results
     * @return a new instance
     * @throws IllegalArgumentException if {@code havingFunction} is null
     */
    SearchStream<X, S> having(Function<? super S, ? extends Expression<Boolean>> havingFunction);

// Streamy stuff

    /**
     * Find any instance in the stream.
     *
     * @return single-valued stream containg any instance in this stream (or {@code NULL} if this stream is empty)
     */
    SearchValue<X, S> findAny();

    /**
     * Find the first instance in the stream.
     *
     * @return single-valued stream containg the first instance in this stream (or {@code NULL} if this stream is empty)
     */
    SearchValue<X, S> findFirst();

// Binding

    /**
     * Bind an unbound reference to a new query root that will be added to the query.
     *
     * <p>
     * To select the new root in a {@link SearchStream}, use {@link SearchStream#map(Class, Function) SearchStream.map()},
     * providing a {@link Function} that returns {@code ref}.
     *
     * <p>
     * Note that this effectively creates an unconstrained (cross product) join with the new root.
     * Typically there would be some additional restrictions imposed (e.g., via {@link #filter filter()})
     * to relate the new root to the items in the stream.
     *
     * @param ref unbound reference
     * @param type type of the new query root
     * @param <R> type of the new query root
     * @throws IllegalArgumentException if {@code ref} is already bound
     * @throws IllegalArgumentException if {@code type} or {@code ref} is null
     * @return new stream that binds {@code ref} to a new query root from {@code type}
     */
    <R> SearchStream<X, S> addRoot(Ref<R, ? super Root<R>> ref, Class<R> type);

// Mapping

    /**
     * Map this stream to an associated property.
     *
     * @param attribute associated property
     * @param <Y> property type
     * @return mapped stream
     */
    default <Y> PathStream<Y, Path<Y>> map(SingularAttribute<? super X, Y> attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        return new PathStreamImpl<>(this.getEntityManager(), new SearchType<>(attribute.getJavaType()), (builder, query) -> {

            // We always prefer having a From over a Path, because you can continue to join with it.
            // If you say path.get() you get a Path, even if you could have said path.join() to get a From.
            // So when mapping a SingluarAttribute, path.join() is the same as path.get() but better.
            final Path<X> path = (Path<X>)this.configure(builder, query);   // cast must be valid if attribute exists
            return path instanceof From ? ((From<?, X>)path).join(attribute) : path.get(attribute);
        });
    }

    /**
     * Map this stream into a stream whose elements are the result of applying the given function.
     *
     * @param type new item type
     * @param exprFunction function mapping this stream's {@link Expression} to a {@link Expression}
     * @param <Y> mapped expression type
     * @return mapped stream
     */
    default <Y> ExprStream<Y, Expression<Y>> map(Class<Y> type, Function<? super S, ? extends Expression<Y>> exprFunction) {
        if (type == null)
            throw new IllegalArgumentException("null type");
        if (exprFunction == null)
            throw new IllegalArgumentException("null exprFunction");
        return new ExprStreamImpl<>(this.getEntityManager(), new SearchType<Y>(type),
          (builder, query) -> exprFunction.apply(this.configure(builder, query)));
    }

    /**
     * Map this stream to an associated collection property.
     *
     * @param attribute associated property
     * @param <E> collection element type
     * @param <C> collection type
     * @return mapped stream
     */
    @SuppressWarnings("unchecked")
    default <E, C extends Collection<E>> ExprStream<C, Expression<C>> map(PluralAttribute<? super X, C, E> attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        // This is necessary due to https://github.com/javaee/jpa-spec/issues/108
        final PluralAttribute<X, C, E> attribute2 = (PluralAttribute<X, C, E>)attribute;
        return new ExprStreamImpl<>(this.getEntityManager(), new SearchType<>(attribute.getJavaType()),
          (builder, query) -> ((Path<X>)this.configure(builder, query)).get(attribute2)); // cast must be valid if attribute exists
    }

    /**
     * Map this stream to an associated map property.
     *
     * @param attribute associated property
     * @param <K> map key type
     * @param <V> map value type
     * @param <M> map type
     * @return mapped stream
     */
    @SuppressWarnings("unchecked")
    default <K, V, M extends Map<K, V>> ExprStream<M, Expression<M>> map(MapAttribute<? super X, K, V> attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        // This is necessary due to https://github.com/javaee/jpa-spec/issues/108
        final MapAttribute<X, K, V> attribute2 = (MapAttribute<X, K, V>)attribute;
        return new ExprStreamImpl<>(this.getEntityManager(), new SearchType<>((Class<M>)attribute.getJavaType()),
          (builder, query) -> ((Path<X>)this.configure(builder, query)).get(attribute2)); // cast must be valid if attribute exists
    }

    /**
     * Map this stream into a stream whose elements are the result of applying the given function.
     *
     * @param type new item type
     * @param pathFunction function mapping this stream's {@link Expression} to a {@link Path}
     * @param <Y> mapped expresssion type
     * @return mapped stream
     */
    default <Y> PathStream<Y, Path<Y>> mapToPath(Class<Y> type, Function<? super S, ? extends Path<Y>> pathFunction) {
        if (type == null)
            throw new IllegalArgumentException("null type");
        if (pathFunction == null)
            throw new IllegalArgumentException("null pathFunction");
        return new PathStreamImpl<>(this.getEntityManager(), new SearchType<Y>(type),
          (builder, query) -> pathFunction.apply(this.configure(builder, query)));
    }

    /**
     * Map this stream into a stream whose elements are the result of applying the given function.
     *
     * @param type new item type
     * @param fromFunction function mapping this stream's {@link Expression} to a {@link From}
     * @param <Z> mapped source type
     * @param <Y> mapped target type
     * @return mapped stream
     */
    default <Z, Y> FromStream<Y, From<Z, Y>> mapToFrom(Class<Y> type, Function<? super S, ? extends From<Z, Y>> fromFunction) {
        if (type == null)
            throw new IllegalArgumentException("null type");
        if (fromFunction == null)
            throw new IllegalArgumentException("null fromFunction");
        return new FromStreamImpl<>(this.getEntityManager(), new SearchType<Y>(type),
          (builder, query) -> fromFunction.apply(this.configure(builder, query)));
    }

    /**
     * Map this stream to an associated floating point value.
     *
     * @param attribute associated numerically-valued property
     * @return mapped stream of doubles
     */
    default DoubleStream mapToDouble(SingularAttribute<? super X, ? extends Number> attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        return new DoubleStreamImpl(this.getEntityManager(),
          (builder, query) -> builder.toDouble(((Path<X>)this.configure(builder, query)).get(attribute))); // cast must be valid...
    }

    /**
     * Map this stream into a stream of double values that are the result of applying the given function.
     *
     * @param doubleExprFunction function mapping this stream's {@link Expression} to a numerical {@link Expression}
     * @return mapped stream of doubles
     */
    default DoubleStream mapToDouble(Function<? super S, ? extends Expression<? extends Number>> doubleExprFunction) {
        if (doubleExprFunction == null)
            throw new IllegalArgumentException("null doubleExprFunction");
        return new DoubleStreamImpl(this.getEntityManager(),
          (builder, query) -> builder.toDouble(doubleExprFunction.apply(this.configure(builder, query))));
    }

    /**
     * Map this stream to an associated long value.
     *
     * @param attribute associated numerically-valued property
     * @return mapped stream of longs
     */
    default LongStream mapToLong(SingularAttribute<? super X, ? extends Number> attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        return new LongStreamImpl(this.getEntityManager(),
          (builder, query) -> builder.toLong(((Path<X>)this.configure(builder, query)).get(attribute))); // cast must be valid...
    }

    /**
     * Map this stream into a stream of long values that are the result of applying the given function.
     *
     * @param longExprFunction function mapping this stream's {@link Expression} to a numerical {@link Expression}
     * @return mapped stream of longs
     */
    default LongStream mapToLong(Function<? super S, ? extends Expression<? extends Number>> longExprFunction) {
        if (longExprFunction == null)
            throw new IllegalArgumentException("null longExprFunction");
        return new LongStreamImpl(this.getEntityManager(),
          (builder, query) -> builder.toLong(longExprFunction.apply(this.configure(builder, query))));
    }

    /**
     * Map this stream to an associated integer value.
     *
     * @param attribute associated numerically-valued property
     * @return mapped stream of integers
     */
    default IntStream mapToInt(SingularAttribute<? super X, ? extends Number> attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        return new IntStreamImpl(this.getEntityManager(),
          (builder, query) -> builder.toInteger(((Path<X>)this.configure(builder, query)).get(attribute))); // cast must be valid...
    }

    /**
     * Map this stream into a stream of integer values that are the result of applying the given function.
     *
     * @param intExprFunction function mapping this stream's {@link Expression} to a numerical {@link Expression}
     * @return mapped stream of integers
     */
    default IntStream mapToInt(Function<? super S, ? extends Expression<? extends Number>> intExprFunction) {
        if (intExprFunction == null)
            throw new IllegalArgumentException("null intExprFunction");
        return new IntStreamImpl(this.getEntityManager(),
          (builder, query) -> builder.toInteger(intExprFunction.apply(this.configure(builder, query))));
    }

    /**
     * Map this stream into a stream whose elements are the result of applying the given function.
     *
     * <p>
     * This method provides support for multiple selection using
     * {@link javax.persistence.criteria.CriteriaBuilder#array CriteriaBuilder.array()},
     * {@link javax.persistence.criteria.CriteriaBuilder#construct CriteriaBuilder.construct()}, or
     * {@link javax.persistence.criteria.CriteriaBuilder#tuple CriteriaBuilder.tuple()}.
     * For normal single selection, typically you would use {@link SearchStream#map SearchStream.map()} instead of this method.
     *
     * @param type selection type
     * @param selectionFunction function to build selection
     * @param <Y> selection type
     * @return mapped stream
     */
    default <Y> SearchStream<Y, Selection<Y>> mapToSelection(Class<Y> type,
      Function<? super S, ? extends Selection<Y>> selectionFunction) {
        if (type == null)
            throw new IllegalArgumentException("null type");
        if (selectionFunction == null)
            throw new IllegalArgumentException("null selectionFunction");
        return new SearchStreamImpl<>(this.getEntityManager(), new SearchType<Y>(type),
          (builder, query) -> selectionFunction.apply(this.configure(builder, query)));
    }

// Flat Mapping

    /**
     * Map this stream to a stream where every item is replaced with the contents of the specified collection.
     *
     * @param attribute associated collection property
     * @param <E> collection element type
     * @return mapped stream
     */
    default <E> FromStream<E, CollectionJoin<X, E>> flatMap(CollectionAttribute<? super X, E> attribute) {
        return this.join(attribute);
    }

    /**
     * Map this stream to a stream where every item is replaced with the contents of the specified collection.
     *
     * @param attribute associated collection property
     * @param <E> list element type
     * @return mapped stream
     */
    default <E> FromStream<E, ListJoin<X, E>> flatMap(ListAttribute<? super X, E> attribute) {
        return this.join(attribute);
    }

    /**
     * Map this stream to a stream where every item is replaced with the contents of the specified collection.
     *
     * @param attribute associated collection property
     * @param <E> set element type
     * @return mapped stream
     */
    default <E> FromStream<E, SetJoin<X, E>> flatMap(SetAttribute<? super X, E> attribute) {
        return this.join(attribute);
    }

    /**
     * Map this stream to a stream where every item is replaced with the keys of the specified map.
     *
     * @param attribute associated map property
     * @param <K> map key type
     * @return mapped stream
     */
    default <K> PathStream<K, Path<K>> flatMapKeys(MapAttribute<? super X, K, ?> attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        return new PathStreamImpl<>(this.getEntityManager(), new SearchType<>(attribute.getKeyJavaType()),
          (builder, query) -> ((From<?, X>)this.configure(builder, query)).join(attribute, JoinType.INNER).key());      // valid...
    }

    /**
     * Map this stream to a stream where every item is replaced with the values of the specified map.
     *
     * @param attribute associated map property
     * @param <V> map value type
     * @return mapped stream
     */
    default <V> PathStream<V, Path<V>> flatMapValues(MapAttribute<? super X, ?, V> attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        return new PathStreamImpl<>(this.getEntityManager(), new SearchType<>(attribute.getElementType().getJavaType()),
          (builder, query) -> ((From<?, X>)this.configure(builder, query)).join(attribute, JoinType.INNER).value());    // valid...
    }

// Singluar joins

    default <Y> FromStream<Y, From<X, Y>> join(SingularAttribute<? super X, Y> attribute) {
        return this.join(attribute, JoinType.INNER);
    }

    default <Y> FromStream<Y, From<X, Y>> join(SingularAttribute<? super X, Y> attribute, JoinType joinType) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        if (!attribute.isAssociation())
            throw new IllegalArgumentException("attribute is not an association: " + attribute);
        if (joinType == null)
            throw new IllegalArgumentException("null joinType");
        return new FromStreamImpl<>(this.getEntityManager(), new SearchType<>(attribute.getJavaType()),
           (builder, query) -> ((From<?, X>)this.configure(builder, query)).join(attribute, joinType)); // cast must be valid...
    }

// Plural Joins

    default <E> FromStream<E, CollectionJoin<X, E>> join(CollectionAttribute<? super X, E> attribute) {
        return this.join(attribute, JoinType.INNER);
    }

    default <E> FromStream<E, CollectionJoin<X, E>> join(CollectionAttribute<? super X, E> attribute, JoinType joinType) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        if (!attribute.isAssociation())
            throw new IllegalArgumentException("attribute is not an association: " + attribute);
        if (joinType == null)
            throw new IllegalArgumentException("null joinType");
        return new FromStreamImpl<>(this.getEntityManager(), new SearchType<>(attribute.getElementType().getJavaType()),
          (builder, query) -> ((From<?, X>)this.configure(builder, query)).join(attribute, joinType));  // cast must be valid...
    }

    default <E> FromStream<E, ListJoin<X, E>> join(ListAttribute<? super X, E> attribute) {
        return this.join(attribute, JoinType.INNER);
    }

    default <E> FromStream<E, ListJoin<X, E>> join(ListAttribute<? super X, E> attribute, JoinType joinType) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        if (!attribute.isAssociation())
            throw new IllegalArgumentException("attribute is not an association: " + attribute);
        if (joinType == null)
            throw new IllegalArgumentException("null joinType");
        return new FromStreamImpl<>(this.getEntityManager(), new SearchType<>(attribute.getElementType().getJavaType()),
          (builder, query) -> ((From<?, X>)this.configure(builder, query)).join(attribute, joinType));  // cast must be valid...
    }

    default <E> FromStream<E, SetJoin<X, E>> join(SetAttribute<? super X, E> attribute) {
        return this.join(attribute, JoinType.INNER);
    }

    default <E> FromStream<E, SetJoin<X, E>> join(SetAttribute<? super X, E> attribute, JoinType joinType) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        if (!attribute.isAssociation())
            throw new IllegalArgumentException("attribute is not an association: " + attribute);
        if (joinType == null)
            throw new IllegalArgumentException("null joinType");
        return new FromStreamImpl<>(this.getEntityManager(), new SearchType<>(attribute.getElementType().getJavaType()),
          (builder, query) -> ((From<?, X>)this.configure(builder, query)).join(attribute, joinType));  // cast must be valid...
    }

    default <K, V> FromStream<V, MapJoin<X, K, V>> join(MapAttribute<? super X, K, V> attribute) {
        return this.join(attribute, JoinType.INNER);
    }

    default <K, V> FromStream<V, MapJoin<X, K, V>> join(MapAttribute<? super X, K, V> attribute, JoinType joinType) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        if (!attribute.isAssociation())
            throw new IllegalArgumentException("attribute is not an association: " + attribute);
        if (joinType == null)
            throw new IllegalArgumentException("null joinType");
        return new FromStreamImpl<>(this.getEntityManager(), new SearchType<>(attribute.getElementType().getJavaType()),
          (builder, query) -> ((From<?, X>)this.configure(builder, query)).join(attribute, joinType));  // cast must be valid...
    }

// Narrowing overrides (QueryStream)

    @Override
    SearchStream<X, S> bind(Ref<X, ? super S> ref);

    @Override
    <X2, S2 extends Selection<X2>> SearchStream<X, S> bind(Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction);

    @Override
    SearchStream<X, S> filter(SingularAttribute<? super X, Boolean> attribute);

    @Override
    SearchStream<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder);
}

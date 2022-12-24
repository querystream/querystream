
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CollectionJoin;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
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
     * @see TypedQuery#getResultList
     */
    default List<X> getResultList() {
        return this.toQuery().getResultList();
    }

    /**
     * Build and evaluate a JPA query based on this instance and return the result stream.
     *
     * <p>
     * Delegates to {@link TypedQuery#getResultStream} and can throw any exception thrown by that method.
     *
     * @return result of executed query
     * @see TypedQuery#getResultStream
     */
    default Stream<X> getResultStream() {
        return this.toQuery().getResultStream();
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
     * Order results using the specified properties.
     *
     * <p>
     * Replaces any existing sort ordering.
     *
     * @param attribute1 associated property
     * @param asc1 true for {@code attribute1} ascending, false for {@code attribute1} descending
     * @param attribute2 associated property
     * @param asc2 true for {@code attribute2} ascending, false for {@code attribute2} descending
     * @return a new stream with specified ordering
     * @throws IllegalArgumentException if {@code attribute1} or {@code attribute2} is null
     */
    SearchStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2);

    /**
     * Order results using the specified properties.
     *
     * <p>
     * Replaces any existing sort ordering.
     *
     * @param attribute1 associated property
     * @param asc1 true for {@code attribute1} ascending, false for {@code attribute1} descending
     * @param attribute2 associated property
     * @param asc2 true for {@code attribute2} ascending, false for {@code attribute2} descending
     * @param attribute3 associated property
     * @param asc3 true for {@code attribute3} ascending, false for {@code attribute3} descending
     * @return a new stream with specified ordering
     * @throws IllegalArgumentException if {@code attribute1}, {@code attribute2}, or {@code attribute3} is null
     */
    SearchStream<X, S> orderBy(SingularAttribute<? super X, ?> attribute1, boolean asc1,
      SingularAttribute<? super X, ?> attribute2, boolean asc2, SingularAttribute<? super X, ?> attribute3, boolean asc3);

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
     * Order results using the specified {@link Order}s.
     *
     * <p>
     * Replaces any existing sort ordering.
     *
     * @param orders ordering(s), with higher precedence orderings first
     * @return a new stream with specified ordering(s)
     * @throws IllegalArgumentException if {@code orders} is null
     */
    SearchStream<X, S> orderBy(Order... orders);

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
     * Order results using the specified property after existing sort.
     *
     * <p>
     * Adds to any existing sort ordering.
     *
     * @param attribute associated property
     * @param asc true for ascending, false for descending
     * @return a new stream with specified additional ordering
     * @throws IllegalArgumentException if {@code attribute} is null
     */
    SearchStream<X, S> thenOrderBy(SingularAttribute<? super X, ?> attribute, boolean asc);

    /**
     * Order results using the specified expression reference after existing sort.
     *
     * <p>
     * Adds to any existing sort ordering.
     *
     * @param ref previously bound expression reference
     * @param asc true for ascending, false for descending
     * @return a new stream with specified additional ordering
     * @throws IllegalArgumentException if {@code ref} is null
     */
    SearchStream<X, S> thenOrderBy(Ref<?, ? extends Expression<?>> ref, boolean asc);

    /**
     * Order results using the specified {@link Order}s after existing sort.
     *
     * <p>
     * Adds to any existing sort ordering.
     *
     * @param orders ordering(s), with higher precedence orderings first
     * @return a new stream with specified additional ordering(s)
     * @throws IllegalArgumentException if {@code orders} is null
     */
    SearchStream<X, S> thenOrderBy(Order... orders);

    /**
     * Order results using the {@link Expression} produced by the given {@link Function} after existing sort.
     *
     * <p>
     * Adds to any existing sort ordering.
     *
     * @param orderExprFunction {@link Function} that produces an {@link Expression} to order on given an item expression
     * @param asc true for ascending, false for descending
     * @return a new stream with specified additional ordering
     * @throws IllegalArgumentException if {@code orderExprFunction} is null
     */
    SearchStream<X, S> thenOrderBy(Function<? super S, ? extends Expression<?>> orderExprFunction, boolean asc);

    /**
     * Apply grouping based on an expression reference.
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
     * Execute this query and return true if the given property is true for every resulting item.
     *
     * <p>
     * Delegates to {@link TypedQuery#getResultStream} and can throw any exception thrown by that method.
     *
     * @param attribute boolean property
     * @return true if all results have the given property true
     */
    boolean allMatch(SingularAttribute<? super X, Boolean> attribute);

    /**
     * Execute this query and return true if the predicate returned by the given function is true for every resulting item.
     *
     * <p>
     * Delegates to {@link TypedQuery#getResultStream} and can throw any exception thrown by that method.
     *
     * @param predicateBuilder function mapping this stream's item to a boolean {@link Expression}
     * @return true if all results have the computed expression true
     */
    boolean allMatch(Function<? super S, ? extends Expression<Boolean>> predicateBuilder);

    /**
     * Execute this query and return true if any results are found for which the given property is true.
     *
     * <p>
     * Delegates to {@link TypedQuery#getResultStream} and can throw any exception thrown by that method.
     *
     * @param attribute boolean property
     * @return true if one or more results have the given property true
     */
    boolean anyMatch(SingularAttribute<? super X, Boolean> attribute);

    /**
     * Execute this query and return true if any results are found for which the predicate returned by the given function is true.
     *
     * <p>
     * Delegates to {@link TypedQuery#getResultStream} and can throw any exception thrown by that method.
     *
     * @param predicateBuilder function mapping this stream's item to a boolean {@link Expression}
     * @return true if one or more results have the computed expression true
     */
    boolean anyMatch(Function<? super S, ? extends Expression<Boolean>> predicateBuilder);

    /**
     * Execute this query and return true if no results are found for which the given property is true.
     *
     * <p>
     * Delegates to {@link TypedQuery#getResultStream} and can throw any exception thrown by that method.
     *
     * @param attribute boolean property
     * @return true if no results have the given property true
     */
    boolean noneMatch(SingularAttribute<? super X, Boolean> attribute);

    /**
     * Execute this query and return true if no result are found for which the predicate returned by the given function is true.
     *
     * <p>
     * Delegates to {@link TypedQuery#getResultStream} and can throw any exception thrown by that method.
     *
     * @param predicateBuilder function mapping this stream's item to a boolean {@link Expression}
     * @return true if no results have the computed expression true
     */
    boolean noneMatch(Function<? super S, ? extends Expression<Boolean>> predicateBuilder);

    /**
     * Execute this query and return true if no results are found.
     *
     * <p>
     * Note: to perform an "exists" operation in a subquery, use {@link ExprStream#exists}.
     *
     * <p>
     * Delegates to {@link TypedQuery#getResultStream} and can throw any exception thrown by that method.
     *
     * @return true if no results are found, false if one or more results are found
     * @see ExprStream#exists
     */
    boolean isEmpty();

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

    /**
     * Find the only instance in the stream.
     *
     * <p>
     * Invoke this method only when you know that the result stream contains at most one value, e.g., when searching
     * for an object by its value in a field with a unique constraint. If the stream actually contains multiple values,
     * then invoking any of the "single value" {@link SearchValue} methods such as {@link SearchValue#value value()} or
     * {@link SearchValue#toOptional toOptional()} will generate a {@link javax.persistence.NonUniqueResultException}.
     *
     * <p>
     * Using this method is preferable to using {@link #findFirst} or {@link #findAny} for the same purpose, because it
     * not only actually verifies the uniqueness assumption, but it also makes that assumption clearer in the code.
     *
     * <p>
     * Example:
     * <pre>
     *  final User user = qb.stream(User.class)
     *    .filter(u -&gt; qb.equal(u.get(User_.username), username))
     *    .findSingle()
     *    .orElseThrow(NoSuchUserException::new);
     * </pre>
     *
     * @return single-valued stream containg the only instance in this stream (or {@code NULL} if this stream is empty)
     */
    SearchValue<X, S> findSingle();

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
     * <p>
     * Unlike {@link #join(SingularAttribute) join()}, this method allows mapping to arbitrary, non-entity properties.
     *
     * <p>
     * If the property is an entity, then this method works just like an inner {@link #join(SingularAttribute) join()}.
     * In particular, the associated entity must exist. For that reason, and also because {@link #join(SingularAttribute) join()}
     * returns the more specific type {@link FromStream}, {@link #join(SingularAttribute) join()} is preferred over this
     * method when the associated property is an entity.
     *
     * @param attribute associated property
     * @param <Y> property type
     * @return mapped stream
     */
    default <Y> PathStream<Y, Path<Y>> map(SingularAttribute<? super X, Y> attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        QueryStreamImpl.checkOffsetLimit(this, "map()");
        return new PathStreamImpl<>(this.getEntityManager(), new SearchType<>(attribute.getJavaType()), (builder, query) -> {

            // We always prefer having a From over a Path, because you can continue to join with it.
            // If you say path.get() you get a Path, even if you could have said path.join() to get a From.
            // So when mapping a SingluarAttribute, path.join() is the same as path.get() but better.
            // Of course, the attribute also has to point to something joinable, not e.g. a basic type.
            final Path<X> path = (Path<X>)this.configure(builder, query);   // cast must be valid if attribute exists
            final boolean joinable;
            switch (attribute.getPersistentAttributeType()) {
            case BASIC:
            case EMBEDDED:
                joinable = false;
                break;
            default:
                joinable = true;
                break;
            }
            return joinable && path instanceof From ? ((From<?, X>)path).join(attribute) : path.get(attribute);
        }, new QueryInfo());
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
        QueryStreamImpl.checkOffsetLimit(this, "map()");
        return new ExprStreamImpl<>(this.getEntityManager(), new SearchType<Y>(type),
          (builder, query) -> exprFunction.apply(this.configure(builder, query)), new QueryInfo());
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
        QueryStreamImpl.checkOffsetLimit(this, "map()");
        // This is necessary due to https://github.com/javaee/jpa-spec/issues/108
        final PluralAttribute<X, C, E> attribute2 = (PluralAttribute<X, C, E>)attribute;
        return new ExprStreamImpl<>(this.getEntityManager(), new SearchType<>(attribute.getJavaType()),
          (builder, query) -> ((Path<X>)this.configure(builder, query)).get(attribute2),  // cast must be valid if attribute exists
          new QueryInfo());
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
        QueryStreamImpl.checkOffsetLimit(this, "map()");
        // This is necessary due to https://github.com/javaee/jpa-spec/issues/108
        final MapAttribute<X, K, V> attribute2 = (MapAttribute<X, K, V>)attribute;
        return new ExprStreamImpl<>(this.getEntityManager(), new SearchType<>((Class<M>)attribute.getJavaType()),
          (builder, query) -> ((Path<X>)this.configure(builder, query)).get(attribute2),  // cast must be valid if attribute exists
          new QueryInfo());
    }

    /**
     * Map this stream into a stream whose elements are the result of applying the given function.
     *
     * @param type new item type
     * @param exprFunction function mapping this stream's {@link Selection} to an {@link Expression}
     * @param <Y> mapped target type
     * @return mapped stream
     */
    default <Y> ExprStream<Y, Expression<Y>> mapToExpr(Class<Y> type, Function<? super S, ? extends Expression<Y>> exprFunction) {
        if (type == null)
            throw new IllegalArgumentException("null type");
        if (exprFunction == null)
            throw new IllegalArgumentException("null exprFunction");
        QueryStreamImpl.checkOffsetLimit(this, "mapToExpr()");
        return new ExprStreamImpl<>(this.getEntityManager(), new SearchType<Y>(type),
          (builder, query) -> exprFunction.apply(this.configure(builder, query)), new QueryInfo());
    }

    /**
     * Map this stream into a stream whose elements are the result of applying the given function.
     *
     * @param type new item type
     * @param pathFunction function mapping this stream's {@link Selection} to a {@link Path}
     * @param <Y> mapped expresssion type
     * @return mapped stream
     */
    default <Y> PathStream<Y, Path<Y>> mapToPath(Class<Y> type, Function<? super S, ? extends Path<Y>> pathFunction) {
        if (type == null)
            throw new IllegalArgumentException("null type");
        if (pathFunction == null)
            throw new IllegalArgumentException("null pathFunction");
        QueryStreamImpl.checkOffsetLimit(this, "mapToPath()");
        return new PathStreamImpl<>(this.getEntityManager(), new SearchType<Y>(type),
          (builder, query) -> pathFunction.apply(this.configure(builder, query)), new QueryInfo());
    }

    /**
     * Map this stream into a stream whose elements are the result of applying the given function.
     *
     * @param type new item type
     * @param fromFunction function mapping this stream's {@link Selection} to a {@link From}
     * @param <Z> mapped source type
     * @param <Y> mapped target type
     * @return mapped stream
     */
    default <Z, Y> FromStream<Y, From<Z, Y>> mapToFrom(Class<Y> type, Function<? super S, ? extends From<Z, Y>> fromFunction) {
        if (type == null)
            throw new IllegalArgumentException("null type");
        if (fromFunction == null)
            throw new IllegalArgumentException("null fromFunction");
        QueryStreamImpl.checkOffsetLimit(this, "mapToFrom()");
        return new FromStreamImpl<>(this.getEntityManager(), new SearchType<Y>(type),
          (builder, query) -> fromFunction.apply(this.configure(builder, query)), new QueryInfo());
    }

    /**
     * Map this stream into a stream whose elements are the result of applying the given function.
     *
     * @param type new item type
     * @param rootFunction function mapping this stream's {@link Selection} to a {@link Root}
     * @param <Y> mapped target type
     * @return mapped stream
     */
    default <Y> RootStream<Y> mapToRoot(Class<Y> type, Function<? super S, ? extends Root<Y>> rootFunction) {
        if (type == null)
            throw new IllegalArgumentException("null type");
        if (rootFunction == null)
            throw new IllegalArgumentException("null rootFunction");
        QueryStreamImpl.checkOffsetLimit(this, "mapToRoot()");
        return new RootStreamImpl<>(this.getEntityManager(), new SearchType<Y>(type),
          (builder, query) -> rootFunction.apply(this.configure(builder, query)), new QueryInfo());
    }

    /**
     * Map this stream to a stream whose elements are bound to the supplied root reference.
     *
     * @param ref previously bound root reference
     * @param <Y> selection type
     * @return mapped stream
     */
    default <Y> RootStream<Y> mapToRef(Class<Y> type, RootRef<Y> ref) {
        return this.mapToRoot(type, selection -> ref.get());
    }

    /**
     * Map this stream to a stream whose elements are bound to the supplied from reference.
     *
     * @param ref previously bound from reference
     * @param <Y> mapped target type
     * @return mapped stream
     */
    @SuppressWarnings("unchecked")
    default <Y> FromStream<Y, From<?, Y>>  mapToRef(Class<Y> type, FromRef<Y> ref) {
        return (FromStream<Y, From<?, Y>>)this.mapToFrom(type, selection -> ref.get());
    }

    /**
     * Map this stream to a stream whose elements are bound to the supplied path reference.
     *
     * @param ref previously bound path reference
     * @param <Y> mapped target type
     * @return mapped stream
     */
    default <Y> PathStream<Y, Path<Y>>  mapToRef(Class<Y> type, PathRef<Y> ref) {
        return this.mapToPath(type, selection -> ref.get());
    }

    /**
     * Map this stream to a stream whose elements are bound to the supplied expression reference.
     *
     * @param ref previously bound expression reference
     * @param <Y> mapped target type
     * @return mapped stream
     */
    default <Y> ExprStream<Y, Expression<Y>> mapToRef(Class<Y> type, ExprRef<Y> ref) {
        return this.mapToExpr(type, selection -> ref.get());
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
        QueryStreamImpl.checkOffsetLimit(this, "mapToDouble()");
        return new DoubleStreamImpl(this.getEntityManager(),
          (builder, query) -> builder.toDouble(((Path<X>)this.configure(builder, query)).get(attribute)),  // cast must be valid...
          new QueryInfo());
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
        QueryStreamImpl.checkOffsetLimit(this, "mapToDouble()");
        return new DoubleStreamImpl(this.getEntityManager(),
          (builder, query) -> builder.toDouble(doubleExprFunction.apply(this.configure(builder, query))), new QueryInfo());
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
        QueryStreamImpl.checkOffsetLimit(this, "mapToLong()");
        return new LongStreamImpl(this.getEntityManager(),
          (builder, query) -> builder.toLong(((Path<X>)this.configure(builder, query)).get(attribute)),  // cast must be valid...
          new QueryInfo());
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
        QueryStreamImpl.checkOffsetLimit(this, "mapToLong()");
        return new LongStreamImpl(this.getEntityManager(),
          (builder, query) -> builder.toLong(longExprFunction.apply(this.configure(builder, query))), new QueryInfo());
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
        QueryStreamImpl.checkOffsetLimit(this, "mapToInt()");
        return new IntStreamImpl(this.getEntityManager(),
          (builder, query) -> builder.toInteger(((Path<X>)this.configure(builder, query)).get(attribute)),  // cast must be valid...
          new QueryInfo());
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
        QueryStreamImpl.checkOffsetLimit(this, "mapToInt()");
        return new IntStreamImpl(this.getEntityManager(),
          (builder, query) -> builder.toInteger(intExprFunction.apply(this.configure(builder, query))), new QueryInfo());
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
        QueryStreamImpl.checkOffsetLimit(this, "mapToSelection()");
        return new SearchStreamImpl<>(this.getEntityManager(), new SearchType<Y>(type),
          (builder, query) -> selectionFunction.apply(this.configure(builder, query)), new QueryInfo());
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
        QueryStreamImpl.checkOffsetLimit(this, "flatMap()");
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
        QueryStreamImpl.checkOffsetLimit(this, "flatMap()");
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
        QueryStreamImpl.checkOffsetLimit(this, "flatMap()");
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
        QueryStreamImpl.checkOffsetLimit(this, "flatMapKeys()");
        return new PathStreamImpl<>(this.getEntityManager(), new SearchType<>(attribute.getKeyJavaType()),
          (builder, query) -> ((From<?, X>)this.configure(builder, query)).join(attribute, JoinType.INNER).key(),       // valid...
          new QueryInfo());
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
        QueryStreamImpl.checkOffsetLimit(this, "flatMapValues()");
        return new PathStreamImpl<>(this.getEntityManager(), new SearchType<>(attribute.getElementType().getJavaType()),
          (builder, query) -> ((From<?, X>)this.configure(builder, query)).join(attribute, JoinType.INNER).value(),     // valid...
          new QueryInfo());
    }

// Singluar joins

    /**
     * Add a singular inner join to this stream.
     *
     * <p>
     * Equivalent to {@link #join(SingularAttribute, JoinType) join(attribute, JoinType.INNER)}.
     *
     * @param attribute associated property
     * @param <Y> associated property type
     * @return a new stream with specified join
     * @throws IllegalArgumentException if {@code attribute} is null
     */
    default <Y> FromStream<Y, From<X, Y>> join(SingularAttribute<? super X, Y> attribute) {
        return this.join(attribute, JoinType.INNER);
    }

    /**
     * Add a singular join to this stream using the specified join type.
     *
     * <p>
     * Equivalent to {@link #join(SingularAttribute, JoinType, Function) join(attribute, JoinType.INNER, join -> null)}.
     *
     * @param attribute associated property
     * @param joinType type of join
     * @param <Y> associated property type
     * @return a new stream with specified join
     * @throws IllegalArgumentException if either parameter is null
     */
    default <Y> FromStream<Y, From<X, Y>> join(SingularAttribute<? super X, Y> attribute, JoinType joinType) {
        return this.join(attribute, joinType, join -> null);
    }

    /**
     * Add a singular join to this stream using the specified join type and ON condition.
     *
     * @param attribute associated property
     * @param joinType type of join
     * @param on function returning a join ON condition, or returning null for none
     * @param <Y> associated property type
     * @return a new stream with specified join
     * @throws IllegalArgumentException if any parameter is null
     */
    default <Y> FromStream<Y, From<X, Y>> join(SingularAttribute<? super X, Y> attribute, JoinType joinType,
      Function<? super Join<X, Y>, ? extends Expression<Boolean>> on) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        if (joinType == null)
            throw new IllegalArgumentException("null joinType");
        if (on == null)
            throw new IllegalArgumentException("null on");
        QueryStreamImpl.checkOffsetLimit(this, "join()");
        return new FromStreamImpl<>(this.getEntityManager(), new SearchType<>(attribute.getJavaType()),
          (builder, query) -> {
            final From<?, X> from = (From<?, X>)this.configure(builder, query);             // cast must be valid...
            final Join<X, Y> join = from.join(attribute, joinType);
            final Expression<Boolean> onPredicate = on.apply(join);
            if (onPredicate != null)
                join.on(onPredicate);
            return join;
          },
          new QueryInfo());
    }

// Plural Joins

    /**
     * Add a collection inner join to this stream.
     *
     * <p>
     * Equivalent to {@link #join(CollectionAttribute, JoinType) join(attribute, JoinType.INNER)}.
     *
     * @param attribute associated property
     * @param <E> collection element type
     * @return a new stream with specified join
     * @throws IllegalArgumentException if {@code attribute} is null
     */
    default <E> FromStream<E, CollectionJoin<X, E>> join(CollectionAttribute<? super X, E> attribute) {
        return this.join(attribute, JoinType.INNER);
    }

    /**
     * Add a collection join to this stream using the specified join type.
     *
     * <p>
     * Equivalent to {@link #join(CollectionAttribute, JoinType, Function) join(attribute, JoinType.INNER, join -> null)}.
     *
     * @param attribute associated property
     * @param joinType type of join
     * @param <E> collection element type
     * @return a new stream with specified join
     * @throws IllegalArgumentException if either parameter is null
     */
    default <E> FromStream<E, CollectionJoin<X, E>> join(CollectionAttribute<? super X, E> attribute, JoinType joinType) {
        return this.join(attribute, joinType, join -> null);
    }

    /**
     * Add a collection join to this stream using the specified join type and ON condition.
     *
     * @param attribute associated property
     * @param joinType type of join
     * @param on function returning a join ON condition, or returning null for none
     * @param <E> collection element type
     * @return a new stream with specified join
     * @throws IllegalArgumentException if any parameter is null
     */
    default <E> FromStream<E, CollectionJoin<X, E>> join(CollectionAttribute<? super X, E> attribute, JoinType joinType,
      Function<? super CollectionJoin<X, E>, ? extends Expression<Boolean>> on) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        if (joinType == null)
            throw new IllegalArgumentException("null joinType");
        if (on == null)
            throw new IllegalArgumentException("null on");
        QueryStreamImpl.checkOffsetLimit(this, "join()");
        return new FromStreamImpl<>(this.getEntityManager(), new SearchType<>(attribute.getElementType().getJavaType()),
          (builder, query) -> {
            final From<?, X> from = (From<?, X>)this.configure(builder, query);             // cast must be valid...
            final CollectionJoin<X, E> join = from.join(attribute, joinType);
            final Expression<Boolean> onPredicate = on.apply(join);
            if (onPredicate != null)
                join.on(onPredicate);
            return join;
          },
          new QueryInfo());
    }

    /**
     * Add a list inner join to this stream.
     *
     * <p>
     * Equivalent to {@link #join(ListAttribute, JoinType) join(attribute, JoinType.INNER)}.
     *
     * @param attribute associated property
     * @param <E> list element type
     * @return a new stream with specified join
     * @throws IllegalArgumentException if {@code attribute} is null
     */
    default <E> FromStream<E, ListJoin<X, E>> join(ListAttribute<? super X, E> attribute) {
        return this.join(attribute, JoinType.INNER);
    }

    /**
     * Add a list join to this stream using the specified join type.
     *
     * <p>
     * Equivalent to {@link #join(ListAttribute, JoinType, Function) join(attribute, JoinType.INNER, join -> null)}.
     *
     * @param attribute associated property
     * @param joinType type of join
     * @param <E> list element type
     * @return a new stream with specified join
     * @throws IllegalArgumentException if either parameter is null
     */
    default <E> FromStream<E, ListJoin<X, E>> join(ListAttribute<? super X, E> attribute, JoinType joinType) {
        return this.join(attribute, joinType, join -> null);
    }

    /**
     * Add a list join to this stream using the specified join type and ON condition.
     *
     * @param attribute associated property
     * @param joinType type of join
     * @param on function returning a join ON condition, or returning null for none
     * @param <E> list element type
     * @return a new stream with specified join
     * @throws IllegalArgumentException if any parameter is null
     */
    default <E> FromStream<E, ListJoin<X, E>> join(ListAttribute<? super X, E> attribute, JoinType joinType,
      Function<? super ListJoin<X, E>, ? extends Expression<Boolean>> on) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        if (joinType == null)
            throw new IllegalArgumentException("null joinType");
        if (on == null)
            throw new IllegalArgumentException("null on");
        QueryStreamImpl.checkOffsetLimit(this, "join()");
        return new FromStreamImpl<>(this.getEntityManager(), new SearchType<>(attribute.getElementType().getJavaType()),
          (builder, query) -> {
            final From<?, X> from = (From<?, X>)this.configure(builder, query);             // cast must be valid...
            final ListJoin<X, E> join = from.join(attribute, joinType);
            final Expression<Boolean> onPredicate = on.apply(join);
            if (onPredicate != null)
                join.on(onPredicate);
            return join;
          },
          new QueryInfo());
    }

    /**
     * Add a set inner join to this stream.
     *
     * <p>
     * Equivalent to {@link #join(SetAttribute, JoinType) join(attribute, JoinType.INNER)}.
     *
     * @param attribute associated property
     * @param <E> set element type
     * @return a new stream with specified join
     * @throws IllegalArgumentException if {@code attribute} is null
     */
    default <E> FromStream<E, SetJoin<X, E>> join(SetAttribute<? super X, E> attribute) {
        return this.join(attribute, JoinType.INNER);
    }

    /**
     * Add a set join to this stream using the specified join type.
     *
     * <p>
     * Equivalent to {@link #join(SetAttribute, JoinType, Function) join(attribute, JoinType.INNER, join -> null)}.
     *
     * @param attribute associated property
     * @param joinType type of join
     * @param <E> set element type
     * @return a new stream with specified join
     * @throws IllegalArgumentException if either parameter is null
     */
    default <E> FromStream<E, SetJoin<X, E>> join(SetAttribute<? super X, E> attribute, JoinType joinType) {
        return this.join(attribute, joinType, join -> null);
    }

    /**
     * Add a set join to this stream using the specified join type and ON condition.
     *
     * @param attribute associated property
     * @param joinType type of join
     * @param on function returning a join ON condition, or returning null for none
     * @param <E> set element type
     * @return a new stream with specified join
     * @throws IllegalArgumentException if any parameter is null
     */
    default <E> FromStream<E, SetJoin<X, E>> join(SetAttribute<? super X, E> attribute, JoinType joinType,
      Function<? super SetJoin<X, E>, ? extends Expression<Boolean>> on) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        if (joinType == null)
            throw new IllegalArgumentException("null joinType");
        if (on == null)
            throw new IllegalArgumentException("null on");
        QueryStreamImpl.checkOffsetLimit(this, "join()");
        return new FromStreamImpl<>(this.getEntityManager(), new SearchType<>(attribute.getElementType().getJavaType()),
          (builder, query) -> {
            final From<?, X> from = (From<?, X>)this.configure(builder, query);             // cast must be valid...
            final SetJoin<X, E> join = from.join(attribute, joinType);
            final Expression<Boolean> onPredicate = on.apply(join);
            if (onPredicate != null)
                join.on(onPredicate);
            return join;
          },
          new QueryInfo());
    }

    /**
     * Add a map inner join to this stream.
     *
     * <p>
     * Equivalent to {@link #join(MapAttribute, JoinType) join(attribute, JoinType.INNER)}.
     *
     * @param attribute associated property
     * @param <K> map key type
     * @param <V> map value type
     * @return a new stream with specified join
     * @throws IllegalArgumentException if {@code attribute} is null
     */
    default <K, V> FromStream<V, MapJoin<X, K, V>> join(MapAttribute<? super X, K, V> attribute) {
        return this.join(attribute, JoinType.INNER);
    }

    /**
     * Add a map join to this stream using the specified join type.
     *
     * <p>
     * Equivalent to {@link #join(MapAttribute, JoinType, Function) join(attribute, JoinType.INNER, join -> null)}.
     *
     * @param attribute associated property
     * @param joinType type of join
     * @param <K> map key type
     * @param <V> map value type
     * @return a new stream with specified join
     * @throws IllegalArgumentException if either parameter is null
     */
    default <K, V> FromStream<V, MapJoin<X, K, V>> join(MapAttribute<? super X, K, V> attribute, JoinType joinType) {
        return this.join(attribute, joinType, join -> null);
    }

    /**
     * Add a map join to this stream using the specified join type and ON condition.
     *
     * @param attribute associated property
     * @param joinType type of join
     * @param on function returning a join ON condition, or returning null for none
     * @param <K> map key type
     * @param <V> map value type
     * @return a new stream with specified join
     * @throws IllegalArgumentException if any parameter is null
     */
    default <K, V> FromStream<V, MapJoin<X, K, V>> join(MapAttribute<? super X, K, V> attribute, JoinType joinType,
      Function<? super MapJoin<X, K, V>, ? extends Expression<Boolean>> on) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        if (joinType == null)
            throw new IllegalArgumentException("null joinType");
        if (on == null)
            throw new IllegalArgumentException("null on");
        QueryStreamImpl.checkOffsetLimit(this, "join()");
        return new FromStreamImpl<>(this.getEntityManager(), new SearchType<>(attribute.getElementType().getJavaType()),
          (builder, query) -> {
            final From<?, X> from = (From<?, X>)this.configure(builder, query);             // cast must be valid...
            final MapJoin<X, K, V> join = from.join(attribute, joinType);
            final Expression<Boolean> onPredicate = on.apply(join);
            if (onPredicate != null)
                join.on(onPredicate);
            return join;
          },
          new QueryInfo());
    }

// Fetches

    /**
     * Add a singular fetch inner join to this stream.
     *
     * <p>
     * Equivalent to {@link #fetch(SingularAttribute, JoinType) fetch(attribute, JoinType.INNER)}.
     *
     * @param attribute associated property
     * @return a new stream with specified inner fetch join
     * @throws IllegalArgumentException if {@code attribute} is null
     */
    SearchStream<X, S> fetch(SingularAttribute<? super X, ?> attribute);

    /**
     * Add a singular fetch join to this stream.
     *
     * <p>
     * Unlike {@link #join(SingularAttribute) join()}, this method does not change the stream's content type.
     * In other words, this method is used simply to pre-fetch an association, to avoid having to fetch it again
     * later for each individual element in the stream.
     *
     * @param attribute associated property
     * @param joinType join type
     * @return a new stream with specified fetch join
     * @throws IllegalArgumentException if either parameter is null
     */
    SearchStream<X, S> fetch(SingularAttribute<? super X, ?> attribute, JoinType joinType);

    /**
     * Add a plural fetch join to this stream.
     *
     * <p>
     * Equivalent to {@link #fetch(PluralAttribute, JoinType) fetch(attribute, JoinType.INNER)}.
     *
     * @param attribute associated property
     * @return a new stream with specified inner fetch join
     * @throws IllegalArgumentException if {@code attribute} is null
     */
    SearchStream<X, S> fetch(PluralAttribute<? super X, ?, ?> attribute);

    /**
     * Add a plural fetch join to this stream.
     *
     * <p>
     * Unlike {@link #join(SingularAttribute) join()}, this method does not change the stream's content type.
     * In other words, this method is used simply to pre-fetch an association, to avoid having to fetch it again
     * later for each individual element in the stream.
     *
     * @param attribute associated property
     * @param joinType join type
     * @return a new stream with specified fetch join
     * @throws IllegalArgumentException if either parameter is null
     */
    SearchStream<X, S> fetch(PluralAttribute<? super X, ?, ?> attribute, JoinType joinType);

// Narrowing overrides (QueryStream)

    @Override
    SearchType<X> getQueryType();

    @Override
    SearchStream<X, S> bind(Ref<X, ? super S> ref);

    @Override
    SearchStream<X, S> peek(Consumer<? super S> peeker);

    @Override
    <X2, S2 extends Selection<X2>> SearchStream<X, S> bind(Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction);

    @Override
    SearchStream<X, S> filter(SingularAttribute<? super X, Boolean> attribute);

    @Override
    SearchStream<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder);

    @Override
    SearchStream<X, S> limit(int maxSize);

    @Override
    SearchStream<X, S> skip(int num);

    @Override
    SearchStream<X, S> withFlushMode(FlushModeType flushMode);

    @Override
    SearchStream<X, S> withLockMode(LockModeType lockMode);

    @Override
    SearchStream<X, S> withHint(String name, Object value);

    @Override
    SearchStream<X, S> withHints(Map<String, Object> hints);

    @Override
    <T> SearchStream<X, S> withParam(Parameter<T> parameter, T value);

    @Override
    SearchStream<X, S> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType);

    @Override
    SearchStream<X, S> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType);

    @Override
    SearchStream<X, S> withParams(Iterable<? extends ParamBinding<?>> params);

    @Override
    SearchStream<X, S> withLoadGraph(String name);

    @Override
    SearchStream<X, S> withFetchGraph(String name);
}

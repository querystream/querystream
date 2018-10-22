
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.criteria.CollectionJoin;
import javax.persistence.criteria.CommonAbstractCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.SetJoin;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.QueryType;
import org.dellroad.querystream.jpa.querytype.SearchType;
import org.dellroad.querystream.jpa.util.ForwardingCriteriaBuilder;

/**
 * Builder for JPA criteria queries, based on configuration through a {@link java.util.stream.Stream}-like API.
 *
 * @param <X> stream item type
 * @param <S> criteria type for stream item
 * @param <C> configured criteria API query type
 * @param <C2> final criteria API query type
 * @param <Q> JPA query type
 */
public interface QueryStream<X,
  S extends Selection<X>,
  C extends CommonAbstractCriteria,
  C2 extends C,
  Q extends Query> extends QueryConfigurer<C, X, S> {

// QueryType

    /**
     * Get the {@link QueryType} of this instance.
     *
     * @return associated {@link QueryType}
     */
    QueryType<X, C, C2, Q> getQueryType();

// Queryification

    /**
     * Get the {@link EntityManager} associated with this instance.
     *
     * @return associated {@link EntityManager}
     */
    EntityManager getEntityManager();

    /**
     * Build a criteria API query based on this instance.
     *
     * <p>
     * Note that due to limitations of the JPA Criteria API, the returned query object lacks information that is configured
     * on the {@link Query} object and not the {@link javax.persistence.criteria.CriteriaQuery} object (for example,
     * {@linkplain #getLockMode lock mode}, {@linkplain #withHint hints}, row {@linkplain #getFirstResult offset} and
     * {@linkplain #getMaxResults limit}, etc.); such information can only be configured on the fully formed {@link Query}.
     * Use {@link #toQuery} instead of this method to also include that information.
     *
     * @return new Criteria API query corresponding to this instance
     * @see #toQuery
     */
    C2 toCriteriaQuery();

    /**
     * Build a fully configured JPA query based on this instance.
     *
     * @return new JPA query corresponding to this instance
     */
    Q toQuery();

    /**
     * Get the row offset associated with this query.
     *
     * @return row offset, or -1 if there is none configured
     * @see #skip
     * @see Query#setFirstResult
     */
    int getFirstResult();

    /**
     * Get the row limit associated with this query.
     *
     * @return row limit, or -1 if there is none configured
     * @see #limit
     * @see Query#setMaxResults
     */
    int getMaxResults();

    /**
     * Get the {@link FlushModeType} associated with this query.
     *
     * @return flush mode, or null if none set (i.e., use {@link EntityManager} setting)
     * @see Query#setFlushMode
     */
    FlushModeType getFlushMode();

    /**
     * Set the {@link FlushModeType} associated with this query.
     *
     * @param flushMode new flush mode
     * @return new stream with the specified flush mode configured
     * @see Query#setFlushMode
     * @throws IllegalArgumentException if {@code flushMode} is null
     */
    QueryStream<X, S, C, C2, Q> withFlushMode(FlushModeType flushMode);

    /**
     * Get the {@link LockModeType} associated with this query.
     *
     * @return lock mode, or null if none set
     * @see Query#setLockMode
     */
    LockModeType getLockMode();

    /**
     * Set the {@link LockModeType} associated with this query.
     *
     * @param lockMode new lock mode
     * @return new stream with the specified lock mode configured
     * @see Query#setLockMode
     * @throws IllegalArgumentException if {@code lockMode} is null
     */
    QueryStream<X, S, C, C2, Q> withLockMode(LockModeType lockMode);

    /**
     * Get any hints associated with this query.
     *
     * @return configured hints, if any, otherwise an empty map
     * @see Query#setHint
     * @return immutable map of hints
     */
    Map<String, Object> getHints();

    /**
     * Associate a hint with this query.
     *
     * @param name name of hint
     * @param value value of hint
     * @return new stream with the specified hint configured
     * @see Query#setHint
     * @throws IllegalArgumentException if {@code lockMode} is null
     */
    QueryStream<X, S, C, C2, Q> withHint(String name, Object value);

    /**
     * Associate hints with this query.
     *
     * @param hints hints to add
     * @return new stream with the specified hints configured
     * @see Query#setHint
     * @throws IllegalArgumentException if {@code hints} is null
     */
    QueryStream<X, S, C, C2, Q> withHints(Map<String, Object> hints);

    /**
     * Configure a load graph for this query.
     *
     * <p>
     * Equivalent to {@link #withHint withHint}{@code ("javax.persistence.loadgraph", name)}.
     *
     * @param name name of load graph
     * @return new stream with the specified load graph configured
     * @throws IllegalArgumentException if {@code name} is invalid
     */
    QueryStream<X, S, C, C2, Q> withLoadGraph(String name);

    /**
     * Configure a fetch graph for this query.
     *
     * <p>
     * Equivalent to {@link #withHint withHint}{@code ("javax.persistence.fetchgraph", name)}.
     *
     * @param name name of fetch graph
     * @return new stream with the specified fetch graph configured
     * @throws IllegalArgumentException if {@code name} is invalid
     */
    QueryStream<X, S, C, C2, Q> withFetchGraph(String name);

// Refs

    /**
     * Bind an unbound reference to the items in this stream.
     *
     * @param ref unbound reference
     * @throws IllegalArgumentException if {@code ref} is already bound
     * @throws IllegalArgumentException if {@code ref} is null
     * @return new stream that binds {@code ref}
     */
    QueryStream<X, S, C, C2, Q> bind(Ref<X, ? super S> ref);

    /**
     * Bind an unbound reference to the result of applying the given function to the items in this stream.
     *
     * @param ref unbound reference
     * @param refFunction function mapping this stream's {@link Selection} to the reference value
     * @param <X2> type of the bound value
     * @param <S2> criteria type of the bound value
     * @throws IllegalArgumentException if {@code ref} is already bound
     * @throws IllegalArgumentException if {@code ref} or {@code refFunction} is null
     * @return new stream that binds {@code ref}
     */
    <X2, S2 extends Selection<X2>> QueryStream<X, S, C, C2, Q> bind(
      Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction);

// Peek

    /**
     * Peek at the items in this stream.
     *
     * <p>
     * This is useful in cases where the selection can be modified, e.g., setting join {@code ON} conditions
     * using {@link Join#on Join.on()}.
     *
     * @param peeker peeker into stream
     * @throws IllegalArgumentException if {@code peeker} is null
     * @return new stream that peeks into this stream
     */
    QueryStream<X, S, C, C2, Q> peek(Consumer<? super S> peeker);

// Filtering

    /**
     * Filter results using the boolean expression produced by the given function.
     *
     * <p>
     * Adds to any previously specified filters.
     *
     * @param predicateBuilder function mapping this stream's item to a boolean {@link Expression}
     * @return new filtered stream
     */
    QueryStream<X, S, C, C2, Q> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder);

    /**
     * Filter results using the specified boolean property.
     *
     * <p>
     * Adds to any previously specified filters.
     *
     * @param attribute boolean property
     * @return new filtered stream
     */
    QueryStream<X, S, C, C2, Q> filter(SingularAttribute<? super X, Boolean> attribute);

// Streamy stuff

    /**
     * Return this stream truncated to the specified maximum length.
     *
     * <p>
     * Due to limitations in the JPA Criteria API, this method is not supported on subquery streams
     * and in general must be specified last (after any filtering, sorting, grouping, joins, etc.).
     *
     * @param maxSize maximum number of elements to return
     * @return new truncated stream
     * @throws IllegalArgumentException if {@code maxSize} is negative
     */
    QueryStream<X, S, C, C2, Q> limit(int maxSize);

    /**
     * Return this stream with the specified number of initial elements skipped.
     *
     * <p>
     * Due to limitations in the JPA Criteria API, this method is not supported on subquery streams
     * and in general must be specified last (after any filtering, sorting, grouping, joins, etc.).
     *
     * @param num number of elements to skip
     * @return new elided stream
     * @throws IllegalArgumentException if {@code num} is negative
     */
    QueryStream<X, S, C, C2, Q> skip(int num);

// Builder

    /**
     * Create a {@link Builder} that constructs {@link QueryStream}s using the given {@link EntityManager}.
     *
     * @param entityManager entity manager
     * @return new stream builder
     * @throws IllegalArgumentException if {@code entityManager} is null
     */
    static Builder newBuilder(EntityManager entityManager) {
        return new Builder(entityManager);
    }

    /**
     * Builder for {@link QueryStream} and related classes.
     *
     * <p>
     * Instances are created via {@link QueryStream#newBuilder QueryStream.newBuilder()}.
     *
     * <p>
     * For convenience, this class also implements {@link CriteriaBuilder}.
     * The primary methods in this class (i.e., not inherited from {@link CriteriaBuilder}) are:
     * <ul>
     *  <li>{@link #stream stream()} - Create a {@link SearchStream} for search queries.</li>
     *  <li>{@link #stream deleteStream()} - Create a {@link DeleteStream} for bulk delete queries.</li>
     *  <li>{@link #stream updateStream()} - Create a {@link UpdateStream} for bulk update queries.</li>
     *  <li>{@link #getEntityManager} - Get the {@link EntityManager} associated with this instance.</li>
     * </ul>
     *
     * <p>
     * In addition, the following methods create {@link SearchStream}s for use in correlated subqueries:
     * <ul>
     *  <li>{@link #substream(Root)} - Create a correlated subquery {@link SearchStream} from a {@link Root}.</li>
     *  <li>{@link #substream(Join)} - Create a correlated subquery {@link SearchStream} from a {@link Join}.</li>
     *  <li>{@link #substream(SetJoin)} - Create a correlated subquery {@link SearchStream} from a {@link SetJoin}.</li>
     *  <li>{@link #substream(MapJoin)} - Create a correlated subquery {@link SearchStream} from a {@link MapJoin}.</li>
     *  <li>{@link #substream(ListJoin)} - Create a correlated subquery {@link SearchStream} from a {@link ListJoin}.</li>
     *  <li>{@link #substream(CollectionJoin)}
     *      - Create a correlated subquery {@link SearchStream} from a {@link CollectionJoin}.</li>
     * </ul>
     *
     * <p>
     * See {@link #substream(Root) substream()} for an example of using substreams.
     */
    final class Builder extends ForwardingCriteriaBuilder {

        private final EntityManager entityManager;
        private final CriteriaBuilder criteriaBuilder;

        private Builder(EntityManager entityManager) {
            if (entityManager == null)
                throw new IllegalArgumentException("null entityManager");
            this.entityManager = entityManager;
            this.criteriaBuilder = this.entityManager.getCriteriaBuilder();
        }

        /**
         * Get the {@link EntityManager} associated with this instance.
         *
         * @return associated {@link EntityManager}
         */
        public EntityManager getEntityManager() {
            return this.entityManager;
        }

        /**
         * Get the {@link CriteriaBuilder} associated with this instance.
         *
         * @return {@link CriteriaBuilder} created from this instance's {@link EntityManager}
         */
        @Override
        public CriteriaBuilder getCriteriaBuilder() {
            return this.criteriaBuilder;
        }

        /**
         * Create a {@link SearchStream} for search queries.
         *
         * @param type stream result type
         * @param <X> stream result type
         * @return new search stream
         * @throws IllegalArgumentException if {@code type} is null
         */
        public <X> RootStream<X> stream(Class<X> type) {
            return new RootStreamImpl<>(this.entityManager, type);
        }

        /**
         * Create a {@link SearchStream} for use as a subquery, using the specified correlated {@link Root}.
         *
         * <p>
         * The returned {@link RootStream} cannot be materialized directly via {@link RootStream#toQuery toQuery()}
         * or {@link RootStream#toCriteriaQuery toCriteriaQuery()}; instead, it can only be used indirectly as a
         * correlated subquery.
         *
         * <p>
         * Here's an example that returns the names of teachers who have one or more newly enrolled students:
         * <pre>
         *  List&lt;String&gt; names = qb.stream(Teacher.class)
         *    .filter(teacher -&gt;
         *       qb.substream(teacher)
         *         .map(Teacher_.students)
         *         .filter(Student_.newlyEnrolled)
         *         .exists()))
         *    .map(Teacher_.name)
         *    .getResultList();
         * </pre>
         *
         * @param root correlated root for subquery
         * @param <X> stream result type
         * @return new subquery search stream
         * @throws IllegalArgumentException if {@code root} is null
         */
        @SuppressWarnings("unchecked")
        public <X> RootStream<X> substream(Root<X> root) {
            if (root == null)
                throw new IllegalArgumentException("null root");
            return new RootStreamImpl<>(this.entityManager, new SearchType<X>((Class<X>)root.getJavaType()),
              (builder, query) -> QueryStreamImpl.getSubqueryInfo().getSubquery().correlate(root), new QueryInfo());
        }

        /**
         * Create a {@link SearchStream} for use as a subquery, using the specified join.
         *
         * @param join correlated join object for subquery
         * @param <X> join origin type
         * @param <E> collection element type
         * @return new subquery search stream
         * @throws IllegalArgumentException if {@code join} is null
         * @see #substream(Root)
         */
        @SuppressWarnings("unchecked")
        public <X, E> FromStream<E, CollectionJoin<X, E>> substream(CollectionJoin<X, E> join) {
            if (join == null)
                throw new IllegalArgumentException("null join");
            return new FromStreamImpl<>(this.entityManager, new SearchType<E>((Class<E>)join.getJavaType()),
              (builder, query) -> QueryStreamImpl.getSubqueryInfo().getSubquery().correlate(join), new QueryInfo());
        }

        /**
         * Create a {@link SearchStream} for use as a subquery, using the specified join.
         *
         * @param join correlated join object for subquery
         * @param <X> join origin type
         * @param <E> list element type
         * @return new subquery search stream
         * @throws IllegalArgumentException if {@code join} is null
         * @see #substream(Root)
         */
        @SuppressWarnings("unchecked")
        public <X, E> FromStream<E, ListJoin<X, E>> substream(ListJoin<X, E> join) {
            if (join == null)
                throw new IllegalArgumentException("null join");
            return new FromStreamImpl<>(this.entityManager, new SearchType<E>((Class<E>)join.getJavaType()),
              (builder, query) -> QueryStreamImpl.getSubqueryInfo().getSubquery().correlate(join), new QueryInfo());
        }

        /**
         * Create a {@link SearchStream} for use as a subquery, using the specified join.
         *
         * @param join correlated join object for subquery
         * @param <X> join origin type
         * @param <K> map key type
         * @param <V> map value type
         * @return new subquery search stream
         * @throws IllegalArgumentException if {@code join} is null
         * @see #substream(Root)
         */
        @SuppressWarnings("unchecked")
        public <X, K, V> FromStream<V, MapJoin<X, K, V>> substream(MapJoin<X, K, V> join) {
            if (join == null)
                throw new IllegalArgumentException("null join");
            return new FromStreamImpl<>(this.entityManager, new SearchType<V>((Class<V>)join.getJavaType()),
              (builder, query) -> QueryStreamImpl.getSubqueryInfo().getSubquery().correlate(join), new QueryInfo());
        }

        /**
         * Create a {@link SearchStream} for use as a subquery, using the specified join.
         *
         * @param join correlated join object for subquery
         * @param <X> join origin type
         * @param <E> set element type
         * @return new subquery search stream
         * @throws IllegalArgumentException if {@code join} is null
         * @see #substream(Root)
         */
        @SuppressWarnings("unchecked")
        public <X, E> FromStream<E, SetJoin<X, E>> substream(SetJoin<X, E> join) {
            if (join == null)
                throw new IllegalArgumentException("null join");
            return new FromStreamImpl<>(this.entityManager, new SearchType<E>((Class<E>)join.getJavaType()),
              (builder, query) -> QueryStreamImpl.getSubqueryInfo().getSubquery().correlate(join), new QueryInfo());
        }

        /**
         * Create a {@link SearchStream} for use as a subquery, using the specified join.
         *
         * @param join correlated join object for subquery
         * @param <X> join origin type
         * @param <E> collection element type
         * @return new subquery search stream
         * @throws IllegalArgumentException if {@code join} is null
         * @see #substream(Root)
         */
        @SuppressWarnings("unchecked")
        public <X, E> FromStream<E, Join<X, E>> substream(Join<X, E> join) {
            if (join == null)
                throw new IllegalArgumentException("null join");
            return new FromStreamImpl<>(this.entityManager, new SearchType<E>((Class<E>)join.getJavaType()),
              (builder, query) -> QueryStreamImpl.getSubqueryInfo().getSubquery().correlate(join), new QueryInfo());
        }

        /**
         * Create a {@link DeleteStream} for bulk delete queries.
         *
         * @param type stream target type
         * @param <X> stream target type
         * @return new bulk delete stream
         * @throws IllegalArgumentException if {@code type} is null
         */
        public <X> DeleteStream<X> deleteStream(Class<X> type) {
            return new DeleteStreamImpl<>(this.entityManager, type);
        }

        /**
         * Create a {@link UpdateStream} for bulk update queries.
         *
         * @param type stream target type
         * @param <X> stream target type
         * @return new bulk update stream
         * @throws IllegalArgumentException if {@code type} is null
         */
        public <X> UpdateStream<X> updateStream(Class<X> type) {
            return new UpdateStreamImpl<>(this.entityManager, type);
        }
    }
}

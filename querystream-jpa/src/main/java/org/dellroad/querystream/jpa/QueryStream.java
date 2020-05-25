
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CollectionJoin;
import javax.persistence.criteria.CommonAbstractCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Predicate;
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
     * on the {@link Query} object and not the {@link javax.persistence.criteria.CriteriaQuery} object, including
     * {@linkplain #getLockMode lock mode}, {@linkplain #withHint hints}, {@linkplain #withParam parameters},
     * row {@linkplain #getFirstResult offset} and {@linkplain #getMaxResults limit}, etc.
     *
     * <p>
     * This information can only be configured on the fully formed {@link Query}; use {@link #toQuery} instead of this
     * method to also include that information.
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
     * @return new stream with the specified hints added
     * @see Query#setHint
     * @throws IllegalArgumentException if {@code hints} is null
     */
    QueryStream<X, S, C, C2, Q> withHints(Map<String, Object> hints);

    /**
     * Get any parameter bindings associated with this query.
     *
     * @return configured parameter bindings, if any, otherwise an empty set
     * @return immutable set of parameter bindings
     * @see Query#setParameter(javax.persistence.Parameter, Object)
     */
    Set<ParamBinding<?>> getParams();

    /**
     * Bind the value of a query parameter.
     *
     * <p>
     * Replaces any previous binding of the same parameter.
     *
     * @param parameter the parameter to set
     * @param value parameter value
     * @param <T> parameter value type
     * @return new stream with the specified parameter value set
     * @throws IllegalArgumentException if {@code parameter} is null
     * @see Query#setParameter(javax.persistence.Parameter, Object)
     */
    <T> QueryStream<X, S, C, C2, Q> withParam(Parameter<T> parameter, T value);

    /**
     * Bind the value of a query parameter of type {@link Date}.
     *
     * <p>
     * Replaces any previous binding of the same parameter.
     *
     * @param parameter the parameter to set
     * @param value parameter value
     * @param temporalType temporal type for {@code value}
     * @return new stream with the specified parameter value set
     * @throws IllegalArgumentException if {@code parameter} or {@code temporalType} is null
     * @see Query#setParameter(javax.persistence.Parameter, Date, TemporalType)
     */
    QueryStream<X, S, C, C2, Q> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType);

    /**
     * Bind the value of a query parameter of type {@link Calendar}.
     *
     * <p>
     * Replaces any previous binding of the same parameter.
     *
     * @param parameter the parameter to set
     * @param value parameter value
     * @param temporalType temporal type for {@code value}
     * @return new stream with the specified parameter value set
     * @throws IllegalArgumentException if {@code parameter} or {@code temporalType} is null
     * @see Query#setParameter(javax.persistence.Parameter, Calendar, TemporalType)
     */
    QueryStream<X, S, C, C2, Q> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType);

    /**
     * Associate parameter bindings with this query.
     *
     * <p>
     * Replaces any previous bindings of the same parameters.
     *
     * @param params bindings to add
     * @return new stream with the specified parameter bindings added
     * @throws IllegalArgumentException if {@code params} or any contained element is null
     * @throws IllegalArgumentException if {@code params} contains duplicate bindings for the same parameter
     * @see Query#setParameter(javax.persistence.Parameter, Object)
     */
    QueryStream<X, S, C, C2, Q> withParams(Iterable<? extends ParamBinding<?>> params);

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
     * @return new stream that peeks into this stream
     * @throws IllegalArgumentException if {@code peeker} is null
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
     * @throws IllegalArgumentException if {@code predicateBuilder} is null
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
     * @throws IllegalArgumentException if {@code attribute} is null
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
     * Builder for {@link QueryStream}s, {@link DeleteStream}s, and {@link UpdateStream}s.
     *
     * <p>
     * New instances of this class are created via {@link QueryStream#newBuilder QueryStream.newBuilder()}.
     *
     * <p>
     * For convenience, this class also implements {@link CriteriaBuilder}.
     *
     * <p>
     * The primary stream creation methods are:
     * <ul>
     *  <li>{@link #stream stream()} - Create a {@link SearchStream} for search queries.</li>
     *  <li>{@link #deleteStream deleteStream()} - Create a {@link DeleteStream} for bulk delete queries.</li>
     *  <li>{@link #updateStream updateStream()} - Create a {@link UpdateStream} for bulk update queries.</li>
     * </ul>
     *
     * <p>
     * The following methods create {@link SearchStream}s for use in correlated subqueries:
     * <ul>
     *  <li>{@link #substream(Root)} - Create a correlated subquery {@link SearchStream} from a {@link Root}.</li>
     *  <li>{@link #substream(Join)} - Create a correlated subquery {@link SearchStream} from a {@link Join}.</li>
     *  <li>{@link #substream(SetJoin)} - Create a correlated subquery {@link SearchStream} from a {@link SetJoin}.</li>
     *  <li>{@link #substream(MapJoin)} - Create a correlated subquery {@link SearchStream} from a {@link MapJoin}.</li>
     *  <li>{@link #substream(ListJoin)} - Create a correlated subquery {@link SearchStream} from a {@link ListJoin}.</li>
     *  <li>{@link #substream(CollectionJoin)}
     *      - Create a correlated subquery {@link SearchStream} from a {@link CollectionJoin}.</li>
     *  <li>{@link #substream(From)}
     *      - Create a correlated subquery {@link SearchStream} from any {@link From} when a more specific type is unknown.</li>
     * </ul>
     *
     * <p>
     * See {@link #substream(Root)} for an example of using substreams.
     *
     * <p>
     * The following methods provide "convenience" access to objects that are not always readily available:
     * <ul>
     *  <li>{@link #currentQuery} - Access the current Criteria API query under construction.</li>
     *  <li>{@link #bindParam bindParam()} - Register a parameter binding with the current {@link Query} under construction.</li>
     *  <li>{@link #getEntityManager} - Get the {@link EntityManager} associated with this instance.</li>
     * </ul>
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
              (builder, query) -> QueryStreamImpl.getCurrentQuery().getSubquery().correlate(root), new QueryInfo());
        }

        /**
         * Create a {@link SearchStream} for use as a subquery, using the specified {@link From}.
         *
         * <p>
         * This method inspects the type of {@code from} and then delegates to the {@code substream()} variant
         * corresponding to whether {@code from} is really a {@link Root}, {@link SetJoin}, {@link MapJoin}, etc.
         * You can use this method when you don't have more specific type information about {@code from}.
         *
         * @param from correlated join object for subquery
         * @param <X> source type
         * @param <Y> target type
         * @return new subquery search stream
         * @throws IllegalArgumentException if {@code join} is null
         * @see #substream(Root)
         */
        @SuppressWarnings("unchecked")
        public <X, Y> FromStream<Y, ? extends From<X, Y>> substream(From<X, Y> from) {
            if (from == null)
                throw new IllegalArgumentException("null join");
            if (from instanceof Root)
                return (FromStream<Y, ? extends From<X, Y>>)this.substream((Root<Y>)from);
            if (from instanceof SetJoin)
                return this.substream((SetJoin<X, Y>)from);
            if (from instanceof ListJoin)
                return this.substream((ListJoin<X, Y>)from);
            if (from instanceof MapJoin)
                return this.substream((MapJoin<X, ?, Y>)from);
            if (from instanceof CollectionJoin)
                return this.substream((CollectionJoin<X, Y>)from);
            if (from instanceof Join)
                return this.substream((Join<X, Y>)from);
            throw new UnsupportedOperationException("substream() from " + from.getClass().getName());
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
              (builder, query) -> QueryStreamImpl.getCurrentQuery().getSubquery().correlate(join), new QueryInfo());
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
              (builder, query) -> QueryStreamImpl.getCurrentQuery().getSubquery().correlate(join), new QueryInfo());
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
              (builder, query) -> QueryStreamImpl.getCurrentQuery().getSubquery().correlate(join), new QueryInfo());
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
              (builder, query) -> QueryStreamImpl.getCurrentQuery().getSubquery().correlate(join), new QueryInfo());
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
              (builder, query) -> QueryStreamImpl.getCurrentQuery().getSubquery().correlate(join), new QueryInfo());
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

        /**
         * Access the current Criteria API query under construction.
         *
         * <p>
         * This method provides a way to access the current {@link javax.persistence.criteria.CriteriaQuery},
         * {@link javax.persistence.criteria.CriteriaUpdate}, {@link javax.persistence.criteria.CriteriaDelete},
         * or {@link javax.persistence.criteria.Subquery} currently being constructed.
         *
         * <p>
         * This is useful (for example) when implementing a {@link #filter(Function)} function using the traditional
         * JPA Criteria API and you need to create a {@link javax.persistence.criteria.Subquery}:
         * <pre>
         *  List&lt;String&gt; names = qb.stream(Teacher.class)
         *    .filter(teacher -&gt; {
         *       Subquery&lt;Student&gt; subquery = qb.currentQuery().subquery(Student.class);
         *       // configure Student subquery...
         *       return qb.exists(subquery);
         *    })
         *    .map(Teacher_.name)
         *    .getResultList();         // note: the query is actually constructed here
         * </pre>
         *
         * <p>
         * This method does not work outside of the context of a query being constructed.
         *
         * <p>
         * In the case of nested {@linkplain #substream substream(s)}, then the inner-most query is returned:
         * <pre>
         *  List&lt;String&gt; names = qb.stream(Teacher.class)
         *    .filter(teacher -&gt; {
         *       // here qb.currentQuery() would return CriteriaQuery&lt;Teacher&gt;
         *       return qb.substream(teacher)
         *         .map(Teacher_.students)
         *         .filter(student -&gt; {
         *           // here qb.currentQuery() returns CriteriaQuery&lt;Student&gt;
         *           Subquery&lt;Test&gt; subquery = qb.currentQuery().subquery(Test.class);
         *           // configure Test subquery...
         *           return qb.exists(subquery);
         *         })
         *         .exists();
         *    })
         *    .map(Teacher_.name)
         *    .getResultList();
         *  // here qb.currentQuery() will throw IllegalStateException
         *  qb.currentQuery();      // this will throw IllegalStateException
         * </pre>
         *
         * <p>
         * The returned query object should not be modified.
         *
         * @return the current Criteria API query under construction
         * @throws IllegalStateException if invoked outside of Criteria API query construction
         */
        public CommonAbstractCriteria currentQuery() {
            try {
                return QueryStreamImpl.getCurrentQuery().getQuery();
            } catch (IllegalStateException e) {
                throw new IllegalStateException("there is no Criteria API query currently under construction");
            }
        }

        /**
         * Register a parameter binding with the current {@link Query} that is under construction.
         *
         * <p>
         * This method addresses an inconvenience in the JPA Criteria API, which is that parameters are (a) used (i.e., within
         * some Criteria API expression) and (b) bound (i.e., assigned a value) at two separate stages of query construction:
         * parameters are used in the context of building a Criteria API {@link Predicate}, but the value of the parameter
         * can only be bound once the overall {@link Query} has been constructed. Often these two steps are implemented
         * at different places in the code.
         *
         * <p>
         * This method allows the value of the parameter to be bound at the same time it is used. It simply remembers the
         * parameter value until later when the {@link Query} is created and the value can then be actually assigned.
         * However, this method only works for {@link Query}s created via QueryStream API query execution methods, e.g.,
         * {@link QueryStream#toQuery}, {@link SearchStream#getResultList}, {@link DeleteStream#delete}, {@link SearchValue#value},
         * etc.
         *
         * <p>
         * This example shows how parameters would usually be handled:
         *
         * <pre>
         *  // Create parameter and get parameterized value
         *  Date startDateCutoff = ...;
         *  Parameter&lt;Date&gt; startDateParam = qb.parameter(Date.class);
         *
         *  // Build Query
         *  Query query = qb.stream(Employee.class)
         *    .filter(e -&gt; qb.greaterThan(e.get(Employee_.startDate), startDateParam))   // parameter used here
         *    .map(Employee_.name)
         *    .toQuery();
         *
         *  // Bind parameter value
         *  query.setParameter(paramRef.get(), startDateCutoff, TemporalType.DATE);      // parameter bound here
         *
         *  // Execute query
         *  return query.getResultStream();
         * </pre>
         *
         * This example, which is functionally equivalent to the above, shows how {@link #bindParam bindParam()} allows
         * performing all of the parameter handling in one place:
         *
         * <pre>
         *  return qb.stream(Employee.class)
         *    .filter(e -&gt; {
         *       Date startDateCutoff = ...;
         *       Parameter&lt;Date&gt; startDateParam = qb.parameter(Date.class);
         *       qb.bindParam(new DateParamBinding(startDateParam, startDateCutoff, TemporalType.DATE));
         *       return qb.greaterThan(e.get(Employee_.startDate), param);
         *    })
         *    .map(Employee_.name)
         *    .getResultStream();               // note: the Query is actually constructed here
         * </pre>
         *
         * <p>
         * If this method is invoked outside of the context of {@link Query} construction,
         * an {@link IllegalStateException} is thrown.
         *
         * @param binding parameter binding
         * @throws IllegalStateException if invoked outside of {@link QueryStream#toQuery} or other query execution method
         * @throws IllegalArgumentException if {@code binding} is null
         */
        public void bindParam(ParamBinding<?> binding) {
            QueryStreamImpl.bindParam(binding, true);
        }
    }
}

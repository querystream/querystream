
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CommonAbstractCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.QueryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builder for JPA criteria queries, based on configuration through a {@link java.util.stream.Stream}-like API.
 *
 * @param <X> stream item type
 * @param <S> criteria type for stream item
 * @param <C> configured criteria API query type
 * @param <C2> final criteria API query type
 * @param <Q> JPA query type
 * @param <QT> corresponding {@link QueryType} subtype
 * @param <QS> subclass type
 */
abstract class QueryStreamImpl<X,
  S extends Selection<X>,
  C extends CommonAbstractCriteria,
  C2 extends C,
  Q extends Query,
  QT extends QueryType<X, C, C2, Q>> implements QueryStream<X, S, C, C2, Q> {

    private static final ThreadLocal<QueryInfo> THREAD_QUERY_INFO = new ThreadLocal<>();
    private static final ThreadLocal<CurrentQuery> THREAD_CURRENT_QUERY = new ThreadLocal<>();

    private static final String LOAD_GRAPH_HINT = "javax.persistence.loadgraph";
    private static final String FETCH_GRAPH_HINT = "javax.persistence.fetchgraph";

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    final EntityManager entityManager;
    final QT queryType;
    final QueryConfigurer<C, X, ? extends S> configurer;
    final QueryInfo queryInfo;

// Constructors

    QueryStreamImpl(EntityManager entityManager, QT queryType, QueryConfigurer<C, X, ? extends S> configurer, QueryInfo queryInfo) {
        if (entityManager == null)
            throw new IllegalArgumentException("null entityManager");
        if (queryType == null)
            throw new IllegalArgumentException("null queryType");
        if (configurer == null)
            throw new IllegalArgumentException("null configurer");
        if (queryInfo == null)
            throw new IllegalArgumentException("null queryInfo");
        this.entityManager = entityManager;
        this.queryType = queryType;
        this.configurer = configurer;
        this.queryInfo = queryInfo;
    }

// QueryType

    @Override
    public QT getQueryType() {
        return this.queryType;
    }

// Extenders

    /**
     * Create an instance equivalent to this one where the given operation is applied to the query.
     *
     * @param modifier additional query modification
     */
    QueryStream<X, S, C, C2, Q> modQuery(BiConsumer<? super C, ? super S> modifier) {
        if (modifier == null)
            throw new IllegalArgumentException("null modifier");
        return this.create(this.entityManager, this.queryType, (builder, query) -> {
            final S selection = this.configure(builder, query);
            modifier.accept(query, selection);
            return selection;
        }, this.queryInfo);
    }

    /**
     * Create an instance equivalent to this one but with a new {@link QueryConfigurer}.
     *
     * @param configurer new query configuration
     */
    QueryStream<X, S, C, C2, Q> withConfig(QueryConfigurer<C, X, ? extends S> configurer) {
        if (configurer == null)
            throw new IllegalArgumentException("null configurer");
        return this.create(this.entityManager, this.queryType, configurer, this.queryInfo);
    }

    /**
     * Create an instance equivalent to this one but with a new {@link QueryInfo}.
     *
     * @param queryInfo new query info
     */
    QueryStream<X, S, C, C2, Q> withQueryInfo(QueryInfo queryInfo) {
        return this.create(this.entityManager, this.queryType, this.configurer, queryInfo);
    }

// Subclass required methods

    /**
     * Create a new instance with the same type as this instance.
     */
    abstract QueryStream<X, S, C, C2, Q> create(EntityManager entityManager,
      QT queryType, QueryConfigurer<C, X, ? extends S> configurer, QueryInfo queryInfo);

    /**
     * Apply selection criteria, if appropriate.
     *
     * @param query query to select from
     * @param selection what to select
     * @return modified query
     */
    abstract C2 select(C2 query, S selection);

// QueryConfigurer

    @Override
    public S configure(CriteriaBuilder builder, C query) {
        return this.configurer.configure(builder, query);
    }

// Queryification

    CriteriaBuilder builder() {
        return this.entityManager.getCriteriaBuilder();
    }

    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    public C2 toCriteriaQuery() {
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final C2 query = this.queryType.createCriteriaQuery(builder);
        return QueryStreamImpl.withCurrentQuery(builder, query, () -> this.select(query, this.configure(builder, query)));
    }

    @Override
    public Q toQuery() {

        // Create a merged QueryInfo object into which we can merge this and all subquery QueryInfo's
        final QueryInfo previous = THREAD_QUERY_INFO.get();
        THREAD_QUERY_INFO.set(this.queryInfo);
        final Q query;
        try {
            query = this.queryType.createQuery(this.entityManager, this.toCriteriaQuery());
            THREAD_QUERY_INFO.get().applyTo(query);                 // apply merged QueryInfo configuration to the query
            return query;
        } finally {
            THREAD_QUERY_INFO.set(previous);
        }
    }

    @Override
    public int getFirstResult() {
        return this.queryInfo.getFirstResult();
    }

    @Override
    public int getMaxResults() {
        return this.queryInfo.getMaxResults();
    }

    @Override
    public FlushModeType getFlushMode() {
        return this.queryInfo.getFlushMode();
    }

    @Override
    public QueryStream<X, S, C, C2, Q> withFlushMode(FlushModeType flushMode) {
        return this.withQueryInfo(this.queryInfo.withFlushMode(flushMode));
    }

    @Override
    public LockModeType getLockMode() {
        return this.queryInfo.getLockMode();
    }

    @Override
    public QueryStream<X, S, C, C2, Q> withLockMode(LockModeType lockMode) {
        return this.withQueryInfo(this.queryInfo.withLockMode(lockMode));
    }

    @Override
    public Map<String, Object> getHints() {
        return this.queryInfo.getHints();
    }

    @Override
    public QueryStream<X, S, C, C2, Q> withHint(String name, Object value) {
        return this.withQueryInfo(this.queryInfo.withHint(name, value));
    }

    @Override
    public QueryStream<X, S, C, C2, Q> withHints(Map<String, Object> hints) {
        return this.withQueryInfo(this.queryInfo.withHints(hints));
    }

    @Override
    public Set<ParamBinding<?>> getParams() {
        return this.queryInfo.getParams();
    }

    @Override
    public <T> QueryStream<X, S, C, C2, Q> withParam(Parameter<T> parameter, T value) {
        return this.withQueryInfo(this.queryInfo.withParam(parameter, value));
    }

    @Override
    public QueryStream<X, S, C, C2, Q> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType) {
        return this.withQueryInfo(this.queryInfo.withParam(parameter, value, temporalType));
    }

    @Override
    public QueryStream<X, S, C, C2, Q> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType) {
        return this.withQueryInfo(this.queryInfo.withParam(parameter, value, temporalType));
    }

    @Override
    public QueryStream<X, S, C, C2, Q> withParams(Iterable<? extends ParamBinding<?>> params) {
        return this.withQueryInfo(this.queryInfo.withParams(params));
    }

    @Override
    public QueryStream<X, S, C, C2, Q> withLoadGraph(String name) {
        return this.withEntityGraph(LOAD_GRAPH_HINT, name);
    }

    @Override
    public QueryStream<X, S, C, C2, Q> withFetchGraph(String name) {
        return this.withEntityGraph(FETCH_GRAPH_HINT, name);
    }

    private QueryStream<X, S, C, C2, Q> withEntityGraph(String hintName, String graphName) {
        if (graphName == null)
            throw new IllegalArgumentException("null entity graph name");
        return this.withHint(hintName, this.entityManager.getEntityGraph(graphName));
    }

// Refs

    @Override
    public QueryStream<X, S, C, C2, Q> bind(Ref<X, ? super S> ref) {
        return this.bind(ref, s -> s);
    }

    @Override
    public QueryStream<X, S, C, C2, Q> peek(Consumer<? super S> peeker) {
        if (peeker == null)
            throw new IllegalArgumentException("null peeker");
        return this.withConfig((builder, query) -> {
            final S selection = this.configure(builder, query);
            peeker.accept(selection);
            return selection;
        });
    }

    @Override
    public <X2, S2 extends Selection<X2>> QueryStream<X, S, C, C2, Q> bind(
      Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction) {
        if (ref == null)
            throw new IllegalArgumentException("null ref");
        if (refFunction == null)
            throw new IllegalArgumentException("null refFunction");
        if (ref.isBound())
            throw new IllegalArgumentException("reference is already bound");
        return this.withConfig((builder, query) -> {
            final S selection = this.configure(builder, query);
            ref.bind(refFunction.apply(selection));
            return selection;
        });
    }

// Filtering

    @Override
    public QueryStream<X, S, C, C2, Q> filter(SingularAttribute<? super X, Boolean> attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        return this.withConfig((builder, query) -> {
            final S result = this.configure(builder, query);
            this.and(builder, query, builder.isTrue(((Path<X>)result).get(attribute))); // cast must be valid if attribute exists
            return result;
        });
    }

    @Override
    public QueryStream<X, S, C, C2, Q> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder) {
        if (predicateBuilder == null)
            throw new IllegalArgumentException("null predicateBuilder");
        QueryStreamImpl.checkOffsetLimit(this, "filter()");
        return this.withConfig((builder, query) -> {
            final S result = this.configure(builder, query);
            this.and(builder, query, predicateBuilder.apply(result));
            return result;
        });
    }

    void and(CriteriaBuilder builder, C query, Expression<Boolean> expression) {
        final Predicate oldRestriction = query.getRestriction();
        this.queryType.where(query, oldRestriction != null ? builder.and(oldRestriction, expression) : expression);
    }

// Streamy stuff

    @Override
    public QueryStream<X, S, C, C2, Q> limit(int limit) {
        if (limit < 0)
            throw new IllegalArgumentException("limit < 0");
        final int newMaxResults = this.getMaxResults() != -1 ? Math.min(limit, this.getMaxResults()) : limit;
        return this.withQueryInfo(this.queryInfo.withMaxResults(newMaxResults));
    }

    @Override
    public QueryStream<X, S, C, C2, Q> skip(int skip) {
        if (skip < 0)
            throw new IllegalArgumentException("skip < 0");
        final int newFirstResult = this.getFirstResult() != -1 ? this.getFirstResult() + skip : skip;
        return this.withQueryInfo(this.queryInfo.withFirstResult(newFirstResult));
    }

    // This is used to prevent other stuff that would affect the returned results being applied after skip()/limit()
    static void checkOffsetLimit(QueryStream<?, ?, ?, ?, ?> stream, String operation) {
        if (stream.getFirstResult() != -1 || stream.getMaxResults() != -1)
            QueryStreamImpl.failJpaRestriction(operation + " must be performed prior to skip() or limit()");
    }

    static void failJpaRestriction(String restriction) {
        throw new UnsupportedOperationException("sorry, " + restriction + " because the JPA Criteria API allows certain"
          + " information to be configured only on a Query object, not on a CriteriaQuery or Subquery");
    }

// QueryInfo Merging

    // Merge the QueryInfo information from a subquery into the "global" QueryInfo we will use for the outermost query
    static void mergeQueryInfo(QueryInfo innerQueryInfo) {
        if (innerQueryInfo == null)
            throw new IllegalArgumentException("null innerQueryInfo");
        final QueryInfo outerQueryInfo = THREAD_QUERY_INFO.get();
        if (outerQueryInfo != null)
            THREAD_QUERY_INFO.set(outerQueryInfo.withMergedInfo(innerQueryInfo));
    }

// CurrentQuery

    static CurrentQuery getCurrentQuery() {
        final CurrentQuery info = THREAD_CURRENT_QUERY.get();
        if (info == null)
            throw new IllegalStateException("subquery must be created in the context of a containing query");
        return info;
    }

    static <T> T withCurrentQuery(CriteriaBuilder builder, CommonAbstractCriteria query, Supplier<T> action) {
        if (action == null)
            throw new IllegalArgumentException("null action");
        final CurrentQuery prev = THREAD_CURRENT_QUERY.get();
        final CurrentQuery info = new CurrentQuery(builder, query);
        THREAD_CURRENT_QUERY.set(info);
        try {
            return action.get();
        } finally {
            THREAD_CURRENT_QUERY.set(prev);
        }
    }
}

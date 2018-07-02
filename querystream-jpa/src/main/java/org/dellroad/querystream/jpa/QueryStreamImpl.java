
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CommonAbstractCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;
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

    static final ThreadLocal<QueryInfo> CURRENT_INFO = new ThreadLocal<>();

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    final EntityManager entityManager;
    final QT queryType;
    final QueryConfigurer<C, X, ? extends S> configurer;

// Constructors

    QueryStreamImpl(EntityManager entityManager, QT queryType, QueryConfigurer<C, X, ? extends S> configurer) {
        if (entityManager == null)
            throw new IllegalArgumentException("null entityManager");
        if (queryType == null)
            throw new IllegalArgumentException("null queryType");
        if (configurer == null)
            throw new IllegalArgumentException("null configurer");
        this.entityManager = entityManager;
        this.queryType = queryType;
        this.configurer = configurer;
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
    QueryStream<X, S, C, C2, Q> modQuery(BiConsumer<? super CriteriaBuilder, ? super C> modifier) {
        if (modifier == null)
            throw new IllegalArgumentException("null modifier");
        return this.create(this.entityManager, this.queryType, (builder, query) -> {
            final S selection = this.configure(builder, query);
            modifier.accept(builder, query);
            return selection;
        });
    }

    /**
     * Create an instance equivalent to this one but with a completely new query configuration.
     *
     * @param extender additional query configuration
     */
    QueryStream<X, S, C, C2, Q> withConfig(QueryConfigurer<C, X, ? extends S> configurer) {
        if (configurer == null)
            throw new IllegalArgumentException("null configurer");
        return this.create(this.entityManager, this.queryType, configurer);
    }

// Subclass required methods

    /**
     * Create a new instance with the same type as this instance.
     */
    abstract QueryStream<X, S, C, C2, Q> create(EntityManager entityManager,
      QT queryType, QueryConfigurer<C, X, ? extends S> configurer);

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
        return QueryStreamImpl.withQueryInfo(builder, query, () -> this.select(query, this.configure(builder, query)));
    }

    @Override
    public Q toQuery() {
        return this.queryType.createQuery(this.entityManager, this.toCriteriaQuery());
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
        return this.filter(e -> ((Path<X>)e).get(attribute));    // cast must be valid if attribute exists
    }

    @Override
    public QueryStream<X, S, C, C2, Q> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder) {
        if (predicateBuilder == null)
            throw new IllegalArgumentException("null predicateBuilder");
        return this.withConfig((builder, query) -> {
            final S result = this.configure(builder, query);
            this.and(builder, query, predicateBuilder.apply(result));
            return result;
        });
    }

    private void and(CriteriaBuilder builder, C query, Expression<Boolean> expression) {
        final Predicate oldRestriction = query.getRestriction();
        this.queryType.where(query, oldRestriction != null ? builder.and(oldRestriction, expression) : expression);
    }

// QueryInfo

    static QueryInfo getQueryInfo() {
        final QueryInfo info = CURRENT_INFO.get();
        if (info == null)
            throw new IllegalStateException("subquery not created in the context of a containing query");
        return info;
    }

    static <T> T withQueryInfo(CriteriaBuilder builder, CommonAbstractCriteria query, Supplier<T> action) {
        if (action == null)
            throw new IllegalArgumentException("null action");
        final QueryInfo prev = CURRENT_INFO.get();
        final QueryInfo info = new QueryInfo(builder, query);
        CURRENT_INFO.set(info);
        try {
            return action.get();
        } finally {
            CURRENT_INFO.set(prev);
        }
    }

    static class QueryInfo {

        private final CriteriaBuilder builder;
        private final CommonAbstractCriteria query;

        QueryInfo(CriteriaBuilder builder, CommonAbstractCriteria query) {
            if (builder == null)
                throw new IllegalArgumentException("null builder");
            if (query == null)
                throw new IllegalArgumentException("null query");
            this.builder = builder;
            this.query = query;
        }

        public CriteriaBuilder getBuilder() {
            return this.builder;
        }

        public CommonAbstractCriteria getQuery() {
            return this.query;
        }

        public Subquery<?> getSubquery() {
            if (!(this.query instanceof Subquery))
                throw new IllegalArgumentException("streams built with QueryBuilder.substream() can only be used in subqueries");
            return (Subquery<?>)this.query;
        }
    }
}

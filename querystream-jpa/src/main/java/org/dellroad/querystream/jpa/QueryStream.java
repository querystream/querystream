
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CommonAbstractCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

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
     * @return new Criteria API query corresponding to this instance
     */
    C2 toCriteriaQuery();

    /**
     * Build a fully configured JPA query based on this instance.
     *
     * @return new JPA query corresponding to this instance
     */
    Q toQuery();

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
     */
    final class Builder {

        private final EntityManager entityManager;

        private Builder(EntityManager entityManager) {
            if (entityManager == null)
                throw new IllegalArgumentException("null entityManager");
            this.entityManager = entityManager;
        }

        /**
         * Get the {@link CriteriaBuilder} associated with this instance.
         *
         * @return {@link CriteriaBuilder} created from this instance's {@link EntityManager}
         */
        public CriteriaBuilder getCriteriaBuilder() {
            return this.entityManager.getCriteriaBuilder();
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

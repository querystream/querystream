
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
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

class SearchValueImpl<X, S extends Selection<X>> extends SearchStreamImpl<X, S> implements SearchValue<X, S> {

// Constructors

    SearchValueImpl(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, QueryInfo queryInfo) {
        super(entityManager, queryType, configurer, queryInfo);
    }

// Subclass required methods

    @Override
    SearchValue<X, S> create(EntityManager entityManager, SearchType<X> queryType,
      QueryConfigurer<AbstractQuery<?>, X, ? extends S> configurer, QueryInfo queryInfo) {
        return new SearchValueImpl<>(entityManager, queryType, configurer, queryInfo);
    }

// Narrowing overrides (QueryStream)

    @Override
    public SearchValue<X, S> bind(Ref<X, ? super S> ref) {
        return (SearchValue<X, S>)super.bind(ref);
    }

    @Override
    public SearchValue<X, S> peek(Consumer<? super S> peeker) {
        return (SearchValue<X, S>)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> SearchValue<X, S> bind(
      Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction) {
        return (SearchValue<X, S>)super.bind(ref, refFunction);
    }

    @Override
    public SearchValue<X, S> filter(SingularAttribute<? super X, Boolean> attribute) {
        return (SearchValue<X, S>)super.filter(attribute);
    }

    @Override
    public SearchValue<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder) {
        return (SearchValue<X, S>)super.filter(predicateBuilder);
    }

    @Override
    public SearchValue<X, S> withFlushMode(FlushModeType flushMode) {
        return (SearchValue<X, S>)super.withFlushMode(flushMode);
    }

    @Override
    public SearchValue<X, S> withLockMode(LockModeType lockMode) {
        return (SearchValue<X, S>)super.withLockMode(lockMode);
    }

    @Override
    public SearchValue<X, S> withHint(String name, Object value) {
        return (SearchValue<X, S>)super.withHint(name, value);
    }

    @Override
    public SearchValue<X, S> withHints(Map<String, Object> hints) {
        return (SearchValue<X, S>)super.withHints(hints);
    }

    @Override
    public SearchValue<X, S> withLoadGraph(String name) {
        return (SearchValue<X, S>)super.withLoadGraph(name);
    }

    @Override
    public SearchValue<X, S> withFetchGraph(String name) {
        return (SearchValue<X, S>)super.withFetchGraph(name);
    }
}


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

class LongValueImpl extends LongStreamImpl implements LongValue {

// Constructors

    LongValueImpl(EntityManager entityManager,
      QueryConfigurer<AbstractQuery<?>, Long, ? extends Expression<Long>> configurer, QueryInfo queryInfo) {
        super(entityManager, configurer, queryInfo);
    }

// Subclass required methods

    @Override
    LongValue create(EntityManager entityManager, SearchType<Long> queryType,
      QueryConfigurer<AbstractQuery<?>, Long, ? extends Expression<Long>> configurer, QueryInfo queryInfo) {
        return new LongValueImpl(entityManager, configurer, queryInfo);
    }

// Narrowing overrides (QueryStream)

    @Override
    public LongValue bind(Ref<Long, ? super Expression<Long>> ref) {
        return (LongValue)super.bind(ref);
    }

    @Override
    public LongValue peek(Consumer<? super Expression<Long>> peeker) {
        return (LongValue)super.peek(peeker);
    }

    @Override
    public <X2, S2 extends Selection<X2>> LongValue bind(
      Ref<X2, ? super S2> ref, Function<? super Expression<Long>, ? extends S2> refFunction) {
        return (LongValue)super.bind(ref, refFunction);
    }

    @Override
    public LongValue filter(SingularAttribute<? super Long, Boolean> attribute) {           // makes no sense but needed for API
        return (LongValue)super.filter(attribute);
    }

    @Override
    public LongValue filter(Function<? super Expression<Long>, ? extends Expression<Boolean>> predicateBuilder) {
        return (LongValue)super.filter(predicateBuilder);
    }

    @Override
    public LongValue withFlushMode(FlushModeType flushMode) {
        return (LongValue)super.withFlushMode(flushMode);
    }

    @Override
    public LongValue withLockMode(LockModeType lockMode) {
        return (LongValue)super.withLockMode(lockMode);
    }

    @Override
    public LongValue withHint(String name, Object value) {
        return (LongValue)super.withHint(name, value);
    }

    @Override
    public LongValue withHints(Map<String, Object> hints) {
        return (LongValue)super.withHints(hints);
    }

    @Override
    public LongValue withLoadGraph(String name) {
        return (LongValue)super.withLoadGraph(name);
    }

    @Override
    public LongValue withFetchGraph(String name) {
        return (LongValue)super.withFetchGraph(name);
    }
}

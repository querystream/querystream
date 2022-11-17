
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.TemporalType;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.dellroad.querystream.jpa.querytype.SearchType;

/**
 * A {@link FromStream} that is guaranteed to return at most a single result.
 */
public interface FromValue<X, S extends From<?, X>> extends PathValue<X, S>, FromStream<X, S> {

// Narrowing overrides (FromStream)

    @Override
    default <Y> FromValue<Y, From<X, Y>> join(SingularAttribute<? super X, Y> attribute) {
        return this.join(attribute, JoinType.INNER);
    }

    @Override
    default <Y> FromValue<Y, From<X, Y>> join(SingularAttribute<? super X, Y> attribute, JoinType joinType) {
        return this.join(attribute, joinType, join -> null);
    }

    @Override
    default <Y> FromValue<Y, From<X, Y>> join(SingularAttribute<? super X, Y> attribute, JoinType joinType,
      Function<? super Join<X, Y>, ? extends Expression<Boolean>> on) {
        if (attribute == null)
            throw new IllegalArgumentException("null attribute");
        if (joinType == null)
            throw new IllegalArgumentException("null joinType");
        if (on == null)
            throw new IllegalArgumentException("null on");
        QueryStreamImpl.checkOffsetLimit(this, "join()");
        return new FromValueImpl<>(this.getEntityManager(), new SearchType<>(attribute.getJavaType()),
           (builder, query) -> {
            final Join<X, Y> join = this.configure(builder, query).join(attribute, joinType);
            final Expression<Boolean> onPredicate = on.apply(join);
            if (onPredicate != null)
                join.on(onPredicate);
            return join;
          },
          QueryInfo.of(this));
    }

// Narrowing overrides (QueryStream)

    @Override
    FromValue<X, S> bind(Ref<X, ? super S> ref);

    @Override
    FromValue<X, S> peek(Consumer<? super S> peeker);

    @Override
    <X2, S2 extends Selection<X2>> FromValue<X, S> bind(Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction);

    @Override
    FromValue<X, S> filter(SingularAttribute<? super X, Boolean> attribute);

    @Override
    FromValue<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder);

    @Override
    FromValue<X, S> withFlushMode(FlushModeType flushMode);

    @Override
    FromValue<X, S> withLockMode(LockModeType lockMode);

    @Override
    FromValue<X, S> withHint(String name, Object value);

    @Override
    FromValue<X, S> withHints(Map<String, Object> hints);

    @Override
    <T> FromValue<X, S> withParam(Parameter<T> parameter, T value);

    @Override
    FromValue<X, S> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType);

    @Override
    FromValue<X, S> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType);

    @Override
    FromValue<X, S> withParams(Iterable<? extends ParamBinding<?>> params);

    @Override
    FromValue<X, S> withLoadGraph(String name);

    @Override
    FromValue<X, S> withFetchGraph(String name);
}

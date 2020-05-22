
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Calendar;
import java.util.Collection;
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
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

/**
 * An {@link ExprStream} that is guaranteed to return at most a single result.
 */
public interface ExprValue<X, S extends Expression<X>> extends SearchValue<X, S>, ExprStream<X, S> {

// Narrowing overrides (ExprStream)

    @Override
    default <Y> PathValue<Y, Path<Y>> map(SingularAttribute<? super X, Y> attribute) {
        return ((PathStreamImpl<Y, Path<Y>>)ExprStream.super.map(attribute)).toValue();
    }

    @Override
    default <Y> ExprValue<Y, Expression<Y>> map(Class<Y> type, Function<? super S, ? extends Expression<Y>> exprFunction) {
        return ((ExprStreamImpl<Y, Expression<Y>>)ExprStream.super.map(type, exprFunction)).toValue();
    }

    @Override
    default <E, C extends Collection<E>> ExprValue<C, Expression<C>> map(PluralAttribute<? super X, C, E> attribute) {
        return ((ExprStreamImpl<C, Expression<C>>)ExprStream.super.map(attribute)).toValue();
    }

    @Override
    default <K, V, M extends Map<K, V>> ExprValue<M, Expression<M>> map(MapAttribute<? super X, K, V> attribute) {
        return ((ExprStreamImpl<M, Expression<M>>)ExprStream.super.<K, V, M>map(attribute)).toValue();
    }

    @Override
    default <Y> PathValue<Y, Path<Y>> mapToPath(Class<Y> type, Function<? super S, ? extends Path<Y>> pathFunction) {
        return ((PathStreamImpl<Y, Path<Y>>)ExprStream.super.mapToPath(type, pathFunction)).toValue();
    }

    @Override
    default <Z, Y> FromValue<Y, From<Z, Y>> mapToFrom(Class<Y> type, Function<? super S, ? extends From<Z, Y>> fromFunction) {
        return ((FromStreamImpl<Y, From<Z, Y>>)ExprStream.super.mapToFrom(type, fromFunction)).toValue();
    }

    @Override
    default DoubleValue mapToDouble(SingularAttribute<? super X, ? extends Number> attribute) {
        return ((DoubleStreamImpl)ExprStream.super.mapToDouble(attribute)).toValue();
    }

    @Override
    default DoubleValue mapToDouble(Function<? super S, ? extends Expression<? extends Number>> doubleExprFunction) {
        return ((DoubleStreamImpl)ExprStream.super.mapToDouble(doubleExprFunction)).toValue();
    }

    @Override
    default LongValue mapToLong(SingularAttribute<? super X, ? extends Number> attribute) {
        return ((LongStreamImpl)ExprStream.super.mapToLong(attribute)).toValue();
    }

    @Override
    default LongValue mapToLong(Function<? super S, ? extends Expression<? extends Number>> longExprFunction) {
        return ((LongStreamImpl)ExprStream.super.mapToLong(longExprFunction)).toValue();
    }

    @Override
    default IntValue mapToInt(SingularAttribute<? super X, ? extends Number> attribute) {
        return ((IntStreamImpl)ExprStream.super.mapToInt(attribute)).toValue();
    }

    @Override
    default IntValue mapToInt(Function<? super S, ? extends Expression<? extends Number>> intExprFunction) {
        return ((IntStreamImpl)ExprStream.super.mapToInt(intExprFunction)).toValue();
    }

// Narrowing overrides (QueryStream)

    @Override
    ExprValue<X, S> bind(Ref<X, ? super S> ref);

    @Override
    ExprValue<X, S> peek(Consumer<? super S> peeker);

    @Override
    <X2, S2 extends Selection<X2>> ExprValue<X, S> bind(Ref<X2, ? super S2> ref, Function<? super S, ? extends S2> refFunction);

    @Override
    ExprValue<X, S> filter(SingularAttribute<? super X, Boolean> attribute);

    @Override
    ExprValue<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder);

    @Override
    ExprValue<X, S> withFlushMode(FlushModeType flushMode);

    @Override
    ExprValue<X, S> withLockMode(LockModeType lockMode);

    @Override
    ExprValue<X, S> withHint(String name, Object value);

    @Override
    ExprValue<X, S> withHints(Map<String, Object> hints);

    @Override
    <T> ExprValue<X, S> withParam(Parameter<T> parameter, T value);

    @Override
    ExprValue<X, S> withParam(Parameter<Date> parameter, Date value, TemporalType temporalType);

    @Override
    ExprValue<X, S> withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType);

    @Override
    ExprValue<X, S> withParams(Iterable<? extends ParamBinding<?>> params);

    @Override
    ExprValue<X, S> withLoadGraph(String name);

    @Override
    ExprValue<X, S> withFetchGraph(String name);
}

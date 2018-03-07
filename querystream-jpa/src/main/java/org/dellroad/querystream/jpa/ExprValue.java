
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
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
    ExprValue<X, S> filter(SingularAttribute<? super X, Boolean> attribute);

    @Override
    ExprValue<X, S> filter(Function<? super S, ? extends Expression<Boolean>> predicateBuilder);
}

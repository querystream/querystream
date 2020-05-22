
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 * Captures the information applied to the {@link javax.persistence.Query} instead of the
 * {@link javax.persistence.criteria.CriteriaQuery}.
 *
 * <p>
 * Instances are more or less immutable.
 */
class QueryInfo {

    private final int firstResult;
    private final int maxResults;
    private final FlushModeType flushMode;                      // may be null
    private final LockModeType lockMode;                        // may be null
    private final Map<String, Object> hints;                    // may be null
    private final Map<Parameter<?>, ParamBinding<?>> params;    // may be null

// Constructors

    QueryInfo() {
        this(-1, -1, null, null, null, null);
    }

    QueryInfo(int firstResult, int maxResults, FlushModeType flushMode,
      LockModeType lockMode, Map<String, Object> hints, Map<Parameter<?>, ParamBinding<?>> params) {
        if (firstResult < -1)
            throw new IllegalArgumentException("invalid firstResult");
        if (maxResults < -1)
            throw new IllegalArgumentException("invalid maxResults");
        this.firstResult = firstResult;
        this.maxResults = maxResults;
        this.flushMode = flushMode;
        this.lockMode = lockMode;
        this.hints = hints;
        this.params = params;
    }

// Kludgey hack

    public static QueryInfo of(QueryStream<?, ?, ?, ?, ?> stream) {
        return ((QueryStreamImpl<?, ?, ?, ?, ?, ?>)stream).queryInfo;
    }

// Configure the given query from this instance

    public void applyTo(Query query) {
        if (this.firstResult != -1)
            query.setFirstResult(this.firstResult);
        if (this.maxResults != -1)
            query.setMaxResults(this.maxResults);
        if (this.flushMode != null)
            query.setFlushMode(this.flushMode);
        if (this.lockMode != null)
            query.setLockMode(this.lockMode);
        if (this.hints != null) {
            for (Map.Entry<String, Object> hint : this.hints.entrySet())
                query.setHint(hint.getKey(), hint.getValue());
        }
        if (this.params != null)
            this.params.values().forEach(param -> param.applyTo(query));
    }

    /**
     * Merge a QueryInfo produced in a subquery into this instance, checking for conflicts.
     *
     * @param that subquery's {@link QueryInfo}
     * @throws IllegalArgumentException if {@code that} is null
     */
    public QueryInfo withMergedInfo(QueryInfo that) {
        if (that == null)
            throw new IllegalArgumentException("null that");
        if (that.firstResult != -1)
            QueryStreamImpl.failJpaRestriction("can't invoke skip() on a subquery");
        if (that.maxResults != -1)
            QueryStreamImpl.failJpaRestriction("can't invoke limit() on a subquery");
        if (that.flushMode != null && !Objects.equals(this.flushMode, that.flushMode)) {
            throw new IllegalArgumentException("conflicting JPA flush mode specified on query ("
              + this.flushMode + ") and nested subquery (" + that.flushMode + ")");
        }
        if (that.lockMode != null && !Objects.equals(this.lockMode, that.lockMode)) {
            throw new IllegalArgumentException("conflicting JPA lock mode specified on query ("
              + this.lockMode + ") and nested subquery (" + that.lockMode + ")");
        }
        return new QueryInfo(this.firstResult, this.maxResults, this.flushMode, this.lockMode,
          this.merge("value", name -> "hint \"" + name + "\"", this.hints, that.hints),
          this.merge("binding", ParamBinding::describeParameter, this.params, that.params));
    }

    private <K, V> Map<K, V> merge(String valueName,
      Function<? super K, String> keyDescriber, Map<K, V> thisMap, Map<K, V> thatMap) {
        if (thisMap == null && thatMap == null)
            return null;
        if (thisMap == null)
            return new HashMap<>(thatMap);
        if (thatMap == null)
            return new HashMap<>(thisMap);
        final Map<K, V> newMap = new HashMap<>(thisMap);
        for (Map.Entry<K, V> entry : thatMap.entrySet()) {
            K key = entry.getKey();
            if (!thisMap.containsKey(key)) {
                newMap.put(key, entry.getValue());
                continue;
            }
            final V thisValue = thisMap.get(key);
            final V thatValue = entry.getValue();
            final Object keyDesc = key instanceof String ? "\"" + key + "\"" : key;
            if (!Objects.equals(thisValue, thatValue)) {
                throw new IllegalArgumentException("conflicting " + valueName + " specified for "
                  + keyDescriber.apply(key) + " on outer query (" + thisValue + ") and nested subquery (" + thatValue + ")");
            }
            newMap.put(key, thisValue);
        }
        return newMap;
    }

// Limits

    public int getFirstResult() {
        return this.firstResult;
    }
    public QueryInfo withFirstResult(final int newFirstResult) {
        return new QueryInfo(newFirstResult, this.maxResults, this.flushMode, this.lockMode, this.hints, this.params);
    }

    public int getMaxResults() {
        return this.maxResults;
    }
    public QueryInfo withMaxResults(final int newMaxResults) {
        return new QueryInfo(this.firstResult, newMaxResults, this.flushMode, this.lockMode, this.hints, this.params);
    }

// FlushMode

    public FlushModeType getFlushMode() {
        return this.flushMode;
    }
    public QueryInfo withFlushMode(final FlushModeType newFlushMode) {
        return new QueryInfo(this.firstResult, this.maxResults, newFlushMode, this.lockMode, this.hints, this.params);
    }

// LockMode

    public LockModeType getLockMode() {
        return this.lockMode;
    }
    public QueryInfo withLockMode(final LockModeType newLockMode) {
        return new QueryInfo(this.firstResult, this.maxResults, this.flushMode, newLockMode, this.hints, this.params);
    }

// Hints

    public Map<String, Object> getHints() {
        return this.hints != null ? Collections.unmodifiableMap(this.hints) : Collections.emptyMap();
    }
    public QueryInfo withHint(String name, Object value) {
        if (name == null)
            throw new IllegalArgumentException("null name");
        return this.withHints(Collections.singletonMap(name, value));
    }
    public QueryInfo withHints(Map<String, Object> moreHints) {
        if (moreHints == null)
            throw new IllegalArgumentException("null hints");
        if (moreHints.keySet().stream().anyMatch(key -> key == null))
            throw new IllegalArgumentException("null key");
        final HashMap<String, Object> newHints = new HashMap<>((this.hints != null ? this.hints.size() : 0) + moreHints.size());
        if (this.hints != null)
            newHints.putAll(this.hints);
        newHints.putAll(moreHints);
        return new QueryInfo(this.firstResult, this.maxResults, this.flushMode, this.lockMode, newHints, this.params);
    }

// Params

    public Set<ParamBinding<?>> getParams() {
        return this.params != null ? Collections.unmodifiableSet(new HashSet<>(this.params.values())) : Collections.emptySet();
    }
    public <T> QueryInfo withParam(Parameter<T> parameter, T value) {
        return this.withParams(Collections.singleton(new ParamBinding<T>(parameter, value)));
    }
    public QueryInfo withParam(Parameter<Date> parameter, Date value, TemporalType temporalType) {
        return this.withParams(Collections.singleton(new DateParamBinding(parameter, value, temporalType)));
    }
    public QueryInfo withParam(Parameter<Calendar> parameter, Calendar value, TemporalType temporalType) {
        return this.withParams(Collections.singleton(new CalendarParamBinding(parameter, value, temporalType)));
    }
    public QueryInfo withParams(Iterable<? extends ParamBinding<?>> moreParams0) {
        if (moreParams0 == null)
            throw new IllegalArgumentException("null params");
        final HashSet<ParamBinding<?>> moreParams = new HashSet<>();
        for (ParamBinding<?> param : moreParams0)
            moreParams.add(param);
        if (moreParams.stream().map(ParamBinding::getParameter).collect(Collectors.toSet()).size() < moreParams.size())
            throw new IllegalArgumentException("duplicated parameter");
        final HashMap<Parameter<?>, ParamBinding<?>> newParams
          = new HashMap<>((this.params != null ? this.params.size() : 0) + moreParams.size());
        if (this.params != null)
            newParams.putAll(this.params);
        moreParams.forEach(binding -> newParams.put(binding.getParameter(), binding));
        return new QueryInfo(this.firstResult, this.maxResults, this.flushMode, this.lockMode, this.hints, newParams);
    }
}

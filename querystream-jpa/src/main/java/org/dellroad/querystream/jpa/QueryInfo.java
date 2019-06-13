
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
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 * Captures the information applied to the {@link javax.persistence.Query} instead of the
 * {@link javax.persistence.criteria.CriteriaQuery}.
 */
class QueryInfo {

    private final int firstResult;
    private final int maxResults;
    private final FlushModeType flushMode;                      // may be null
    private final LockModeType lockMode;                        // may be null
    private final Map<String, Object> hints;                    // may be null
    private final Map<Parameter<?>, ParamBinding<?>> params;    // may be null

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

    public static QueryInfo of(QueryStream<?, ?, ?, ?, ?> stream) {
        return ((QueryStreamImpl<?, ?, ?, ?, ?, ?>)stream).queryInfo;
    }

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
    public QueryInfo withParams(Set<ParamBinding<?>> moreParams) {
        if (moreParams == null)
            throw new IllegalArgumentException("null params");
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

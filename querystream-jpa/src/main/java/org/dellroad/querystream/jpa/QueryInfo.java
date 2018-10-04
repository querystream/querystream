
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

/**
 * Captures the information applied to the {@link javax.persistence.Query} instead of the
 * {@link javax.persistence.criteria.CriteriaQuery}.
 */
class QueryInfo {

    private final int firstResult;
    private final int maxResults;
    private final FlushModeType flushMode;              // may be null
    private final LockModeType lockMode;                // may be null
    private final Map<String, Object> hints;            // may be null

    QueryInfo() {
        this(-1, -1, null, null, null);
    }

    QueryInfo(int firstResult, int maxResults, FlushModeType flushMode, LockModeType lockMode, Map<String, Object> hints) {
        if (firstResult < -1)
            throw new IllegalArgumentException("invalid firstResult");
        if (maxResults < -1)
            throw new IllegalArgumentException("invalid maxResults");
        this.firstResult = firstResult;
        this.maxResults = maxResults;
        this.flushMode = flushMode;
        this.lockMode = lockMode;
        this.hints = hints;
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
    }

    public int getFirstResult() {
        return this.firstResult;
    }
    public QueryInfo withFirstResult(final int newFirstResult) {
        return new QueryInfo(newFirstResult, this.maxResults, this.flushMode, this.lockMode, this.hints);
    }

    public int getMaxResults() {
        return this.maxResults;
    }
    public QueryInfo withMaxResults(final int newMaxResults) {
        return new QueryInfo(this.firstResult, newMaxResults, this.flushMode, this.lockMode, this.hints);
    }

    public FlushModeType getFlushMode() {
        return this.flushMode;
    }
    public QueryInfo withFlushMode(final FlushModeType newFlushMode) {
        return new QueryInfo(this.firstResult, this.maxResults, newFlushMode, this.lockMode, this.hints);
    }

    public LockModeType getLockMode() {
        return this.lockMode;
    }
    public QueryInfo withLockMode(final LockModeType newLockMode) {
        return new QueryInfo(this.firstResult, this.maxResults, this.flushMode, newLockMode, this.hints);
    }

    public Map<String, Object> getHints() {
        return this.hints != null ? Collections.unmodifiableMap(this.hints) : Collections.emptyMap();
    }
    public QueryInfo withHint(String name, Object value) {
        return this.withHints(Collections.singletonMap(name, value));
    }
    public QueryInfo withHints(Map<String, Object> moreHints) {
        if (moreHints == null)
            throw new IllegalArgumentException("null hints");
        final HashMap<String, Object> newHints = new HashMap<>((this.hints != null ? this.hints.size() : 0) + moreHints.size());
        if (this.hints != null)
            newHints.putAll(this.hints);
        newHints.putAll(moreHints);
        return new QueryInfo(this.firstResult, this.maxResults, this.flushMode, this.lockMode, newHints);
    }
}

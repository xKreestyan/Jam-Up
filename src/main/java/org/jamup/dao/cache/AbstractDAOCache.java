package org.jamup.dao.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDAOCache<T> {

    private final Map<String, T> tCache;

    protected AbstractDAOCache() {
        tCache = new HashMap<>();
    }

    public boolean isInCache(String id) {
        return tCache.containsKey(id);
    }

    public T fetchFromCache(String id){
        return tCache.get(id);
    }

    public void putInCache(String id, T entity){
        tCache.put(id, entity);
    }

    public Collection<T> getAllFromCache() {
        return tCache.values();
    }

}

/* TODO */
// CONTROLLA CHE LA CACHE DELLE NOTIFICHE LE ABBIA MEMORIZZATE CON CHIAVE ID O RECIPIENTID
// FINISCI DI CONTROLLARE IL FUNZIONAMENTO DI TUTTI I DAO CON CACHE
package org.jamup.dao.cache;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDAOCache<T> {

    /**
     * Internal storage for cached entities.
     */
    private final Map<String, T> tCache;

    /**
     * Initializes a new instance of the cache with an empty map.
     */
    protected AbstractDAOCache() {
        tCache = new HashMap<>();
    }

    /**
     * Checks if an entity with the specified ID exists in the cache.
     *
     * @param id the unique identifier of the entity
     * @return true if the entity is cached, false otherwise
     */
    public boolean isInCache(String id) {
        return tCache.containsKey(id);
    }

    /**
     * Retrieves an entity from the cache by its ID.
     *
     * @param id the unique identifier of the entity
     * @return the cached entity, or null if not found
     */
    public T fetchFromCache(String id){
        return tCache.get(id);
    }

    /**
     * Adds or updates an entity in the cache.
     *
     * @param id     the unique identifier for the entity
     * @param entity the entity to be stored
     */
    public void putInCache(String id, T entity){
        tCache.put(id, entity);
    }

}
package com.hw.DataStorage;

import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.map.LRUMap;

import java.util.ArrayList;

/**
 * Created by Pasha on 5/27/2016.
 */
public class MemoryCache<K, T> {

    private long timeToLive;
    private LRUMap mCacheMap;

    protected class CacheObject {
        long lastAccessed = System.currentTimeMillis();
        T value;

        protected CacheObject(T value) {
            this.value = value;
        }
    }

    /**
     *
     * @param TimeToLive Time in seconds that object stay in cache
     * @param cacheTimerInterval Time interval for cleanup thread to awake up
     * @param maxItems maximum number of items that can stay in cache
     */
    public MemoryCache(long TimeToLive, final long cacheTimerInterval, int maxItems) {
        this.timeToLive = TimeToLive * 1000;

        mCacheMap = new LRUMap(maxItems);

        if (timeToLive > 0 && cacheTimerInterval > 0) {

            Thread t = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(cacheTimerInterval * 1000);
                        } catch (InterruptedException ex) {
                        }
                        cleanup();
                    }
                }
            });

            t.setDaemon(true);
            t.start();
        }
    }


    public void put(K key, T value) {
        synchronized (mCacheMap) {
            mCacheMap.put(key, new CacheObject(value));
        }
    }

    @SuppressWarnings("unchecked")
    public T get(K key) {
        synchronized (mCacheMap) {
            CacheObject c = (CacheObject) mCacheMap.get(key);

            if (c == null)
                return null;
            else {
                c.lastAccessed = System.currentTimeMillis();
                return c.value;
            }
        }
    }

    public void remove(K key) {
        synchronized (mCacheMap) {
            mCacheMap.remove(key);
        }
    }

    public int size() {
        synchronized (mCacheMap) {
            return mCacheMap.size();
        }
    }

    /**
     * This code runs under inner demon thread that work like garbage collector
     * and clear the cache from unused object that not used for longer then timeToLive time.
     */
    @SuppressWarnings("unchecked")
    public void cleanup() {

        long now = System.currentTimeMillis();
        ArrayList<K> deleteKey = null;

        synchronized (mCacheMap) {
            MapIterator itr = mCacheMap.mapIterator();

            deleteKey = new ArrayList<K>((mCacheMap.size() / 2) + 1);
            K key = null;
            CacheObject c = null;

            while (itr.hasNext()) {
                key = (K) itr.next();
                c = (CacheObject) itr.getValue();

                if (c != null && (now > (timeToLive + c.lastAccessed))) {
                    deleteKey.add(key);
                }
            }
        }

        for (K key : deleteKey) {
            synchronized (mCacheMap) {
                mCacheMap.remove(key);
            }

            Thread.yield();
        }
    }
}

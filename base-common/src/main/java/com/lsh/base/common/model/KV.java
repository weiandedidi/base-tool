package com.lsh.base.common.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 键值对
 * Created by huangdong on 16/8/28.
 */
public class KV<K, V> implements Serializable {

    private static final long serialVersionUID = -5954158951466913500L;

    private final K key;

    private V value;

    public KV(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public static <K, V> Map<K, V> toMap(Collection<KV<K, V>> coll) {
        if (coll == null || coll.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<K, V> map = new HashMap<K, V>();
        for (KV<K, V> item : coll) {
            if (item == null) {
                continue;
            }
            map.put(item.getKey(), item.getValue());
        }
        return map;
    }
}

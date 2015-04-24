package com.appmodel.util;

import java.util.LinkedHashMap;

public class MapWithIndex<K, V> extends LinkedHashMap<K, V> {

    public V valueAt(int index) {
        return get(keyAt(index));
    }

    public K keyAt(int index) {
        int i = 0;
        for (K key : keySet()) {
            if (i++ == index) {
                return key;
            }
        }
        return null;
    }

}

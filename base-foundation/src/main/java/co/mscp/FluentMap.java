/*---[SPEEDA Radar]--------------------------------------------m(._.)m--------*\
 |
 |  Copyright (c) 2018 Uzabase Inc. all rights reserved.
 |
 |  Author: Asia PDT (asia-pdt@uzabase.com)
 |
 *//////////////////////////////////////////////////////////////////////////////


package co.mscp;

import java.util.HashMap;

public class FluentMap<K, V> extends HashMap<K, V> {
    
    public static <K, V> FluentMap<K, V> empty() {
        return new FluentMap<>();
    }
    
    public static <K, V> FluentMap<K, V> with(K key, V value) {
        FluentMap<K, V> m = empty();
        m.put(key, value);
        return m;
    }
    
    
    public FluentMap<K, V> andWith(K key, V value) {
        put(key, value);
        return this;
    }
    
}

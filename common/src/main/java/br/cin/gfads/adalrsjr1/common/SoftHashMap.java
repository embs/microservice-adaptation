package br.cin.gfads.adalrsjr1.common;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//http://www.java2s.com/Code/Java/Collections-Data-Structure/MapwhichstoresitemsusingSoftReference.htm
public class SoftHashMap<K,V> extends AbstractMap<K, V> {

	private Map<K, SoftValue<V>> map;
	private ReferenceQueue<V> queue = new ReferenceQueue<>();
	
	public SoftHashMap() {
		map = new HashMap<>();
	}
	
	private void processQueue() {
        while (true) {
            Reference<? extends V> o = queue.poll();
            if (o == null) {
                return;
            }
            SoftValue<V> k = (SoftValue<V>) o;
            Object key = k.key;
            map.remove(key);
        }
    }

    public V get(Object key) {
        processQueue();
        SoftReference<V> o = map.get(key);
        if (o == null) {
            return null;
        }
        return o.get();
    }
    
    public V put(K key, V value) {
        processQueue();
        SoftValue<V> old = map.put(key, new SoftValue<V>(value, queue, key));
        return old == null ? null : old.get();
    }
	
    public V remove(Object key) {
        processQueue();
        SoftReference<V> ref = map.remove(key);
        return ref == null ? null : ref.get();
    }

    public void clear() {
        processQueue();
        map.clear();
    }

    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }
	
	private static class SoftValue<T> extends SoftReference<T> {
		final Object key;
		
		public SoftValue(T referent, ReferenceQueue<? super T> q, Object key) {
			super(referent, q);
			this.key = key;
		}

		
		
	}

}


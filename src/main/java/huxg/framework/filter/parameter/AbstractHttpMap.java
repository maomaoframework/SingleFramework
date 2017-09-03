package huxg.framework.filter.parameter;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 对于Http中的数据进行包装的Map抽象类
 * 
 * @author 胡晓光
 */
public abstract class AbstractHttpMap implements Map {

    protected abstract Enumeration getNames();

    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    /**
     * Returns the number of key-value mappings in this map.<br>
     * If the map contains more than <tt>Integer.MAX_VALUE</tt> elements,<br>
     * returns <tt>Integer.MAX_VALUE</tt>. <br>
     * 从{@link #getNames()}计算得出.
     * 
     * @return the number of key-value mappings in this map
     * @see #getNames()
     */
    public int size() {
        Enumeration emu = getNames();
        int size = 0;
        while (emu.hasMoreElements()) {
            emu.nextElement();
            size++;
        }
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void putAll(Map m) {
        Iterator iter = m.entrySet().iterator();
        Map.Entry e;
        while (iter.hasNext()) {
            e = (Entry) iter.next();
            put(e.getKey(), e.getValue());
        }
    }

    /**
     * 返回name的集合
     * 
     * @see #getNames()
     */
    public Set keySet() {
        Set keySet = new HashSet();
        Enumeration emu = getNames();
        while (emu.hasMoreElements()) {
            keySet.add(emu.nextElement());
        }
        return keySet;
    }

    /**
     * @deprecated 不支持该方法
     * @throws java.lang.UnsupportedOperationException
     */
    public void clear() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    /**
     * @deprecated 不支持该方法
     * @throws java.lang.UnsupportedOperationException
     */
    public Set entrySet() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    /**
     * @deprecated 不支持该方法
     * @throws java.lang.UnsupportedOperationException
     */
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException(getClass().getName());
    }

    /**
     * @deprecated 不支持该方法
     * @throws java.lang.UnsupportedOperationException
     */
    public Collection values() {
        throw new UnsupportedOperationException(getClass().getName());
    }

}

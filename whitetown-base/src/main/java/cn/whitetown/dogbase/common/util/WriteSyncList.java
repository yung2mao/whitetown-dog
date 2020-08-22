package cn.whitetown.dogbase.common.util;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 写同步list
 * @author taixian
 * @date 2020/08/21
 **/
public class WriteSyncList<T> implements List<T> {

    Integer lock = 0;

    private List<T> list;

    public WriteSyncList(List<T> list) {
        this.list = list;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(T t) {
        synchronized (lock) {
            return list.add(t);
        }
    }

    @Override
    public boolean remove(Object o) {
        synchronized (lock) {
            return list.remove(o);
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        synchronized (lock) {
            return list.addAll(c);
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        synchronized (lock) {
            return list.addAll(index,c);
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        synchronized (lock) {
            return list.removeAll(c);
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        synchronized (lock) {
            return list.retainAll(c);
        }
    }

    @Override
    public void clear() {
        synchronized (lock) {
            list.clear();
        }
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public T set(int index, T element) {
        synchronized (lock) {
            return list.set(index,element);
        }
    }

    @Override
    public void add(int index, T element) {
        synchronized (lock) {
            list.add(index,element);
        }
    }

    @Override
    public T remove(int index) {
        synchronized (lock) {
            return list.remove(index);
        }
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        synchronized (lock) {
            return list.subList(fromIndex, toIndex);
        }
    }

    @Override
    public String toString() {
        return list.toString();
    }
}

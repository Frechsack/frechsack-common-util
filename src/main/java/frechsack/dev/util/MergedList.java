package frechsack.dev.util;

import java.lang.reflect.Array;
import java.util.*;


public class MergedList {

    private MergedList() {}

    @SafeVarargs
    public static <E> List<E> of(List<E>... lists){
        boolean isRandomAccess = true;
        for (List<E> list : lists) if(!(list instanceof RandomAccess)) {
            isRandomAccess = false;
            break;
        }

        return isRandomAccess ? new MergedLinkedList<>(lists) : new MergedRandomAccessList<>(lists);
    }

    private static <E> int computeListIndex(List<E>[] lists, int index){
        int listLastIndex = 0;
        for(int i = 0; i < lists.length; i++){
            listLastIndex += lists[i].size();
            if(index < listLastIndex) return i;
        }
        // WIll throw an exception
        throw new IndexOutOfBoundsException(index);
    }

    private static <E>  int computeListOffset(List<E>[] lists, int listIndex) {
        int offset = 0;
        for (int i = 0; i < listIndex;i++) offset += lists[i].size();
        return offset;
    }

    private static class MergedLinkedList<E> extends AbstractList<E> implements List<E>, RandomAccess {
        private final List<E>[] lists;

        public MergedLinkedList(List<E>[] lists) {
            this.lists = lists;
        }

        @Override
        public int size() {
            int size = 0;
            for (List<? extends E> list : lists) size += list.size();
            return size;
        }

        @Override
        public boolean isEmpty() {
            for (List<? extends E> list: lists) if(!list.isEmpty()) return false;
            return true;
        }

        @Override
        public boolean contains(Object o) {
            for (List<? extends E> list: lists) if(list.contains(o)) return true;
            return false;
        }

        @Override
        public Object[] toArray() {
            Object[] array = new Object[size()];
            Iterator<E> iterator = iterator();
            for(int i = 0; i < array.length; i++) array[i] = iterator.next();
            return array;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T[] toArray(T[] a) {
            final int size = size();
            if(a.length < size) {
                a = (a.getClass() == Object[].class)
                        ? (T[]) new Object[size()]
                        : (T[]) Array.newInstance(a.getClass().getComponentType(), size());
            }
            Iterator<E> iterator = iterator();
            for (int i = 0; i < size(); i++) a[i] = (T) iterator.next();
            if(a.length > size) for (int i = size; i < a.length; i++) a[i] = null;
            return a;
        }

        @Override
        public boolean add(E e) {
            if(lists.length != 0) return lists[lists.length - 1].add(e);
            return false;
        }

        @Override
        public boolean remove(Object o) {
            for (List<E> list : lists) if (list.remove(o)) return true;
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            for (Object o : c) if(!contains(o)) return false;
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            if(lists.length != 0) return lists[lists.length - 1].addAll(c);
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            int listIndex = MergedList.computeListIndex(lists,index);
            return lists[listIndex].addAll(index - MergedList.computeListIndex(lists,listIndex),c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            boolean isModified = false;
            for(Object o : c) if(remove(o)) isModified = true;
            return isModified;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            boolean isModified = false;
            for(List<E> list : lists) if(list.retainAll(c)) isModified = true;
            return isModified;
        }

        @Override
        public void clear() {
            for(List<E> list : lists) list.clear();
        }

        @Override
        public E get(int index) {
            int listIndex = MergedList.computeListIndex(lists,index);
            return lists[listIndex].get(index - MergedList.computeListOffset(lists,listIndex));
        }

        @Override
        public E set(int index, E element) {
            int listIndex = MergedList.computeListIndex(lists,index);
            return lists[listIndex].set(index - MergedList.computeListOffset(lists,listIndex),element);
        }

        @Override
        public void add(int index, E element) {
            int listIndex = MergedList.computeListIndex(lists,index);
            lists[listIndex].add(index - MergedList.computeListOffset(lists,listIndex),element);
        }

        @Override
        public E remove(int index) {
            int listIndex = MergedList.computeListIndex(lists,index);
            return lists[listIndex].remove(index - MergedList.computeListOffset(lists, listIndex));
        }

        @Override
        public int indexOf(Object o) {
            Iterator<E> iterator = this.iterator();
            int size = size();
            for (int i = 0; i < size; i++) if(Objects.equals(o,iterator.next())) return i;
            return -1;
        }

        @Override
        public int lastIndexOf(Object o) {
            int size = size();
            ListIterator<E> iterator = this.listIterator(this.size());
            for (int i = size() - 1; i >= 0 ; i--) if(Objects.equals(o,iterator.previous())) return i;
            return -1;
        }
    }


    public static class MergedRandomAccessList<E> extends AbstractList<E>  implements List<E>, RandomAccess {

        private final List<E>[] lists;

        public MergedRandomAccessList(List<E>[] lists) {
            this.lists = lists;
        }

        @Override
        public int size() {
            int size = 0;
            for (List<? extends E> list : lists) size += list.size();
            return size;
        }

        @Override
        public boolean isEmpty() {
            for (List<? extends E> list: lists) if(!list.isEmpty()) return false;
            return true;
        }

        @Override
        public boolean contains(Object o) {
            for (List<? extends E> list: lists) if(list.contains(o)) return true;
            return false;
        }

        @Override
        public Object[] toArray() {
            Object[] array = new Object[size()];
            for(int i = 0; i < array.length; i++) array[i] = get(i);
            return array;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T[] toArray(T[] a) {
            final int size = size();
            if(a.length < size) {
                a = (a.getClass() == Object[].class)
                        ? (T[]) new Object[size()]
                        : (T[]) Array.newInstance(a.getClass().getComponentType(), size());
            }
            for (int i = 0; i < size(); i++) a[i] = (T) get(i);
            if(a.length > size) for (int i = size; i < a.length; i++) a[i] = null;
            return a;
        }

        @Override
        public boolean add(E e) {
            if(lists.length != 0) return lists[lists.length - 1].add(e);
            return false;
        }

        @Override
        public boolean remove(Object o) {
            for (List<E> list : lists) if (list.remove(o)) return true;
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            for (Object o : c) if(!contains(o)) return false;
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            if(lists.length != 0) return lists[lists.length - 1].addAll(c);
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            int listIndex = MergedList.computeListIndex(lists,index);
            return lists[listIndex].addAll(index - MergedList.computeListOffset(lists,listIndex),c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            boolean isModified = false;
            for(Object o : c) if(remove(o)) isModified = true;
            return isModified;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            boolean isModified = false;
            for(List<E> list : lists) if(list.retainAll(c)) isModified = true;
            return isModified;
        }

        @Override
        public void clear() {
            for(List<E> list : lists) list.clear();
        }

        @Override
        public E get(int index) {
            int listIndex = MergedList.computeListIndex(lists,index);
            return lists[listIndex].get(index - MergedList.computeListOffset(lists, listIndex));
        }

        @Override
        public E set(int index, E element) {
            int listIndex = MergedList.computeListIndex(lists,index);
            return lists[listIndex].set(index - MergedList.computeListOffset(lists, listIndex),element);
        }

        @Override
        public void add(int index, E element) {
            int listIndex = MergedList.computeListIndex(lists,index);
            lists[listIndex].add(index - MergedList.computeListOffset(lists, listIndex),element);
        }

        @Override
        public E remove(int index) {
            int listIndex = MergedList.computeListIndex(lists,index);
            return lists[listIndex].remove(index - MergedList.computeListOffset(lists, listIndex));
        }

        @Override
        public int indexOf(Object o) {
            for (int i = 0; i < size(); i++) if(Objects.equals(o,get(i))) return i;
            return -1;
        }

        @Override
        public int lastIndexOf(Object o) {
            for (int i = size() - 1; i >= 0 ; i--) if(Objects.equals(o,get(i))) return i;
            return -1;
        }
    }
}

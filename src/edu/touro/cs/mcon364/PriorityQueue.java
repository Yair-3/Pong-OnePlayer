package edu.touro.cs.mcon364;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;


public class PriorityQueue<E> implements Queue<E>, Serializable {
    transient private int size;
    transient private List<E> backingStore = new LinkedList<>();
    private final Comparator<? super E> currentComp;


    public PriorityQueue() {
        this((Comparator<? super E>) Comparator.naturalOrder());
    }

    public PriorityQueue(Comparator<? super E> comparator) {
        this.currentComp = comparator;
    }

    @Override
    public boolean add(E e) {
        if (backingStore.isEmpty()) {
            backingStore.add(e);
            size++;
            return true;
        }
        int i = 0;
        while (currentComp.compare(e, backingStore.get(i)) >= 0) {
            if (currentComp.compare(e, backingStore.get(i)) == 0) {
                backingStore.add(backingStore.indexOf(backingStore.get(i)), e);
                size++;
                return true;
            }
            i++;
            if (i == backingStore.size()) {
                backingStore.add(backingStore.size(), e);
                size++;
                return true;
            }
        }
        backingStore.add(backingStore.indexOf(backingStore.get(i)), e);
        size++;
        return true;
    }

    @Override
    public boolean offer(E e) {
        add(e);
        return true;
    }

    @Override
    public E remove() {
        if (backingStore.isEmpty()) {
            throw new NoSuchElementException();
        }
        E returned = backingStore.get(0);
        backingStore.remove(backingStore.get(0));
        size--;
        return returned;
    }

    @Override
    public E poll() {
        if (backingStore.isEmpty()) {
            return null;
        }
        return remove();
    }

    @Override
    public E element() {
        if (backingStore.isEmpty()) {
            throw new NoSuchElementException();
        }
        return backingStore.get(0);
    }

    @Override
    public E peek() {
        if (backingStore.isEmpty()) {
            return null;
        }
        return backingStore.get(0);
    }

    @Override
    public int size() {
        return size;
    }

    public E get(int index){
       return backingStore.get(index);
    }


    @Serial
    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
            s.defaultWriteObject();
            s.writeInt(size);
            for (int i = 0; i < size; i++) {
                s.writeObject(backingStore.get(i));
            }


    }
    @Serial
    private void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {
            s.defaultReadObject();

            size =  s.readInt();
            backingStore = new LinkedList<>();
            if (size > 0){
                for (int i = 0; i < size; i++){
                    backingStore.add((E) s.readObject());
                }
            }
    }

    public void setSize(int s){
        this.size = s;
    }

    public void iterate(int amount){
        for (int i = 0; i < 6; i++){
            backingStore.get(i);
        }
    }
    @Override
    public Iterator<E> iterator() {
        return backingStore.listIterator();
    }

    @Override
    public Spliterator<E> spliterator() {
        return backingStore.spliterator();
    }
    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }


    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }
}

package com.appmodel.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LoopList<T> {
    private CopyOnWriteArrayList<T> lst;
    int mCapacity;
    int mFirst;
    int mLast;
    int mSize;

    public LoopList(List<T> list, int size) {
        lst = new CopyOnWriteArrayList<T>();
        if (list != null) {
            lst.addAll(list);
        }
        else {
            for (int i = 0; i < size; i++) {
                lst.add((T) null);
            }
        }
        reset(size);
    }

    public T getAt(int pos) {
        int i = index(pos);
        return i == -1 ? null : lst.get(i);
    }

    public T setAt(int pos, T x) {
        int i = index(pos);
        return i == -1 ? null : lst.set(i, x);
    }

    public synchronized int index(int pos) {
        return pos < mSize ? (mFirst + pos) % mCapacity : -1;
    }

    public boolean append(T x) {
        if (full()) return false;

        lst.set(increase(), x);
        return true;
    }

    private synchronized int increase() {
        int last = mLast;
        mLast = (mLast + 1) % mCapacity;
        if (!full()) {
            mSize++;
        }
        else {
            mFirst = (mFirst + 1) % mCapacity;
        }
        return last;
    }

    private synchronized int decrease() {
        int first = mFirst;
        mFirst = (mFirst + 1) % mCapacity;
        mSize--;
        return first;
    }

    public T expand() {
        return lst.get(increase());
    }

    /**
     * The difference between append and push is that:
     *   append give up if full;
     *   push will pop the head if full.
     **/
    public void push(T x) {
        if (full()) pop();
        append(x);
    }

    public T front() { return empty() ? null : getAt(0); }

    public T pop() {
        if (empty()) return null;

        return lst.set(decrease(), null);
    }

    public T shrink() {
        if(empty()) return null;

        return lst.get(decrease());
    }

    public synchronized int freeSize() { return mCapacity - mSize; }

    public synchronized int size() { return mSize; }

    public synchronized boolean full() { return mSize == mCapacity; }

    public synchronized int capacity() { return mCapacity; }

    public synchronized boolean empty() { return mSize == 0; }

    public synchronized int first() { return mFirst; }

    public synchronized void reset() {
        reset(mCapacity);
    }

    synchronized public void reset(int cap) {
        mCapacity = cap;
        mFirst = mLast = mSize = 0;
        for (int i = 0; i < lst.size(); i++) {
            lst.set(i, (T) null);
        }
    }

    public List<T> toList() {
        List<T> xs = new ArrayList<T>();
        for (int i = 0; i < mSize; ++i)
            xs.add(getAt(i));
        return xs;
    }
}

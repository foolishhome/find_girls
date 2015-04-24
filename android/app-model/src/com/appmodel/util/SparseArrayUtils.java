package com.appmodel.util;

import android.util.SparseArray;
import android.util.SparseIntArray;
import java.util.ArrayList;
import java.util.List;

public class SparseArrayUtils {

    private static final List<Integer> EMPTY_LIST = new ArrayList<Integer>();

    public static List<Integer> mutableKeys(SparseArray<?> sa) {
        if (FP.empty(sa)) {
            return new ArrayList<Integer>();
        }

        return keysOf(sa);
    }

    public static List<Integer> mutableKeys(SparseIntArray sa) {
        if (FP.empty(sa)) {
            return new ArrayList<Integer>();
        }

        return keysOf(sa);
    }

    public static List<Integer> readOnlyKeys(SparseArray<?> sa) {
        if (FP.empty(sa)) {
            return EMPTY_LIST;
        }

        return keysOf(sa);
    }

    public static List<Integer> readOnlyKeys(SparseIntArray sa) {
        if (FP.empty(sa)) {
            return EMPTY_LIST;
        }

        return keysOf(sa);
    }

    public static <T> List<T> valuesOf(SparseArray<T> sa) {
        if (FP.empty(sa)) {
            return new ArrayList<T>();
        }

        final int size = sa.size();
        ArrayList<T> ret = new ArrayList<T>();

        for (int i = 0; i < size; ++i) {
            ret.add(sa.valueAt(i));
        }

        return ret;
    }

    public static List<Integer> valuesOf(SparseIntArray sa) {
        if (FP.empty(sa)) {
            return new ArrayList<Integer>();
        }

        final int size = sa.size();
        ArrayList<Integer> ret = new ArrayList<Integer>();

        for (int i = 0; i < size; ++i) {
            ret.add(sa.valueAt(i));
        }

        return ret;
    }

    private static List<Integer> keysOf(SparseArray<?> sa) {
        final int size = sa.size();
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < size; ++i) {
            ret.add(sa.keyAt(i));
        }
        return ret;
    }

    private static List<Integer> keysOf(SparseIntArray sa) {
        final int size = sa.size();
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < size; ++i) {
            ret.add(sa.keyAt(i));
        }
        return ret;
    }

    /**
     * extract sub SparseArray.
     *
     * @param sa                Source SparseArray to extract from.
     * @param keys              The keys whose values to be extracted.
     * @param filterEmptyValues If this is true, won't put the null values(i.e. no
     *                          correlated value of key in sa) into returned SparseArray.
     *                          Otherwise, will put the null value into returned SparseArray.
     * @return The sub set of input SparseArray sa, null if input keys is null.
     */
    public static <T> SparseArray<T> sub(SparseArray<T> sa, int[] keys, boolean filterEmptyValues) {
        if (FP.empty(keys)) {
            return null;
        }

        SparseArray<T> ret = new SparseArray<T>();
        for (int e : keys) {
            final T value = sa.get(e);
            if (value != null || !filterEmptyValues) {
                ret.put(e, value);
            }
        }

        return ret;
    }

    /**
     * extract sub SparseArray.
     *
     * @param sa                Source SparseArray to extract from.
     * @param keys              The keys whose values to be extracted.
     * @param filterEmptyValues If this is true, won't put the null values(i.e. no
     *                          correlated value of key in sa) into returned SparseArray.
     *                          Otherwise, will put the null value into returned SparseArray.
     * @return The sub set of input SparseArray sa, null if input keys is null.
     */
    public static <T> SparseArray<T> sub(SparseArray<T> sa, List<Integer> keys, boolean filterEmptyValues) {
        if (FP.empty(keys)) {
            return null;
        }

        SparseArray<T> ret = new SparseArray<T>();
        for (int e : keys) {
            final T value = sa.get(e);
            if (value != null || !filterEmptyValues) {
                ret.put(e, value);
            }
        }

        return ret;
    }

    public static long[] LongListTolongArray(List<Long> lg) {
        long[] l = new long[lg.size()];
        for (int i = 0; i < lg.size(); i++) {
            l[i] = lg.get(i).longValue();
        }
        return l;
    }

}

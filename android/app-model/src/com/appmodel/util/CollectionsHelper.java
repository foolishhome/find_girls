package com.appmodel.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CollectionsHelper {

    public static boolean isNullOrEmpty(Collection<?> list) {
        return list == null || list.size() == 0;
    }

    public static <T> boolean isNullOrEmpty(T... array) {
        return array == null || array.length == 0;
    }

    public static boolean isNullOrEmpty(int... args) {
        return args == null || args.length == 0;
    }

    public static boolean hasData(Collection<?> list) {
        return !isNullOrEmpty(list);
    }

    public static <T> boolean hasData(T... array) {
        return !isNullOrEmpty(array);
    }

    /**
     * Clone the input list.
     *
     * @param src Source list, can be null.
     * @return The cloned input list. NOTE the elements are not cloned, just
     * references of the originals. Empty list returned when input is
     * null.
     */
    public static <T> List<T> cloneList(List<T> src) {
        List<T> ret = new ArrayList<T>();
        if (src == null) {
            return ret;
        }

        for (T e : src) {
            ret.add(e);
        }
        return ret;
    }

    public static <T> List<T> getUniques(List<T> list) {
        Set<T> set = new HashSet<T>();
        List<T> newList = new ArrayList<T>();
        for (Iterator<T> iter = list.iterator(); iter.hasNext(); ) {
            T element = iter.next();
            if (set.add(element)) {
                newList.add(element);
            }
        }
        return newList;
    }

    public static <T> T firstValid(List<T> list, T defaultValue) {
        if (isNullOrEmpty(list)) {
            return defaultValue;
        }
        for (T e : list) {
            if (e != null) {
                return e;
            }
        }
        return defaultValue;
    }

    public static <T> T firstValue(T defaultValue, T... args) {
        if (isNullOrEmpty(args)) {
            return defaultValue;
        }
        return args[0];
    }

    public static int firstValue(int defaultValue, int... args) {
        if (isNullOrEmpty(args)) {
            return defaultValue;
        }
        return args[0];
    }

    /**
     * Two null or empty list will be regarded as equal.
     *
     * @param l1         A list, can be null or empty.
     * @param l2         A list, can be null or empty.
     * @param comparator return 0 if equals, others for not equals. If it is null,
     *                   equals of T will be used. Two null elements are regarded equal
     *                   when equals of T be used.
     * @return True if all members are equal in the list order, false otherwise.
     */
    public static <T> boolean equal(List<T> l1, List<T> l2, Comparator<T> comparator) {
        if (isNullOrEmpty(l1)) {
            return isNullOrEmpty(l2);
        }

        if (isNullOrEmpty(l2)) {
            return isNullOrEmpty(l1);
        }

        final int n1 = l1.size();
        final int n2 = l2.size();
        if (n1 != n2) {
            return false;
        }

        if (comparator == null) {
            return equlWithoutComparator(l1, l2);
        } else {
            return equlWithComparator(l1, l2, comparator);
        }
    }

    private static <T> boolean equlWithoutComparator(List<T> l1, List<T> l2) {
        final int n1 = l1.size();
        for (int i = 0; i < n1; i++) {
            T e1 = l1.get(i);
            T e2 = l2.get(i);
            if (e1 == null) {
                if (e2 != null) {
                    return false;
                }
            } else if (!e1.equals(e2)) {
                return false;
            }
        }
        return true;
    }

    private static <T> boolean equlWithComparator(List<T> l1, List<T> l2, Comparator<T> comparator) {
        final int n1 = l1.size();
        for (int i = 0; i < n1; i++) {
            T e1 = l1.get(i);
            T e2 = l2.get(i);

            if (comparator.compare(e1, e2) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tell whether array has an element who points to dest.
     */
    public static <T> boolean containsRef(T[] array, T dest) {
        if (isNullOrEmpty(array)) {
            return false;
        }
        for (T e : array) {
            if (e == dest) {
                return true;
            }
        }
        return false;
    }

}

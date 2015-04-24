package com.appmodel.util;

import java.util.List;

public class GroupedList {

    public static int size(List<? extends Object> list, int columns) {
        if (list == null) {
            return 0;
        } else {
            return (int) Math.ceil(1d * list.size() / columns);
        }
    }

    public static Object get(List list, int columns, int row, int column) {
        int index = row * columns + column;
        if (list.size() > index) {
            return list.get(index);
        } else {
            return null;
        }
    }
}

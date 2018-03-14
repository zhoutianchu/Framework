package com.zhoutianchu.framework.utils;

import java.util.List;

/**
 * 数组辅助类
 * Created by zhout on 2018/3/14.
 */

public class ArraysUtil {
    private ArraysUtil() {

    }

    public static int[] list_to_int_arrays(List<Integer> list) {
        if (list == null)
            return null;
        int[] arrays = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arrays[i] = list.get(i);
        }
        return arrays;
    }
}

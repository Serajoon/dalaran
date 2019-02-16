package com.serajoon.dalaran.common.util;

import java.util.List;
import java.util.stream.Collectors;

public abstract class MyCollectionUtils {

    /**
     * @param list1 List
     * @param list2 List
     * @return list1-list2的差集
     * @author hanmeng1
     * @since 2019/2/16 13:51
     */
    public static List<String> diff(List<String> list1, List<String> list2) {
        return list1.stream().filter(item -> !list2.contains(item)).collect(Collectors.toList());
    }
}

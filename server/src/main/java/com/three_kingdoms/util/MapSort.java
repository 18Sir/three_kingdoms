package com.three_kingdoms.util;

import java.util.*;

public class MapSort {
    //将map按字符串降序排序
    public static Map<String,Object> sortMapByDesc(Map<String,Object> map){
        if(map == null){
            return map;
        }
        List<Map.Entry<String,Object>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Object>>() {
            @Override
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                if(o1.getKey().compareTo(o2.getKey()) == 1){
                    return -1;
                }
                return 0;
            }
        });
        Map<String,Object> sortMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : list) {
            sortMap.put(entry.getKey(),entry.getValue());
        }
        System.out.println(sortMap);
        return sortMap;
    }
}

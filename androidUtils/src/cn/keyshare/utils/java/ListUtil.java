package cn.keyshare.utils.java;

import java.util.List;

public class ListUtil {

    public static <V> boolean isEmpty(List<V> sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }
}

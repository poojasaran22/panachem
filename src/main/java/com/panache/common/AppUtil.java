package com.panache.common;


import java.util.Map;

public class AppUtil {

    public static String [] toArray(String ...input){
        return input;
    }

    public static boolean isEmpty(String str){
        if(str==null || "".equals(str))
            return true;
        return false;
    }

    public static <K,V,T> T lookup(Map<K,V> map, String key, Class<T> classT){
        V v = map.get(key);
        if(v==null)throw new RuntimeException(key +" is null or empty");
        if(!v.getClass().isAssignableFrom(classT))throw new
                RuntimeException(key +" can not be cast to  "+classT);
        return classT.cast(v);
    }
}

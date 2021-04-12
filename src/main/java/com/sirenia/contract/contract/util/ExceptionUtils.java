package com.sirenia.contract.contract.util;

public class ExceptionUtils {
    public static void wrapWithRuntimeEx(Callbacks.Callback00e cb){
        try {
            cb.apply();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    public static <T> T wrapWithRuntimeEx(Callbacks.Callback01e<T> cb){
        try {
            return cb.apply();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}

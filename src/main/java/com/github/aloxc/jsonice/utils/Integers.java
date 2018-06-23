package com.github.aloxc.jsonice.utils;

public final class Integers {

    public static int [] intValues(Integer [] params) {
        int len = params.length;
        int [] temp = new int [len];
        for (int i = 0; i < len; i ++) {
            temp[i] = params[i];
        }
        return temp;
    }
}

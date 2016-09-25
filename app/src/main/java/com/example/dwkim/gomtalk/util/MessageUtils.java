package com.example.dwkim.gomtalk.util;

import com.example.dwkim.gomtalk.data.DBUtil;

/**
 * Created by dwkim on 2015-06-20.
 */
public class MessageUtils {

    public static String join(String[] stringArray, String delimeter) {
        boolean isFirst = true;
        StringBuilder sb = new StringBuilder();

        for(String str : stringArray) {
            if(!isFirst) {
                sb.append(delimeter);
            }

            isFirst = false;

            sb.append(str);
        }

        return sb.toString();
    }
}

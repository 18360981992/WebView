package com.dabao.demo.utils;

import android.util.Log;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * creat by wd 2018/11/27
 *
 * 请求接口加密
 *
 */
public class Md5RequestUtils {

    public static Map<String,Object> compartorList(Map<String,Object> map,String urlName){
        //排序
        List<Map.Entry<String,Object>> list = new ArrayList<Map.Entry<String,Object>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Object>>() {
            @Override
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                return o2.getKey().compareTo(o1.getKey());//降序排    01-02正序

            }
        });

        Map<String,Object> mapSign =new HashMap<>();
        String str="";
        String str2="";
        //遍历map

        for(Map.Entry<String,Object> next:list){
            str+=next.getKey()+next.getValue();
            mapSign.put(next.getKey(),next.getValue());
        }

        str2=str+"jxs"+urlName;
        Log.d("Md5RequestUtils",str2);
        //转码
        String encode = URLEncoder.encode(str2);
        Log.d("Md5RequestUtils",URLEncoder.encode(str2));
        //加密
        String s = parseStrToMd5L32(encode);
        Log.d("Md5RequestUtils",s);
        //截取字符串
        String substring = s.substring(15, 21);
        //sign放进map
        mapSign.put("sig",substring);

        return mapSign;

    }

    /*
    *
    * MD5加密
    * */
    public static String parseStrToMd5L32(String str) {
        String reStr = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bytes) {
                int bt = b & 0xff;
                if (bt < 16) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(bt));
            }
            reStr = stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return reStr.toString();
    }
}

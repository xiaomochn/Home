package com.lakala.platform.weex.extend.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaofeng on 17/7/18.
 */

public class DataFormatUtil {

    /**
     * 递归把 JSONObject 转换成 Map
     */
    public static Map<String, Object> jsonToMap(JSONObject obj){
        Map<String, Object> result = new HashMap();
        if(obj == null){
            return result;
        }else{
            Iterator<String> it = obj.keys();
            String key;
            Object value;
            while(it.hasNext()){
                key = it.next();
                value = obj.opt(key);
                if(value instanceof JSONArray){
                    List list = new ArrayList();
                    for(int i = 0; i < ((JSONArray) value).length(); i++){
                        list.add(jsonToMap(((JSONArray) value).optJSONObject(i)));
                    }
                    value = list;
                }else if(value instanceof JSONObject){
                    value = jsonToMap((JSONObject) value);
                }

                //做一个过滤，剔除值为null的数据项。如果包含null,就是此时获取到的数据类型就是  org.json.JSONObject$1
                String className = value.getClass().getName();
                if("org.json.JSONObject$1".equals(className)){
                    result.put(key, "");
                    continue;
                }

                result.put(key, value);
            }
            return result;
        }
    }

    public static ArrayList<Map<String,Object>> jsonArrayToList(JSONArray arr){
        ArrayList<Map<String,Object>> res = new ArrayList<Map<String, Object>>();
        if(arr == null){
            return res;
        }else{
            for(int i = 0; i < arr.length(); i++){
                JSONObject item = arr.optJSONObject(i);
                Map<String, Object> itemData = jsonToMap(item);
                res.add(itemData);
            }
            return res;
        }
    }

}

package com.interview.lib;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mauricio Lomeli
 * @version Feburary, 2020
 *
 * This program transfers JSON String data into a Json Object.
 */

public class Json {
    public JSONObject json;
    public enum TYPECASTED{LIST, MAP, HASHMAP, STRING, INTEGER, DOUBLE};

    public static JSONObject fromObject(Object object){
        TYPECASTED typecasted = typeCasted(object);
        switch (typecasted){
            case MAP:
                return fromMap((Map) object);
            case LIST:
                return fromList((List) object);
            case HASHMAP:
                return fromHashMap((HashMap) object);
            default:
                break;
        }
        return null;
    }

    public static JSONObject fromMap(Map map){
        return new JSONObject(map);
    }

    public static JSONObject fromHashMap(HashMap<String, Object> map){
        return new JSONObject((Map) map);
    }

    public static JSONObject fromList(List list){
        List<Object> objectList = new ArrayList<>();

        for(Object obj : objectList)
        {
            if (obj instanceof Map)
                objectList.add(fromMap((Map) obj));
            else if (obj instanceof HashMap)
                objectList.add(fromHashMap((HashMap) obj));
            else if (obj instanceof List)
                objectList.add(fromList((List) obj));
            else
                objectList.add(obj);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", new JSONArray(objectList));
        return jsonObject;
    }

    public static TYPECASTED typeCasted(Object jsonObject){
        Object obj = null;
        try{ obj = (Map) jsonObject; return TYPECASTED.MAP; } catch (Exception e){ }
        try{ obj = (HashMap) jsonObject; return TYPECASTED.HASHMAP; } catch (Exception e){ }
        try{ obj = (List) jsonObject; return TYPECASTED.LIST; } catch (Exception e){ }
        try{ obj = (Integer) jsonObject; return TYPECASTED.INTEGER; } catch (Exception e){ }
        try{ obj = (Double) jsonObject; return TYPECASTED.DOUBLE; } catch (Exception e){ }
        try{ obj = (String) jsonObject; return TYPECASTED.STRING; } catch (Exception e){ }

        return null;
    }

}

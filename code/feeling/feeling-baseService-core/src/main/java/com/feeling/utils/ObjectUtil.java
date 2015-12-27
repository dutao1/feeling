package com.feeling.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.log4j.Logger;


public class ObjectUtil {
    
    static Logger  log = Logger.getLogger(ObjectUtil.class);
    public static HashMap<String, String> voToMap(Object object) {
        if (object == null) return null;
        HashMap<String, String> returnMap = new HashMap<String, String>();
        getValsByObject(object.getClass(),object,returnMap);
        getValsByObject(object.getClass().getSuperclass(),object,returnMap);
        return returnMap;
    }
    /**
     * 对象深拷贝
     * 
     * @param oldObj
     * @return
     */
    public static Object objectCopy(Object oldObj) {  
        Object obj = null;  
        try {  
            // 将对象输出为byte
            ByteArrayOutputStream bos = new ByteArrayOutputStream();  
            ObjectOutputStream out = new ObjectOutputStream(bos);  
            out.writeObject(oldObj);  
            out.flush();  
            out.close();  
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());   
            ObjectInputStream in = new ObjectInputStream(bis);  
            obj = in.readObject();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return obj;  
    }  
    public  static void getValsByObject(Class<?> clazz,Object o,HashMap<String, String> returnMap){
        if(returnMap==null){
            returnMap = new HashMap<String, String>();
        }
        if(clazz==null||o==null){
            return ;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals("serialVersionUID")) {
                continue;
            }
            Object value = null;
            field.setAccessible(true);
            try {
                value = field.get(o);
                if(value==null){
                    continue;
                }
            } catch (Exception e) {
                log.error("ObjectUtil.voToMap get field value error", e);
            }
            returnMap.put(field.getName(),  value.toString());
        }
        
    }
}

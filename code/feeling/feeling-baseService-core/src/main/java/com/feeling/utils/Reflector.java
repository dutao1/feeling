package com.feeling.utils;
/**
 * Reflector.java
 * 创建:DUTAO 2010-4-29
 */


import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public  class Reflector {
	/**
	 * 获取指定属性的值，可忽略大小写，也可忽略是否具有get方法，
	 * 但是推荐使用和原属性大小写一致且原属性的命名合乎java一般编程规范，且具有get方法， 这样能获得更好的性能。
	 * 
	 * @param invokeObj
	 *            将调用的方法所属对象
	 * @param attr
	 *            需要调用get方法的属性
	 * @return 返回对应get方法得到的对象
	 * @throws ReflectException
	 *             若不存在对应属性或发生异常将抛出此异常
	 */
	public static Object getInvoke(Object invokeObj, String attr,boolean isBoolean) throws Exception {
		String methodName = getBeanMethodName("get", attr);
		if(isBoolean){
			methodName = getBeanMethodName("is", attr);
		}
		try {
			Method method = invokeObj.getClass().getMethod(methodName);
			method.setAccessible(true);
			return method.invoke(invokeObj);
		} catch (Exception ex) {
			try {
				// 若名字大小写不对或方法找不到则查找合适的名字再做
				Map<String, Field> attrNamesMap = getFieldNames(invokeObj);
				if (attrNamesMap.containsKey(attr.toUpperCase())) {
					Field f = attrNamesMap.get(attr.toUpperCase());
					// String realAttrName = f.getName();
					f.setAccessible(true);
					try {
						return f.get(invokeObj);
					} catch (Exception e) {
						throw new Exception("动态调用get方法：" + methodName + "失败!"
								+ e.getMessage());
					}
				} else {
					throw new Exception("动态调用get方法：" + methodName + "失败!"
							+ ex.getMessage());
				}
			} catch (Exception exx) {
				throw new Exception("动态调用get方法：" + methodName + "失败!"
						+ exx.getMessage());
			}
		}

	}
    public static Object invokeMethod(Object invokeObj,String methodName,Object... setParams){
    	Object b = null ;
    	Method method;
		try {
			if(setParams!=null&&setParams.length>0){
				Class clazz[] = new Class[setParams.length];
				for(int i=0;i<setParams.length;i++){
					Class clazzOne = setParams[i].getClass();
					Class clazzPrimitive = getPrimitiveClass(clazzOne);
					if(clazzPrimitive==null){
						clazzPrimitive = clazzOne;
					}
					clazz[i]=clazzPrimitive;
				}
				method = invokeObj.getClass().getMethod(methodName,clazz);
				method.setAccessible(true);
				b = method.invoke(invokeObj,setParams);
			}else{
				method = invokeObj.getClass().getMethod(methodName);
				method.setAccessible(true);
				b= method.invoke(invokeObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return b ;
    }
	// 获取所有的属性名的map<大写名称,实际名称>
	@SuppressWarnings("unchecked")
	public static TreeMap<String, Field> getFieldNames(Object invokeObj) {

		TreeMap<String, Field> fieldNamesMap = new TreeMap<String, Field>();
		Class classOfObj = invokeObj.getClass();
		Field[] fields = null;
		while (classOfObj != null) {
			fields = classOfObj.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				String fname =	fields[i].getName().toUpperCase();
				if (!fname.equals("THIS$0") &&!fname.equals("SERIALVERSIONUID"))
					fieldNamesMap.put(fields[i].getName(),
							fields[i]);
			}
			classOfObj = classOfObj.getSuperclass();
		}
		return fieldNamesMap;
	}
	
	/**
	 * 动态设置指定属性的值，可忽略大小写，也可忽略该属性是否有set方法。
	 * 但是推荐使用和原属性大小写一致且原属性的命名合乎java一般编程规范，且具有set方法， 这样能获得更好的性能。
	 * 
	 * @param invokeObj
	 *            将调用的方法所属对象
	 * @param attr
	 *            需要调用set方法的属性名
	 * @param setValue
	 *            需要设置的值
	 * @throws ReflectException
	 *             ReflectException 若不存在对应属性或发生异常将抛出此异常
	 */
	public static void setInvoke(Object invokeObj, String attr, Object setValue)
			throws Exception {
		String methodName = getBeanMethodName("set", attr);
		Class  clazz =  getFieldType(invokeObj, attr);
		if(clazz!=null){
			setValue = getWrapperValue(clazz, setValue);
			if(setValue==null){
				return ;
			}
		}
		try {
			Method method = invokeObj.getClass().getMethod(methodName,
					setValue.getClass());
			method.setAccessible(true);
			method.invoke(invokeObj, setValue);
		} catch (Exception ex) {
			try {
				Method method = invokeObj.getClass().getMethod(methodName,
						getPrimitiveClass(setValue));
				method.invoke(invokeObj, setValue);
			} catch (Exception ex2) {
				try {
					// 若名字大小写不对或方法找不到则查找合适的名字再做
					Map<String, Field> attrNamesMap = getFieldNames(invokeObj);
					if (attrNamesMap.containsKey(attr.toUpperCase())) {

						Field f = attrNamesMap.get(attr.toUpperCase());
						// String realAttrName = f.getName();
						f.setAccessible(true);
						try {
							f.set(invokeObj, setValue);
						} catch (Exception e) {
							throw new Exception("动态调用set方法：" + methodName
									+ "失败!" + e.getMessage());
						}
					} else {
						throw new Exception("动态调用set方法：" + methodName + "失败!"
								+ ex2.getMessage());
					}
				} catch (Exception e3) {
					throw new Exception("动态调用set方法：" + methodName + "失败!"
							+ e3.getMessage());
				}

			}
		}
	}

	/**
	 * 获取指定名称的属性的类型
	 * @param <S>
	 * 
	 * @param invokeObj
	 *            对应的对象
	 * @param fieldName
	 *            属性名
	 * @return 属性值
	 * @throws Exception
	 *             若不存在该属性或获取时发生异常则抛出
	 */
	@SuppressWarnings("unchecked")
	public static  Class   getFieldType(Object invokeObj, String fieldName)
			throws Exception {
		Map<String, Field> attrNamesMap = getFieldNames(invokeObj);
		Field ff =  attrNamesMap.get(fieldName);
		 if(ff!=null){
			 return ff.getType();
		 }else{
			 throw new Exception("动态调用get方法：" + fieldName + "失败!");
		 }
	/*	if (attrNamesMap.containsKey(fieldName.toUpperCase())) {
			Field f = attrNamesMap.get(fieldName.toUpperCase());
			return f.getType();
		} else {
			throw new Exception("动态调用get方法：" + fieldName + "失败!");
		}*/
	}
	/**
	 * 获取是否泛型
	 * @param ff  Field
	 * @return Class
	 */
	private static   Class getFtype(Field ff){
		if(ff==null){
			return null;
		}
		Type fc = ff.getGenericType();
		//如果是泛型参数的类型  
		 if(fc!=null &&fc instanceof ParameterizedType){
			 ParameterizedType pt = (ParameterizedType) fc;  
			 Class  type_f =   (Class)pt.getActualTypeArguments()[0]; //  得到泛型里的class类型对象。 
			 return  type_f;
		 } 
		 return null ;
	}
	/**
	 * 利用反射进行类型转换，仅支持基本类型和字符串之间的转换,不支持基本类型之间的转换；
	 * 一般和 {@link #getFieldType(Object, String)}结合起来使用
	 * @param value 待转换值
	 * @param type 要转换成的结果类型
	 * @return 转换后的值
	 * @throws Exception 若转换失败则抛出异常
	 */
	@SuppressWarnings("unchecked")
	public static Object formatValue(Object value, Class type) throws Exception {
		boolean candoType = false;
		boolean candoValue = false;
		// 目的类型是否是字符串或基本类型及其包装类
		if (type.isPrimitive()) {
			candoType = true;
		} else if (type.equals(String.class)) {
			candoType = true;
		} else {
			if (type.equals(Integer.class) || type.equals(Short.class)
					|| type.equals(Long.class) || type.equals(Double.class)
					|| type.equals(Float.class) || type.equals(Boolean.class)
					|| type.equals(Byte.class) || type.equals(Character.class)) {
				candoType = true;
			}
		}
		// 源值类型是否是字符串或基本类型及其包装类
		if (value.getClass().isPrimitive()) {
			candoValue = true;
		} else if (value.getClass().equals(String.class)) {
			candoValue = true;
		} else {
			if (getPrimitiveClass(value) != null) {
				candoValue = true;
			}
		}
		if (candoValue && candoType) {
			Class tmpType = type;//tmpType均为转化后的包装类
			if (type.isPrimitive()) {
				tmpType = getWrapperClass(type);
			}
			//将空串转换为基本类型则返回null
			if(getPrimitiveClass(type)!=null&&value.getClass().equals(String.class)&&value.toString().trim().length()==0){
				return null;
			}
			if (value.getClass().equals(tmpType)) {// 类型一样则返回源值
				return value;
			} else {
				if (type.equals(String.class)) {// 转字符串则调用toString方法
					return value.toString();
				} else {
					if (value.getClass().equals(String.class)
							&& (type.equals(char.class) || type
									.equals(Character.class))) {
						if (value.toString().length() == 1) {
							return value.toString().charAt(0);
						} else {
							throw new Exception("不支持此类型的转换："
									+ value.getClass().getName() + "->"
									+ type.getName());
						}
					}
					
					// 对应的valueOf方法
					Method valueOfMethod = tmpType.getDeclaredMethod("valueOf",
							value.getClass());
					return valueOfMethod.invoke(tmpType, value.toString());
				}
			}
		} else {
			throw new Exception("不支持此类型的转换：" + value.getClass().getName()
					+ "->" + type.getName());
		}

		/*
		 * if(type.equals(Integer.class)||type.equals(int.class)){ return
		 * Integer.valueOf(value.toString()); }else{ }
		 */
	}

	private static String getBeanMethodName(String prefix, String attr) {
		String first = attr.substring(0, 1).toUpperCase();
		String rest = attr.substring(1);
		return prefix + first + rest;
	}

	@SuppressWarnings("unchecked")
	private static Class getPrimitiveClass(Object obj) {
		if (obj instanceof Integer) {
			return int.class;
		} else if (obj instanceof Short) {
			return short.class;
		} else if (obj instanceof Long) {
			return long.class;
		} else if (obj instanceof Double) {
			return double.class;
		} else if (obj instanceof Float) {
			return Float.class;
		} else if (obj instanceof Boolean) {
			return boolean.class;
		} else if (obj instanceof Byte) {
			return byte.class;
		} else if (obj instanceof Character) {
			return char.class;
		} else {
			return null;
		}
	}
	public static <S>  S setAllVals(HashMap hm,S s){
		try {
			Iterator i =  hm.keySet().iterator();
			while(i.hasNext()){
				String key = i.next().toString();
				Object val = (Object)hm.get(key);
				if(key.indexOf(".")>0){
					String subkey = key.split("[.]")[0];
					String subkey_ = key.split("[.]")[1];
					Object o = getInvoke(s, subkey, false);
					if(o==null){
						Class clazz =getFieldType(s, subkey);
						if(clazz!=null ){
							 o = clazz.newInstance();
						}
						// o =  Class.forName(clazz.getName());
						/*LinkedHashMap<String, Field> ll =  this.getFieldNames(s);
						 Field ff =  ll.get(subkey);
						 if(ff!=null){
							 Class clazz =ll.get(subkey).getType();
							 o =  Class.forName(clazz.getName());
						 }*/
					}
					setInvoke(o, subkey_, val);
					setInvoke(s, subkey, o);
				}else{
					setInvoke(s,key,val);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	/**
	 * 获取对象内的所有属性及对应属性值
	 * @param object  目标对象
	 * @return  HashMap<String ,String>   key,val
	 */
	@SuppressWarnings("unchecked")
	public static   TreeMap<String,Object> getAllVals(Object object,String...subkey){
		if(object==null){
			return null;
		}
		TreeMap<String,Object>  vals = new TreeMap();
		TreeMap<String, Field> ll =  getFieldNames(object);
		 if( ll!=null){
			Iterator<String> i =  ll.keySet().iterator();
			while(i.hasNext()){
				String key = i.next();
				try {
					Class clazz = ll.get(key).getType();
					String cname = clazz.toString();
					if( cname.indexOf("java.util.")>-1){
						Class fantype = getFtype(ll.get(key));
						if(fantype!=null&&fantype.toString().indexOf("com.feeling")>-1)
						continue;
					}
					
					Object o = getInvoke(object, key,(clazz.equals(boolean.class)));
					if(clazz.getComponentType()==null && cname.indexOf("com.feeling")>-1&&o!=null){
						TreeMap  vals_   = getAllVals(o,key);
						if(vals_!=null&&vals_.size()>0){
							vals.putAll(vals_);
						}
					}
					if(o!=null&& ( cname.indexOf("com.feeling")<0 )   ){
						if(clazz.getComponentType()!=null &&(clazz.getComponentType().getName().indexOf("com.feeling")<0)){
							StringBuilder  sbr = new StringBuilder();
							 for (int j = 0; j< Array.getLength(o); j++) {  
								 if(j==0){
									 sbr.append(Array.get(o, j) );
								 }else{
									 sbr.append(","+Array.get(o, j) );
								 }
						        } 
							 o = sbr.toString();
						}
						if(subkey!=null&&subkey.length>0){
							vals.put(subkey[0]+"."+key, o);
						}else{
							vals.put(key, o);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		 }
		return vals;
	}
	/**
	 * 获得类的泛型类
	 * @param c Class
	 * @return  Class
	 */
	public static Class getFanTypeObj(Class c){
		Type superClass =	 c.getGenericSuperclass();
		if (superClass instanceof Class<?>) {
			 return null;
		}else{
			Class clazz_f= (Class) ((ParameterizedType) superClass).getActualTypeArguments()[0];  
			try {
				return clazz_f;
			} catch (Exception e) {
				e.printStackTrace();
			}  
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	private static Class getPrimitiveClass(Class clax) {
		if (clax.equals(Integer.class)) {
			return int.class;
		} else if (clax.equals(Short.class)) {
			return short.class;
		} else if (clax.equals(Long.class)) {
			return long.class;
		} else if (clax.equals(Double.class)) {
			return double.class;
		} else if (clax.equals(Float.class)) {
			return Float.class;
		} else if (clax.equals(Boolean.class)) {
			return boolean.class;
		} else if (clax.equals(Byte.class)) {
			return byte.class;
		} else if (clax.equals(Character.class)) {
			return char.class;
		} else {
			return null;
		}
	}
	//获取基本类型的包装类
	@SuppressWarnings("unchecked")
	private static Class getWrapperClass(Class clax) {
		if (clax.equals(int.class)) {
			return Integer.class;
		} else if (clax.equals(short.class)) {
			return Short.class;
		} else if (clax.equals(long.class)) {
			return Long.class;
		} else if (clax.equals(double.class)) {
			return Double.class;
		} else if (clax.equals(float.class)) {
			return Float.class;
		} else if (clax.equals(byte.class)) {
			return Byte.class;
		} else if (clax.equals(char.class)) {
			return Character.class;
		} else if (clax.equals(boolean.class)) {
			return Boolean.class;
		} else {
			return null;
		}
	}
	
	public static    Object  getWrapperArray(Class clax_,Object val){
		if(clax_!=null){
			StringBuilder  sbr = new StringBuilder();
			String s[] = val.toString().split(",");
			Object newArray =  Array.newInstance(clax_, s.length);
			for(int i=0;i<s.length;i++){
				try{
					Array.set(newArray, i, getWrapperValue(clax_,s[i]));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return newArray;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static   Object getWrapperValue(Class clax,Object val ) throws Exception  {
		    Class  clax_ = clax.getComponentType();
		    if(clax_!=null){
		    	/*if (clax_.isPrimitive()) {
		    		return null;
		    		//clax_ = getWrapperClass(clax_);
				}*/
		    	return getWrapperArray(clax_,val);
		    } 
			if(clax.getName().equals("boolean")||clax.getName().equals("java.lang.Boolean")){
		 		val = Boolean.valueOf(val.toString().toLowerCase());
		 	 } 
			else if(clax.getName().equals("int")||clax.getName().equals("java.lang.Integer") ){
		 		val = Integer.valueOf(val.toString());
		 	 }
			else if(clax.getName().equals("long")||clax.getName().equals("java.lang.Long")){
		 		val = Long.valueOf(val.toString());
		 	}
			else if(clax.getName().equals("short")||clax.getName().equals("java.lang.Short")){
		 		val =Short.valueOf(val.toString());
		 	}
			else if(clax.getName().equals("double")||clax.getName().equals("java.lang.Double")){
		 		val = Double.valueOf(val.toString());
		 	}
			else if(clax.getName().equals("byte")||clax.getName().equals("java.lang.Byte")){
		 		val = Byte.valueOf(val.toString());
		 	}
			else if(clax.getName().equals("char")||clax.getName().equals("java.lang.Character")){
		 		val = Long.valueOf(val.toString());
		 	}
			else if(clax.getName().equals("java.util.Date")){
		 		val =DateUtil.parse(val.toString(), "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		 	}else{
		 		if(val.getClass().getName().indexOf("JSONArray")>0){
		 			/*JsonArray  ja =(JsonArray) val;
		 			if(isInterface(clax,"java.util.List")){
		 				val = JsonArray.toList(ja);
		 			}
					if(isInterface(clax,"java.util.Set")){
						Set s = (Set) clax.newInstance();		
						List list = JsonArray.toList(ja);
						for(Object i:list){
							s.add(i);
						}
						val = s;
					 }
		 			return val;*/
		 		}
		 		/*if(val.getClass().getName().indexOf("JSONObject")>0){
		 			JsonObject  ja =(JsonObject) val;
					if(isInterface(clax,"java.util.Map")){
						 Map m = (Map) clax.newInstance();
						 for(Iterator i = ja.entrySet().iterator(); i.hasNext();){
							 Object key = i.next();
							 m.put(key, ja.get(key.toString()));
						 }
						 val = m;
					 }
		 			return val;
		 		}*/
		 	}
		 	return val;
	}
	public static boolean  isInterface(Class clax, String szInterface){
			Class[] cc = clax.getInterfaces();
			if(cc!=null){
				for(Class c:cc){
					if(c.getName().equals(szInterface))
					{
						return true;
					}
					else
					{
							Class[]face1=c.getInterfaces();
							for(int x=0;x<face1.length;x++)
							{
								if(face1[x].getName().equals(szInterface))
								{
									return true;
								}
								else if(isInterface(face1[x],szInterface))
								{
									return true;
								}
							}
						}
					  }
					if(null!=clax.getSuperclass())
					{
						return isInterface(clax.getSuperclass(),szInterface);
					}
					}
			return false;
	}
   
}

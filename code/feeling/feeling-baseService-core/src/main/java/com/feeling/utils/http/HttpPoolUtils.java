package com.feeling.utils.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.feeling.enums.HttpEnum;
import com.feeling.utils.ObjectUtil;
import com.feeling.utils.Reflector;


/**
 * @description:  http 连接池处理
 * @author dutao
 * @since dutao by 2015年5月22日
 */
public class HttpPoolUtils {
    
    
    private static Logger logger= Logger.getLogger(HttpPoolUtils.class);
    private static final Reflector reflector = new Reflector();
    private static final String UTF8 = "UTF-8";
    private static final String MAP_PACKAGE_NAME = "java.util.Map";
   // private static final String CONTENT_TYPE_TEXT = "text/xml";
    private static final String URL_PARAM_SPLIT = "[?]";
    private static final String Q_MARK = "?";
    private static final String EMPTY_STR = "";
    private static final String AND_MARK = "&";
    private static final String EQUAL_MARK = "=";
    /**URL的分隔符*/
    //private static String URL_PATH_SPLIT = "[/]";
    private static final String URL_PATH_MATCH = "\\{\\w+\\}";
    /**
     * {ADFAFDA}中的“{”
     */
    private static final String URL_PATH_LEFT = "{";
    /**
     * {ADFAFDA}中的“}”
     */
    private static final String URL_PATH_RIGHT = "}";
    private static final Pattern pattern = Pattern.compile(URL_PATH_MATCH, Pattern.CASE_INSENSITIVE); 
    /**
     * 处理 http delete 方法
     * @param params  可以是自定义对象或者map
     * @param url  请求url
     * @param headerMap 头信息map
     * @param charSet  可选，默认 utf8
     * @return HttpResult对象
     */
    public static  HttpResult doDelete(Object params,String url,Map<String, String> headerMap,String...charSet){
        
        if(StringUtils.isEmpty(url)){
            return null;
        }
        String charSetStr = null;
        if(charSet!=null&&charSet.length>0){
            charSetStr=charSet[0];
        }
        try {
            Map<String, String> mapParams = null;
            if(isMapClass(params)){
                mapParams = (Map<String, String>) params;
            }else{
                mapParams =   ObjectUtil.voToMap(params);
            }
            return httpMethodRequest(HttpEnum.DELETE.getValue(),url,mapParams, headerMap, charSetStr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  
        return null;
    }
    /**
     * 处理 http put 方法
     * @param params  可以是自定义对象或者map
     * @param url  请求url
     * @param headerMap 头信息map
     * @param charSet  可选，默认 utf8
     * @return HttpResult对象
     */
    public static  HttpResult doPut(Object params,String url,Map<String, String> headerMap,String...charSet){
        if(StringUtils.isEmpty(url)){
            return null;
        }
        String charSetStr = null;
        if(charSet!=null&&charSet.length>0){
            charSetStr=charSet[0];
        }
        Map<String, String> mapParams = null;
        if(isMapClass(params)){
            mapParams = (Map<String, String>) params;
        }else{
            mapParams =   ObjectUtil.voToMap(params);
        }
        try {
            return httpMethodRequest(HttpEnum.PUT.getValue(),url,mapParams, headerMap, charSetStr);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null;
    }
    
    /**
     * 处理 http get 方法
     * @param params  可以是自定义对象或者map
     * @param url  请求url
     * @param headerMap 头信息map
     * @param charSet  可选，默认 utf8
     * @return HttpResult对象
     */
    public static HttpResult doGet(Object params,String url,Map<String, String> headerMap,String...charSet){
        if(StringUtils.isEmpty(url)){
            return null;
        }
        String charSetStr = null;
        if(charSet!=null&&charSet.length>0){
            charSetStr=charSet[0];
        }
        Map<String, String> mapParams = null;
        if(isMapClass(params)){
            mapParams = (Map<String, String>) params;
        }else{
            mapParams =   ObjectUtil.voToMap(params);
        }
        try {
           return  httpMethodRequest(HttpEnum.GET.getValue(),url, mapParams, headerMap, charSetStr);
        } catch (Exception e) {
            e.printStackTrace();
        }  
        return null;
    }
    /**
     * 处理 http post 方法
     * @param params  可以是自定义对象或者map
     * @param url  请求url
     * @param headerMap 头信息map
     * @param charSet  可选，默认 utf8
     * @return HttpResult对象
     */
    public static HttpResult doPost(Object params,String url,Map<String, String> headerMap,String...charSet){
        if(StringUtils.isEmpty(url)){
            return null;
        }
        String charSetStr = null;
        if(charSet!=null&&charSet.length>0){
            charSetStr=charSet[0];
        }
        Map<String, String> mapParams = null;
        if(isMapClass(params)){
            mapParams = (Map<String, String>) params;
        }else{
            mapParams =   ObjectUtil.voToMap(params);
        }
        try {
            return  httpMethodRequest(HttpEnum.POST.getValue(),url, mapParams, headerMap, charSetStr);
        } catch (Exception e) {
            e.printStackTrace();
        }  
        return null;
    }
    /**
     * 替换url 的  pathvals
     * 如： users/{userid}/{type} 替换成 users/123/456
     * @param url
     * @param parametersMap
     * @return String 
     */
    public  static  String formatPathUrl(String url,Map<String, String> parametersMap){
        if(StringUtils.isEmpty(url)){
            return url;
        }
        if(parametersMap!=null&&!parametersMap.isEmpty()){
            Matcher matcher=pattern.matcher(url);
            while(matcher.find()){
                String findMatchStr = matcher.group();
                String findMatchStrNoSgin = matcher.group().replace(URL_PATH_LEFT, EMPTY_STR).replace(URL_PATH_RIGHT, EMPTY_STR);
                String vals = parametersMap.get(findMatchStrNoSgin);
                if(StringUtils.isNotEmpty(vals)){
                    url=url.replace(findMatchStr, vals);
                }
            }
        }
        return url;
    }
    /**
     * 替换url 的  pathvals
     * 如： users/{userid}/{type} 替换成 users/123/456
     * @param url
     * @param parametersMap 
     * @return String 
     */
    public  static String   formatPathUrl(String url,Object parametersMap){
        return formatPathUrl(url,ObjectUtil.voToMap(parametersMap));
    }
    @SuppressWarnings("unused")
    private static HttpResult httpMethodRequest(int method,String url,
            Map<String, String> parametersMap, Map<String, String> headerMap,
            String charSet)
            throws Exception {
        charSet = StringUtils.isBlank(charSet) ? UTF8 : charSet;
        String result = null;
        HttpRequestBase requestMethod = null;
        switch(method){
            case 1:
                url = formatParamUrl(parametersMap, url);
                requestMethod = new HttpGet(url);
                break;
            case 2:
                requestMethod = formatHttpPost(url,parametersMap,charSet);
                break;
            case 3:
                requestMethod = formatHttpPut(url,parametersMap,charSet);
                break;
            case 4:
                url = formatParamUrl(parametersMap, url);
                requestMethod = new HttpDelete(url);
                break;
            default:
                url = formatParamUrl(parametersMap, url);
                requestMethod = new HttpGet(url);
        }
        if (headerMap != null) {
            for (Entry<String, String> entry : headerMap.entrySet()) {
                requestMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
        requestMethod.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET,
                charSet);
       return  executeMethod(requestMethod,url,charSet);
    }
    private static  HttpPost formatHttpPost(String url ,Map<String,String> parametersMap,String charSet){
        HttpPost httpPost = new HttpPost(url);
        if (parametersMap != null) {
            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            for (Entry<String, String> entry : parametersMap.entrySet()) {
                paramList.add(new BasicNameValuePair(entry.getKey(), entry
                        .getValue()));
            }
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(paramList,charSet));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
           // post.set.setRequestBody(paramList.toArray(new NameValuePair[0]));
        }
        return httpPost;
    }
    private static  HttpPut formatHttpPut(String url ,Map<String,String> parametersMap,String charSet){
        HttpPut httpPut = new HttpPut(url);
        if (parametersMap != null) {
            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            for (Entry<String, String> entry : parametersMap.entrySet()) {
                paramList.add(new BasicNameValuePair(entry.getKey(), entry
                        .getValue()));
                //StringEntity  s = new StringEntity(string, mimeType, charset)
            }
            try {
                httpPut.setEntity(new UrlEncodedFormEntity(paramList,charSet));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //  if (StringUtils.isNotBlank(body)) {
            // post.setRequestEntity(new StringRequestEntity(body, "text/xml",
                  //   charSet));
        // }
           // post.set.setRequestBody(paramList.toArray(new NameValuePair[0]));
        }
        return httpPut;
    }
    /**
     * 执行http
     * @param httpMethod
     * @param url
     * @param charSet
     * @return
     */
    private static HttpResult executeMethod(HttpRequestBase httpMethod,String url,String charSet){
        HttpResponse response = null;
        HttpResult httpResult = new HttpResult();
        httpResult.setUrl(url);
        httpResult.setStatus( HttpStatus.SC_INTERNAL_SERVER_ERROR);
        long beginTime = System.currentTimeMillis();
        try {
            response = HttpConnectionManager.httpClient.execute(httpMethod);
            if(response!=null){
                byte[] innerBuff = new byte[512];
                int len = 0;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                InputStream is =  response.getEntity().getContent();
                try {
                    while ((len = is.read(innerBuff)) > 0) {
                        bos.write(innerBuff, 0, len);
                    }
                    httpResult.setBodyByte(bos.toByteArray());
                    httpResult.setBody(new String(bos.toByteArray(),charSet));
                    httpResult.setStatus( response.getStatusLine().getStatusCode());
                    httpResult.setHeaders(response.getAllHeaders());
                } 
                catch(Exception e){
                    e.printStackTrace();
                }
                finally {
                    if (bos != null) {
                        bos.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                }
            }
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }finally{
            httpResult.setExeUseTime(System.currentTimeMillis()-beginTime);
            // 释放连接
            if(response!=null){
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            httpMethod.releaseConnection();
            logger.info(httpResult.toString());
        }
       return httpResult;
    }
    
    /**
     * 格式化成带参数的url
     * @param parametersMap 参数map
     * @param url
     * @return String 
     */
    private static String formatParamUrl(Map<String, String> parametersMap,String url){
        if(parametersMap==null||parametersMap.isEmpty()){
            return url;
        }
        Map<String, String> urlParamMap = getUrlParamMap(url);
        if (urlParamMap != null&&!urlParamMap.isEmpty()) {
             
            parametersMap.putAll(urlParamMap);
        }
        // 和传过来的参数map混合加到url后面
        if (parametersMap!=null&&!parametersMap.isEmpty()) {
            String paramStr = null;
            try {
                paramStr = getUrlParamsByMap(parametersMap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (url.indexOf(Q_MARK) != -1)// 存在?，只取前面的path url
            {
                String[] arrSplit = url.split(URL_PARAM_SPLIT);
                url = arrSplit[0];
            }
            url += Q_MARK+ paramStr;
        }
        return url;
    }
    
    /**
     * 将map转换成url
     * 
     * @param map
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String getUrlParamsByMap(Map<String, String> map)
            throws UnsupportedEncodingException {
        
        if(map ==null||map.isEmpty()){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey() + EQUAL_MARK
                    + URLEncoder.encode(entry.getValue(), UTF8));
            sb.append(AND_MARK);
        }
        String s = sb.toString();
        if (s.endsWith(AND_MARK)) {
            s = StringUtils.substringBeforeLast(s, AND_MARK);
        }
        return s;
    }
    /**
     * 解析出url参数中的键值对 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     * 
     * @param url
     *            url地址
     * @return url请求参数部分
     */
    private static Map<String, String> getUrlParamMap(String url) {
        LinkedHashMap<String, String> mapRequest = null;
        String[] arrSplit = null;
        String strUrlParam = getUrlParams(url);
        if (StringUtils.isEmpty(strUrlParam)) {
            return mapRequest;
        }
        mapRequest=new LinkedHashMap<String, String>();
        // 每个键值为一组
        arrSplit = strUrlParam.split(AND_MARK);
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split(EQUAL_MARK);

            // 解析出键值
            if (arrSplitEqual.length > 1) {
                // 正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (arrSplitEqual[0] != EMPTY_STR) {
                    // 只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], EMPTY_STR);
                }
            }
        }
        return mapRequest;
    }
    /**
     * 去掉url中的路径，留下请求参数部分
     * 
     * @param strURL
     *            url地址
     * @return url请求参数部分
     */
    private static String getUrlParams(String strURL) {
        if(StringUtils.isEmpty(strURL)){
            return null;
        }
        String strAllParam = null;
        strURL = strURL.trim().toLowerCase();
        String[] arrSplit = strURL.split(URL_PARAM_SPLIT);
        if (arrSplit.length > 1) {
            if (arrSplit[1] != null) {
                strAllParam = arrSplit[1];
            }
        }
        return strAllParam;
    }
    /**
     * 是否是map对象
     * @param obj
     * @return
     */
    private static boolean isMapClass(Object obj){
        if(obj==null){
            return false;
        }
       return reflector.isInterface(obj.getClass(),MAP_PACKAGE_NAME);
    }
    public static void main(String args[]){
        /*String url = "https://api.ffan.com/ucenter/v2/thirdPlatformLogin" ;
        String urlLogin = "https://api.ffan.com/ucenter/v2/loginTokens";
        //HttpResult hp = doPost(ut, urlLogin, null);
        Map<String, String> map = new HashMap<String, String>();
        map.put("userName", "15810205084"+i);
        map.put("appid", "feifan");
        map.put("uid", "15000000001528464");
        map.put("loginToken", "de47318caeda356b98ad647fa1615d95");
        doDelete(map, urlLogin,  null);*/
        
    }
}

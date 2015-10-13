package com.feeling.web.aop;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.alibaba.fastjson.JSON;
import com.feeling.annotation.NotEmpty;
import com.feeling.annotation.NotNull;
import com.feeling.enums.ReturnCodeEnum;
import com.feeling.exception.OptException;
import com.feeling.log.LogInfo;
import com.feeling.utils.Reflector;
import com.feeling.web.common.ReturnResult;

/**
 * LogAop.java的实现描述:统一AOP
 * 1.权限验证
 * 2.登陆轨迹日志
 * 3.才参数验证结果
 * 4.方法性能：时间消耗
 * 
 * @author dutao
 *
 */
@Component
@Aspect
public class ControllerAop {

	Reflector r = new Reflector();
	
    @Pointcut("execution(* com.feeling.web.controller.*.*(..))")
    private void anyMethod() {
    }
    @Around("anyMethod()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {


        long start = System.currentTimeMillis();
        StringBuilder methodSB = new StringBuilder();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        boolean paramError =false;//是否参数不符合要求
        String paramErrorMsg = null;//对应错误信息
        try {
        	methodSB.append("execute web method is [").append(className).append(".")
        	 .append(methodName).append("()],params==[");
        	
            Object[] args = joinPoint.getArgs();
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    Object obj = args[i];
                    if (obj != null) {
                    	if(obj.getClass().getName().indexOf("com.feeling.vo")<0){
                        	continue;
                        }
                    	methodSB.append(JSON.toJSONString(obj));
                        Field[] fields = obj.getClass().getDeclaredFields();
                        for (Field field : fields) {
                            if (!paramError&&field.isAnnotationPresent(NotNull.class)) {
                                String filedName = field.getName();
                                Object result = r.getInvoke(obj, filedName,false);
                                if (result == null) {
                                	paramErrorMsg = filedName + "不能为空" ;
                                	paramError=true;
                                	break;
                                }

                            } else if (!paramError&&field.isAnnotationPresent(NotEmpty.class)) {

                            	 String filedName = field.getName();
                                 Object result = r.getInvoke(obj, filedName,false);
                                 NotEmpty notEmpty = field.getAnnotation(NotEmpty.class);
                                 String desc = notEmpty.desc();
                            	 if(StringUtils.isEmpty(desc)){
                            		 desc=filedName;
                            	 }
                                 if (result == null||result.toString().equals("")) {
                                 	paramErrorMsg = desc + "不能为空" ;
                                 	paramError=true;
                                 	break;
                                 }else{
                                	
                                	 int max = notEmpty.maxLength();
                                	 int min = notEmpty.minLength();
                                     if(result.toString().length()<min){
                                    	paramErrorMsg = desc + "长度不能小于["+min +"]";
                                      	paramError=true;
                                      	break;
                                     }else if (result.toString().length()>max){
                                    	paramErrorMsg = desc + "长度不能大于["+max +"]";
                                       	paramError=true;
                                       	break;
                                     }
                                 }
                            }
                        }

                    }
                }
                methodSB.append("]");
            }
            Object retVal=null;
            if(!paramError){
            	retVal = joinPoint.proceed();
            	if(retVal!=null){
            		methodSB.append(";result==["+JSON.toJSONString(retVal)+"]");
            	}else{
            		methodSB.append(";result==[null]");
            	}
            }else{
            	String message = String.format(ReturnCodeEnum.PARAMETER_ERROR.getMessage(), paramErrorMsg);
            	String result=new ReturnResult(ReturnCodeEnum.PARAMETER_ERROR.getCode(), message).toString();
            	methodSB.append(";result==["+result+"]");
            	retVal= result;
            }
            return retVal;
        } catch (OptException ex) {
        //	LogInfo.WEB_LOG.error("LogAop->doAround;", methodSB.toString()+"; error is "+ex.getMessage());
            String result=new ReturnResult(ex.getCode(), ex.getMessage()).toString();
            methodSB.append(";result==["+result+"]");
            return result;
        } catch (Throwable e) {
            String result=new ReturnResult(ReturnCodeEnum.ERROR.getCode(), ReturnCodeEnum.ERROR.getMessage()).toString();
            LogInfo.WEB_LOG.error("LogAop->doAround; had error , error is++++++++++["+e.getMessage()+"]");
            methodSB.append(";result==["+result+"]");
            return result;
        } finally {
            long end = System.currentTimeMillis();
            long expendTime = end - start;
            methodSB.append(";use time is ["+expendTime+"]ms ");
            LogInfo.WEB_LOG.info(methodSB.toString());
        }
    }

}

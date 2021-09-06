package com.kenji.aspect;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSON;
import com.kenji.domain.SysUserLog;
import com.kenji.model.WebLog;
import com.kenji.service.SysUserLogService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Kenji
 * @Date 2021/8/16 17:06
 * @Description
 */
@Aspect
//@Component
@Order(2)
@Slf4j
public class SysUserLogAspect {

    @Autowired
    private SysUserLogService sysUserLogService;

    /**
     * 雪花算法
     * 1、机器的id
     * 2、应用的id
     */
    private Snowflake snowflake = new Snowflake(1,1);

    /**
     * 定义切入点
     */
    @Pointcut("execution( * com.kenji.controller.*.*(..))")
    public void webLog(){
    }

    /**
     * 记录日志的环绕通知
     * @param proceedingJoinPoint
     * @return
     */
    @Around(value = "webLog()")
    public Object recordWebLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = null;
        WebLog webLog = new WebLog();
        long start = System.currentTimeMillis();
        //执行方法的真实调用
        result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        long end = System.currentTimeMillis();
        webLog.setSpendTime((int) ((end-start)/1000));  //请求接口花费的时间
        //获取请求的上下文
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //获取登录的用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取方法
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //获取方法上的ApiOperation注解
        ApiOperation annotation = method.getAnnotation(ApiOperation.class);
        //获取目标对象的类型名称
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        //获取请求的url地址
        String requestUrl = request.getRequestURL().toString();
        //获取当前请求的的request对象
        webLog.setBasePath(StrUtil.removeSuffix(requestUrl, URLUtil.url(requestUrl).getPath()));    //http://ip:port/
        webLog.setDescription(annotation ==null?"no desc":annotation.value());
        webLog.setIp(request.getRemoteAddr());
        webLog.setParameter(getMethodParameter(method,proceedingJoinPoint.getArgs()));
        webLog.setMethod(className+"."+method.getName());
        webLog.setResult(result);
        webLog.setUri(request.getRequestURI());
        webLog.setUrl(requestUrl);
        webLog.setUsername(authentication==null?"anonymous":authentication.getPrincipal().toString());
        SysUserLog sysUserLog = new SysUserLog();
        sysUserLog.setCreated(new Date());
        sysUserLog.setDescription(webLog.getDescription());
        sysUserLog.setGroup(webLog.getDescription());
        sysUserLog.setId(snowflake.nextId());
        sysUserLog.setIp(webLog.getIp());
        sysUserLog.setMethod(webLog.getMethod());
        sysUserLog.setUserId(webLog.getUsername().equals("anonymousUser")?0:Long.valueOf(webLog.getUsername()));
        sysUserLogService.save(sysUserLog);
        return result;

    }

    /**
     * 获取方法的执行参数
     * @param method
     * @param args
     * @return
     * {"key_参数的名称"："value_参数的值"}
     */
    private Object getMethodParameter(Method method, Object[] args) {
        LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer
                = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = localVariableTableParameterNameDiscoverer.getParameterNames(method);
        Map<String,Object> methodParameters = new HashMap<>();
        if (args != null){
            for (int i = 0;i <parameterNames.length;i++){
                methodParameters.put(parameterNames[i],args[i]);
            }
        }
        return methodParameters;
    }
}

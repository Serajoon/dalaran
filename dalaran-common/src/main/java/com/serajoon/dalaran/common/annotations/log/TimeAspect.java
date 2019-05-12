package com.serajoon.dalaran.common.annotations.log;


import com.serajoon.dalaran.common.util.MyLogUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
public class TimeAspect {

    private Logger log = MyLogUtils.log(getClass());
    /**
     * 切入点表达式
     */
    private static final String LOG_ANNOTATION = "@annotation(com.serajoon.dalaran.common.annotations.log.MyLog)";
    /**
     * 日志标识符 7个=
     */
    private static final String LOG_FLAG = "=======";

    /**
     * info,warn,debug日志输出格式
     */
    private static final String LOG_FORMAT = "{} ip:{} class:{} method:{} remark:{} params:{} cost:{}ms";
    /**
     * error日志输出格式
     */
    private static final String LOG_ERROR_FORMAT = "{} ip:{} class:{} method:{} remark:{} params:{} error:{}";

    @Pointcut(LOG_ANNOTATION)
    private void LogPointcut() {
    }

    @Around("LogPointcut()")
    public Object handleMethodLog(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = request.getRemoteAddr();
        Signature pjpSignature = pjp.getSignature();
        String className = pjpSignature.getDeclaringTypeName();
        String methodName = pjpSignature.getName();
        String params = Arrays.toString(pjp.getArgs());
        MethodSignature methodSignature = (MethodSignature) pjpSignature;
        Method method = methodSignature.getMethod();
        MyLog myLog = method.getAnnotation(MyLog.class);
        String desc = myLog.value();
        boolean enable = myLog.enable();
        int max = myLog.max() * 1000;
        StopWatch clock = new StopWatch();
        clock.start();
        Object object = pjp.proceed();
        clock.stop();
        if (enable) {
            long clockTotalTimeMillis = clock.getTotalTimeMillis();
            if (clockTotalTimeMillis > max) {
                log.warn(LOG_FORMAT, LOG_FLAG, ip, className, methodName, desc, params, clockTotalTimeMillis);
            }
            log.info(LOG_FORMAT, LOG_FLAG, ip, className, methodName, desc, params, clockTotalTimeMillis);
        }
        return object;
    }


    @AfterThrowing(pointcut = "LogPointcut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = request.getRemoteAddr();
        Signature pjpSignature = joinPoint.getSignature();
        String className = pjpSignature.getDeclaringTypeName();
        String methodName = pjpSignature.getName();
        String params = Arrays.toString(joinPoint.getArgs());
        MethodSignature methodSignature = (MethodSignature) pjpSignature;
        Method method = methodSignature.getMethod();
        MyLog myLog = method.getAnnotation(MyLog.class);
        String desc = myLog.value();
        boolean enable = myLog.enable();
        if (enable) {
            StringBuilder errorMsg = new StringBuilder();
            StackTraceElement[] trace = e.getStackTrace();
            for (StackTraceElement s : trace) {
                errorMsg.append("\tat ").append(s).append("\r\n");
            }
            log.error(LOG_ERROR_FORMAT, LOG_FLAG, ip, className, methodName, desc, params, errorMsg.toString());
        }
    }
}

package com.hisense.common.aspect.page;

import com.github.pagehelper.util.MetaObjectUtil;
import com.hisense.common.constants.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * 默认分页注解
 *
 * @author hanmeng
 * @since 2019/5/31 10:27
 */
@Aspect
@Component
@Slf4j
public class PageAspect {

    @Around(value = "(@annotation(org.springframework.web.bind.annotation.RequestMapping) || @annotation(org.springframework.web.bind.annotation.GetMapping))" +
            "&& execution(* *PageList(..))")
    @SuppressWarnings("all")
    public Object handleMethodLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature) joinPoint.getSignature())
                .getParameterNames();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Map) {
                doMap(args[i]);
                continue;
            }

            String paramName = paramNames[i];
            if (Objects.equals(paramName, Page.PAGENUM)) {
				if(Objects.isNull(args[i])){
					args[i] = Page.PAGENUM_DEFAULT;
				}
                continue;
            }
            if (Objects.equals(paramName, Page.PAGESIZE)) {
				if( Objects.isNull(args[i])){
					args[i] = Page.PAGESIZE_DEFAULT;
				}
                continue;
            }
            MetaObject metaObject = MetaObjectUtil.forObject(args[i]);
            if(metaObject.isCollection()){
                continue;
            }
			if (Objects.nonNull(args[i])) {
                doPoJo(args[i]);
            }
        }
        return joinPoint.proceed(args);
    }

    private void doMap(Object arg){
        Map<String, Object> parameter0 = (Map<String, Object>) arg;
        parameter0.putIfAbsent(Page.PAGENUM, Page.PAGENUM_DEFAULT);
        parameter0.putIfAbsent(Page.PAGESIZE, Page.PAGESIZE_DEFAULT);
    }

    private void doPoJo(Object arg){
        MetaObject metaObject = MetaObjectUtil.forObject(arg);
        if (metaObject.hasGetter(Page.PAGESIZE)) {
            boolean isPageSizeNullOrZero = Objects.isNull(metaObject.getValue(Page.PAGESIZE)) || Objects.equals(metaObject.getValue(Page.PAGESIZE),0);
            if(isPageSizeNullOrZero){
                metaObject.setValue(Page.PAGESIZE, Page.PAGESIZE_DEFAULT);
            }
        }
        if (metaObject.hasGetter(Page.PAGENUM)) {
            boolean isPageNumNullOrZero = Objects.isNull(metaObject.getValue(Page.PAGENUM)) ||Objects.equals(metaObject.getValue(Page.PAGENUM),0);
            if(isPageNumNullOrZero){
                metaObject.setValue(Page.PAGENUM, Page.PAGENUM_DEFAULT);
            }
        }
    }


}

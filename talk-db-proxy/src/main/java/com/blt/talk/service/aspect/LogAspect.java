/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */
package com.blt.talk.service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handler处理日志
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Component
@Aspect
public class LogAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 定义一个切入点. 解释下：
     *
     * ~ 第一个 * 代表任意修饰符及任意返回值. ~ 第二个 * 定义在web包或者子包 ~ 第三个 * 任意方法 ~ .. 匹配任意数量的参数.
     */
    @Pointcut("execution(* com.blt.talk.service.remote.rest.*.*(..))")
    public void logPointcut() {}

    @Around("logPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            logger.debug("[start] {}#{}", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName());

            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            throw e;
        } finally {
            long end = System.currentTimeMillis();
            logger.debug("[end] {}ms  {}#{}",  end - start, joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName());
        }

    }

}

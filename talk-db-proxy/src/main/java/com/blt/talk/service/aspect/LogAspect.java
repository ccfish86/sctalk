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
    @Pointcut("execution(* com.blt.talk.service.remote.impl.*.*(..))")
    public void logPointcut() {}

    @Around("logPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // LOG.debug("logPointcut " + joinPoint + "\t");
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long end = System.currentTimeMillis();
            logger.info("++Execute [{}]\tUse time : {}ms!", joinPoint.getSignature(), end - start);
            return result;

        } catch (Throwable e) {
            long end = System.currentTimeMillis();
            logger.info("++Execute [{}]\tUse time : {}ms!  with exception : {}", joinPoint.getSignature(), end - start, e.getMessage());
            throw e;
        }

    }

}

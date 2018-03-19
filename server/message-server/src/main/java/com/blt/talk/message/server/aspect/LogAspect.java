package com.blt.talk.message.server.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.IMHeader;
import com.google.protobuf.MessageLite;

/**
 * Handler处理日志
 * <br>
 * 仅在开发和测试环境中启用
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Component
@Aspect
@Profile(value = {"dev", "test"})
public class LogAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 定义一个切入点. 解释下：
     *
     * ~ 第一个 * 代表任意修饰符及任意返回值. ~ 第二个 * 定义在web包或者子包 ~ 第三个 * 任意方法 ~ .. 匹配任意数量的参数.
     */
    @Pointcut("execution(* com.blt.talk.message.server.handler.impl.*.*(..))")
    public void logPointcut() {}

    @Around("logPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // LOG.debug("logPointcut " + joinPoint + "\t");
        long start = System.currentTimeMillis();
        try {
            logger.debug("[start] {}#{}", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName());

            // 输出参数
            Object[] objs = joinPoint.getArgs();
            for (Object obj : objs) {

                if (obj == null) {
                    continue;
                }
                if (obj instanceof IMHeader) {
                    IMHeader header = (IMHeader)obj;
                    logger.debug("[param.header] serviceId:{}, commandId:{}", header.getServiceId(), header.getCommandId());
                } else if (obj instanceof MessageLite) {
                    logger.trace("[param.body] :{}", obj);
                } else {
                    logger.debug("[param.other] :{}", obj);
                }
            }
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

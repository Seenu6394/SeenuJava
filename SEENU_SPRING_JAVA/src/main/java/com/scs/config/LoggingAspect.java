package com.scs.config;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	private Logger logger = Logger.getLogger(getClass());

	@Before("execution(* com.scs.controller.*.*(..)) || execution(* com.scs.service.*.*(..)) || execution(* com.scs.exception.*.*(..))")
	public void logBefore(JoinPoint joinPoint) {
		logger.debug(joinPoint.getSourceLocation().getWithinType().getSimpleName() + ":"
				+ joinPoint.getSignature().getName() + " - ENTRY");
	}

	@After("execution(* com.scs.controller.*.*(..)) || execution(* com.scs.service.*.*(..)) || execution(* com.scs.exception.*.*(..))")
	public void logAfter(JoinPoint joinPoint) {
		logger.debug(joinPoint.getSourceLocation().getWithinType().getSimpleName() + ":"
				+ joinPoint.getSignature().getName() + " - EXIT");
	}

	@AfterThrowing(pointcut = "execution(* com.scs.controller.*.*(..)) || execution(* com.scs.service.*.*(..)) || execution(* com.scs.exception.*.*(..))", throwing = "error")
	public void logAfterThrowing(JoinPoint joinPoint, Exception error) {
		logger.debug(joinPoint.getSourceLocation().getWithinType().getSimpleName() + ":"
				+ joinPoint.getSignature().getName() + " - EXCEPTION: " + error);
	}

}
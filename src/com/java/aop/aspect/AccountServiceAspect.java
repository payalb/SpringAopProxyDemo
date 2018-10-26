package com.java.aop.aspect;

import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/*With @AspectJ support enabled, any bean defined in your application context with a class that is an @AspectJ aspect (has the @Aspect annotation) is automatically detected by Spring and used to configure Spring AOP. */
@Aspect
@Component
public class AccountServiceAspect {
	/*
	 * When two pieces of advice defined in different aspects both need to run at
	 * the same join point, unless you specify otherwise, the order of execution is
	 * undefined. You can control the order of execution by specifying precedence.
	 * This is done in the normal Spring way by either implementing the
	 * org.springframework.core.Ordered interface in the aspect class or annotating
	 * it with the Order annotation. Given two aspects, the aspect returning the
	 * lower value from Ordered.getValue() (or the annotation value) has the higher
	 * precedence.
	 */
	@Order(1)
	@Before(value = "com.infotech.aop.PointcutDefinition.serviceLayer()")
	public void beforeAdvice(JoinPoint joinPoint) {
		System.out.println("Before method:" + joinPoint.getSignature().getName() + ",Class:"
				+ joinPoint.getTarget().getClass().getSimpleName());
	}

	@Order(1)
	@After(value = "com.infotech.aop.PointcutDefinition.serviceLayer()")
	public void afterAdvice(JoinPoint joinPoint) {
		System.out.println("After method:" + joinPoint.getSignature().getName() + ",Class:"
				+ joinPoint.getTarget().getClass().getSimpleName());
	}

	@Order(2)
	@Around("execution(public String AccountServiceImpl.updateAccountBalance(..))")
	public Object interceptAndLog(ProceedingJoinPoint invocation) throws Throwable {
		try {
			return invocation.proceed();
		} catch (Exception e) {
			Logger.getLogger("AspectJ")
					.warning("THE INJECTED CODE SAYS: the method " + invocation.getSignature().getName()
							+ " failed for the input '" + invocation.getArgs()[0] + "'. Original exception: " + e);
			throw e;
		}
	}
}

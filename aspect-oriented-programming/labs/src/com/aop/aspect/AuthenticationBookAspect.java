package com.aop.aspect;

import ajia.security.AbstractAuthenticationAspect;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AuthenticationBookAspect extends AbstractAuthenticationAspect
{
    @Override
//    @Pointcut("execution(* com.aop.model.Library.*Book(..))")
    @Pointcut("execution(@AuthRequired * *(..))")
    public void authenticationRequired() {}
}

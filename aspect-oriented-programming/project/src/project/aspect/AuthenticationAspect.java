
package project.aspect;

import ajia.security.AbstractAuthenticationAspect;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


@Aspect
public class AuthenticationAspect extends AbstractAuthenticationAspect
{
    @Override
    @Pointcut("execution(@AuthRequired * *(..))")
    public void authenticationRequired() {}
}

package project.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CacheAspect
{
    @Pointcut("execution(* project.repository.JDBCProductRepository.*(..))")
    public void cachePointcut() {}
}

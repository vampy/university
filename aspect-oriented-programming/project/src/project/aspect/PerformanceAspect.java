package project.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PerformanceAspect
{
    @Pointcut("execution(* project.repository.JDBCProductRepository.*(..))")
    public void performancePointcut() {}
}

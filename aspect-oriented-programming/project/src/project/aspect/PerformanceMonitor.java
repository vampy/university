package project.aspect;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

public class PerformanceMonitor implements MethodInterceptor
{
    private static final Logger log = Logger.getLogger("performanceLogger");

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        final StopWatch stopWatch = new StopWatch(invocation.getMethod().toGenericString());
        stopWatch.start("invocation.proceed()");

        try
        {
            return invocation.proceed();
        }
        finally
        {
            stopWatch.stop();
            log.trace(stopWatch.shortSummary());
        }
    }
}

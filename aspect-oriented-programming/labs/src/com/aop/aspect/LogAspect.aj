package com.aop.aspect;

import com.aop.log.Log;
import com.aop.controller.*;
import com.aop.model.*;
import org.aspectj.lang.JoinPoint;

public aspect LogAspect
{
    pointcut publicMethodExecuted(): (execution(public * *(..))) && !cflow(within(LogAspect))
        && !(within(BooksMainTableModel || BooksTerminalTableModel || BooksTerminalUserTableModel || Book));

    before(): publicMethodExecuted()
        {
            Log.get().info(String.format("[Enters]: %s | %s", thisJoinPoint.getSignature(), createParams(thisJoinPoint)));
        }

    String createParams(JoinPoint joinPoint)
    {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0)
        {
            StringBuffer result = new StringBuffer();
            result.append("Arguments value: ");
            for (Object o : args)
            {
                result.append(" " + o + ", ");
            }
            return result.toString();
        }
        return "";
    }
}

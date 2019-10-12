package project.aspect;

import project.log.Log;
import project.controller.*;
import project.model.*;
import org.aspectj.lang.JoinPoint;


public aspect LogAspect
{
    pointcut publicMethodExecuted(): (execution(public * *(..))) && !cflow(within(LogAspect))
        && !(within(MainTableModel || TerminalTableModel || TerminalUserTableModel || Product));

    before(): publicMethodExecuted()
        {
            Log.getAll().info(String.format("[Enters]: %s | %s", thisJoinPoint.getSignature(), createParams(thisJoinPoint)));
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

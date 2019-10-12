package project.log;

import org.apache.log4j.Logger;

public class Log
{
    private static Logger logger = Logger.getLogger("allLogger");

    /**
     * Getter for property 'logger'.
     *
     * @return Value for property 'logger'.
     */
    public static Logger getAll()
    {
        return logger;
    }
}

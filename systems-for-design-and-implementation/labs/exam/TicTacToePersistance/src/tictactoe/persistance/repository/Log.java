package tictactoe.persistance.repository;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

// TODO add log to file from proprieties file
public class Log
{
    private static Logger  logger = Logger.getLogger("log");
    private static boolean added  = false;

    /**
     * Getter for property 'logger'.
     *
     * @return Value for property 'logger'.
     */
    public static Logger get()
    {
        if (!added)
        {
            try
            {
                FileHandler file = new FileHandler("sdi.log");
                file.setFormatter(new SimpleFormatter());
                logger.addHandler(file);
            }
            catch (IOException e)
            {
                logger.warning("Failed to add file handle for log");
            }
            added = true;
        }
        return logger;
    }
}

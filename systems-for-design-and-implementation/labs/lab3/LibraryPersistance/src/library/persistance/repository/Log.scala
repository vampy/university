package library.persistance.repository

import java.io.IOException
import java.util.logging.{FileHandler, Logger, SimpleFormatter}

object Log
{
    private val logger: Logger = Logger.getLogger("log")
    private var added: Boolean = false

    def get: Logger =
    {
        if (!added)
        {
            try
            {
                val file: FileHandler = new FileHandler("sdi.log")
                file.setFormatter(new SimpleFormatter)
                logger.addHandler(file)
            }
            catch
            {
                case e: IOException =>
                {
                    logger.warning("Failed to add file handle for log")
                }
            }
            added = true
        }
        logger
    }
}

package se306.team7;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class TaskScheduler
{
    public static void main( String[] args )
    {
        //BasicConfigurator.configure();
        PropertyConfigurator.configure("src/log4j.properties");
        Logger logger = LoggerFactory.getLogger(TaskScheduler.class);
        logger.trace("trace message");
        logger.debug("debug message");
        logger.info("info message");
        logger.warn("warn message");
        logger.error("error message");
        System.out.println( "Hello World!" );
    }
}

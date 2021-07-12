package cn.yeegro.nframe.log.monitor;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
/**
 *  业务结构化日志
 */
public class BizLog {
	private static final ILogger LOGGER =  SLoggerFactory.getLogger(BizLog.class);

    public static void info(String message,Object...params ) {
    	LOGGER.info(message, params);
    }

    public static void debug(String message,Object...params ) {
    	LOGGER.debug(message, params);
    }
    
    public static void trace(String message,Object...params ) {
    	LOGGER.trace(message, params);
    }
    

	
}

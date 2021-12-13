package cc.akashic.insight;

import java.util.logging.Logger;

public final class Log {
    private static Logger log;

    /**
     * Get the logger.
     *
     * @return logger for Log
     */
    public static Logger getLogger() {
        return log;
    }

    /**
     * Set the logger.
     *
     * @param logger logger for Log
     */
    public static void setLogger(Logger logger) {
        log = logger;
    }

    /**
     * Log an INFO message.
     *
     * @param msg message
     */
    public static void info(String msg) {
        log.info(msg);
    }
}

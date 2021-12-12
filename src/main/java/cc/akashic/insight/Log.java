package cc.akashic.insight;

import java.util.logging.Logger;

public final class Log {
    private static Logger log;

    public static void setLogger(Logger logger) {
        log = logger;
    }

    public static void info(String msg) {
        log.info(msg);
    }
}

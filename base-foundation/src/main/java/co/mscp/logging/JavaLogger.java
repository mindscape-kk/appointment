package co.mscp.logging;


import co.mscp.Cache;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;


public class JavaLogger extends Logger {

    // --- NESTED TYPES --- //

    private static class HandlerImpl extends Handler {
        private SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss.S");

        @Override
        public void publish(LogRecord record) {
            StringBuilder sb = new StringBuilder();
            sb.append("[").append(record.getLevel()).append("] ")
                .append(f.format(new Date(record.getMillis())))
                .append(" ")
                .append(record.getMessage());
            System.out.println(sb.toString());
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }
    }



    // --- STATIC FIELDS --- //

    private static final Cache<Class<?>, JavaLogger> cache = new Cache<>();



    // --- STATIC INITIALIZER --- //

    static {
        LogManager.getLogManager().reset();
        java.util.logging.Logger rootLogger
            = LogManager.getLogManager().getLogger("");
        rootLogger.addHandler(new HandlerImpl());
    }



    // --- STATIC METHODS --- //

    public static JavaLogger of(Class<?> cls) {
        return cache.getOrMake(cls, () -> new JavaLogger(cls));
    }



    // --- FIELDS --- //

    private final java.util.logging.Logger impl;



    // --- CONSTRUCTORS --- //

    private JavaLogger(Class<?> cls) {
        impl = java.util.logging.Logger.getLogger(cls.getCanonicalName());
    }



    // --- METHODS --- //

    @Override
    public void log(Logger.Level level, boolean alert, String message, Object... params) {
        final java.util.logging.Level jLevel;
        switch (level) {
            case INFO: jLevel = java.util.logging.Level.INFO; break;
            case WARN: jLevel = java.util.logging.Level.WARNING; break;
            case DEBUG: jLevel = java.util.logging.Level.FINE; break;
            case ERROR: jLevel = java.util.logging.Level.SEVERE; break;
            default: jLevel = java.util.logging.Level.FINE; break;
        }

        if(alert) {
            message = "[!] " + message;
        }

        impl.log(jLevel, String.format(message, params));
    }

}

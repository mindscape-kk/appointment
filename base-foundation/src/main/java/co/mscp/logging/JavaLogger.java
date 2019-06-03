package co.mscp.logging;


import co.mscp.Cache;
import co.mscp.Logger;

public class JavaLogger extends Logger {
    
    private static final Cache<Class<?>, JavaLogger> cache = new Cache<>();
    
    public static JavaLogger of(Class<?> cls) {
        return cache.getOrMake(cls, () -> new JavaLogger(cls));
    }

    private final java.util.logging.Logger impl;
    
    private JavaLogger(Class<?> cls) {
        impl = java.util.logging.Logger.getLogger(cls.getCanonicalName());
    }

    @Override
    public void log(Level level, boolean alert, String message, Object... params) {
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

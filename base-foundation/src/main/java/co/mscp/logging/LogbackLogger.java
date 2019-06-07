package co.mscp.logging;

import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class LogbackLogger extends Logger {
    
    private static final Marker ALERT_MAERKER = MarkerFactory.getMarker("alert");
    private static final Marker DUMP_MAERKER = MarkerFactory.getMarker("dump");
    
    private final org.slf4j.Logger impl;
    
    public LogbackLogger(Class<?> cls) {
        impl = LoggerFactory.getLogger(cls);
    }
    
    @Override
    public void log(Logger.Level level, boolean alert, String message, Object... params) {
        String msg = String.format(message, params);
        
        Marker marker = alert?ALERT_MAERKER:DUMP_MAERKER;
    
        switch (level) {
            case INFO: impl.info(marker, msg); break;
            case WARN: impl.warn(marker, msg); break;
            case DEBUG: impl.debug(marker, msg); break;
            case ERROR: impl.error(marker, msg); break;
            default: impl.debug(marker, msg); break;
        }
    }
    
    @Override
    public void error(Throwable th) {
        impl.error(DUMP_MAERKER, "", th);
    }
    
    @Override
    public void error(String msg, Throwable th) {
        impl.error(DUMP_MAERKER, msg, th);
    }
}

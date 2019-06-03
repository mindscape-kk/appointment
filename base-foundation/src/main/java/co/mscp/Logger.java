package co.mscp;


import co.mscp.logging.JavaLogger;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class Logger {

    public enum Level {
        DEBUG,
        INFO,
        WARN,
        ERROR
    }


    public static Logger of(Class<?> cls) {
        return JavaLogger.of(cls);
    }


    public static Logger of(Object obj) {
        if(obj == null) {
            throw new NullPointerException();
        }
        
        return of(obj.getClass());
    }


    private static String toString(Throwable th) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        th.printStackTrace(pw);
        return sw.toString();
    }


    public abstract void log(Level level, boolean alert, String message, Object... params);

    
    public void debug(String msg, Object... params) {
        log(Level.DEBUG, false, msg, params);
    }


    public void info(String msg, Object... params) {
        log(Level.INFO, false, msg, params);
    }


    public void warn(String msg, Object... params) {
        log(Level.WARN, false, msg, params);
    }


    public void error(String msg, Object... params) {
        log(Level.ERROR, false, msg, params);
    }


    public void error(Throwable th) {
        log(Level.ERROR, false, toString(th));
    }


    public void error(String msg, Throwable th) {
        log(Level.ERROR, false, msg + "\n" + toString(th));
    }
    
}

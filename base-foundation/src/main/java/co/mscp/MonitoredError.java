
package co.mscp;

public class MonitoredError extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MonitoredError(String message, Throwable cause) {
        super(message, cause);
    }

    public MonitoredError(String message) {
        super(message);
    }

    public MonitoredError(Throwable cause) {
        super(cause);
    }
    
}

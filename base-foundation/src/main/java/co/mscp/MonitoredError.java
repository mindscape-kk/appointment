/*---[SPEEDA Radar]--------------------------------------------m(._.)m--------*\
 |
 |  Copyright (c) 2018 Uzabase Inc. all rights reserved.
 |
 |  Author: Asia PDT (asia-pdt@uzabase.com)
 |
 *//////////////////////////////////////////////////////////////////////////////


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

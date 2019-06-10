package co.mscp.net;

import org.apache.http.HttpStatus;
import org.apache.http.impl.EnglishReasonPhraseCatalog;

import java.util.Locale;


public enum HttpErrorStatus {
    BAD_REQUEST(HttpStatus.SC_BAD_REQUEST),   // 400
    UNAUTHORIZED(HttpStatus.SC_UNAUTHORIZED), // 401
    FORBIDDEN(HttpStatus.SC_FORBIDDEN),       // 403
    NOT_FOUND(HttpStatus.SC_NOT_FOUND),       // 404
    METHOD_NOT_ALLOWED(HttpStatus.SC_METHOD_NOT_ALLOWED), // 405
    NOT_ACCEPTABLE(HttpStatus.SC_NOT_ACCEPTABLE), // 406
    CONFLICT(HttpStatus.SC_CONFLICT),             // 409
    GONE(HttpStatus.SC_GONE),                     // 410
    EXPECTATION_FAILED(HttpStatus.SC_EXPECTATION_FAILED),         // 417
    UNPROCESSABLE_ENTITY(HttpStatus.SC_UNPROCESSABLE_ENTITY),     // 422
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE), // 425
    INTERNAL_SERVER_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR),   // 500
    NOT_IMPLEMENTED(HttpStatus.SC_NOT_IMPLEMENTED),               // 501
    BAD_GATEWAY(HttpStatus.SC_BAD_GATEWAY),                       // 502
    SERVICE_UNAVAILABLE(HttpStatus.SC_SERVICE_UNAVAILABLE),       // 503
    ;

    public static HttpErrorStatus of(int code) {
        for(HttpErrorStatus stt: values()) {
            if(stt.code == code) {
                return stt;
            }
        }
        return null;
    }

    private final int code;

    HttpErrorStatus(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

    public String reason() {
        return EnglishReasonPhraseCatalog.INSTANCE.getReason(code, Locale.ENGLISH);
    }

}

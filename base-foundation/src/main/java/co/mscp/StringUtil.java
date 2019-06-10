package co.mscp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;



public class StringUtil {
    
    private StringUtil() {
        // nothing
    }
    
    public static String toString(String delimeter, Object ... objects) {
        StringBuilder b = new StringBuilder();
        boolean notFirst = false;
        for(Object o: objects) {
            if(notFirst) {
                b.append(delimeter);
            } else {
                notFirst = true;
            }
            b.append(o.toString());
        }
        return b.toString();
    }
    
    public static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new Error("This should not have happened", e);
        }
    }
    
}
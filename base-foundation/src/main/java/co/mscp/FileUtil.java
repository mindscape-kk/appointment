/*---[SPEEDA Radar]--------------------------------------------m(._.)m--------*\
 |
 |  Copyright (c) 2018 Uzabase Inc. all rights reserved.
 |
 |  Author: Asia PDT (asia-pdt@uzabase.com)
 |
 *//////////////////////////////////////////////////////////////////////////////


package co.mscp;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public class FileUtil {
    
    public static File mkDirs(String path) {
        File dir = new File(path);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }
    
    public static File tmpDir() {
        return mkDirs("tmp");
    }
    
    public static File logsDir() {
        return mkDirs("log");
    }

    public static File tmpFile(String fileName) throws IOException {
        File tempFile = new File(tmpDir(),fileName);
        tempFile.createNewFile();
        return  tempFile;
    }
    
    public static String getPathDecoded(URL url) {
        try {
            return URLDecoder.decode(url.getPath(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new MonitoredError("This should not have happened", e);
        }
    }
    
}

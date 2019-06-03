package co.mscp;


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DockerUtil {
    
    private static PrintStream logWriter = null;
    
    private static void log(String msg) {
        System.out.println(msg);
        logToFile(msg);
    }
    
    private static void logDocker(String msg) {
        logToFile("Docker: " + msg);
    }
    
    private static void logToFile(String msg)  {
        if(logWriter == null) {
            try {
                logWriter = new PrintStream(new FileOutputStream(
                    new File(FileUtil.logsDir(), "docker-util.log"), true));
    
                Runtime.getRuntime().addShutdownHook(new Thread(() -> logWriter.close()));
            } catch (FileNotFoundException ex) {
                logWriter = System.out;
            }
        }
        logWriter.println(msg);
        logWriter.flush();
    }
    
    
    private static <T> T forEachOutputLine(Process p, boolean processAll,
        Function<String, T> fn) throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        T result = null;
        
        while((result == null || processAll)
            && (line = in.readLine()) != null)
        {
            T thisResult = fn.apply(line);
            if(thisResult != null && result == null) {
                result = thisResult;
            }
        }
        
        return result;
    }

    private static File datafile(String label) throws IOException {
        return FileUtil.tmpFile(label);
    }

    private static String resolveLabelToId(String label) throws IOException {
        File file = datafile(label);
        if(!file.exists()) {
            return null;
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String containerId = reader.readLine();
        reader.close();

        return containerId;
    }

    
    public static Process dockerExec(String... params) throws IOException {
        String[] commands = new String[params.length + 2];
        commands[0] = "/usr/bin/env";
        commands[1] = "docker";

        System.arraycopy(params, 0, commands, 2, params.length);
        
        ProcessBuilder builder = new ProcessBuilder(commands);
        builder.redirectErrorStream(true);
        logDocker("> Running: " + String.join(" ", commands));
        return builder.start();
    }
    
    
    public static void dockerExecThrough(String... params) throws IOException, InterruptedException {
        Process p = dockerExec(params);

        AtomicReference<String> lastLine = new AtomicReference<>("");

        forEachOutputLine(p, true, line -> {
            lastLine.set(line);
            logDocker(line);
            return null;
        });

        if(p.waitFor() != 0) {
            throw new MonitoredError("Docker operation failed (see logs for details): " + lastLine.get());
        }
    }


    public static String build(URL dockerDir) throws IOException, InterruptedException {
        String path = FileUtil.getPathDecoded(dockerDir);
        
        log("Building docker image at: " + path);
        
        Process p = dockerExec("build", path);
        
        Matcher matcher = Pattern.compile("Successfully built (\\p{Alnum}*)").matcher("");

        AtomicReference<String> lastLine = new AtomicReference<>("");

        String imageId = forEachOutputLine(p, true, line -> {
            lastLine.set(line);
            logDocker(line);
            matcher.reset(line);
            if(matcher.find()) {
                return matcher.group(1).trim();
            }
            return null;
        });
        
        log("Image created: " + imageId);

        if(p.waitFor() != 0) {
            throw new MonitoredError("Could not build docker image (see logs for details): " + lastLine.get());
        }
        
        return imageId;
    }
    
    
    public static String run(String label, String imageName,
            Map<Integer, Integer> portMapping, String environment)
        throws IOException, InterruptedException
    {
        log("Running docker image: " + imageName);

        List<String> paramsList = new ArrayList<>();
        paramsList.add("run");
        paramsList.add("-idt");
        portMapping.forEach((k, v) -> {
            paramsList.add("-p");
            paramsList.add(v.toString() + ":" + k.toString());
        });
        if(environment != null) {
            paramsList.add("-e");
            paramsList.add(environment);
        }
        paramsList.add(imageName);
        
        String[] params = new String[paramsList.size()];
        
        Process p = dockerExec(paramsList.toArray(params));

        AtomicReference<String> lastLine = new AtomicReference<>("");
        String containerId = forEachOutputLine(p, true, line -> {
            lastLine.set(line);
            logDocker(line);
            return line;
        });
    
        if(p.waitFor() != 0) {
            throw new MonitoredError("Could not start docker container (see logs for details): " + lastLine.get());
        }
        
        log("Container ID: " + containerId);

        PrintWriter writer = new PrintWriter(FileUtil.tmpFile(label));
        writer.println(containerId);
        writer.close();

        return containerId;
    }
    

    public static void waitForContainerWithLabel(String label, String flag)
        throws IOException
    {
        final String containerId = resolveLabelToId(label);
        if(containerId == null) {
            throw new MonitoredError("No container with label: " + label);
        }

        log("Waiting for container to boot.");
        logDocker("> Boot flag: \"" + flag + "\n");

        Process p = dockerExec("logs", "-f", containerId);

        Object flagFound = forEachOutputLine(p, false, line -> {
            logDocker(line);
            if(line.contains(flag)) {
                log("Docker container boot confirmed.");
                return true;
            }
            return null;
        });

        if(flagFound == null) {
            throw new MonitoredError("Docker container job completed by boot flag was not observed. See longs for details.");
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // Nothing
        }
    }


    @Deprecated
    public static void waitForContainerFlag(String containerId, String flag)  {
        throw new UnsupportedOperationException("This method is deprecated");
    }
    
    
    public static void stop(String label) throws IOException, InterruptedException {
        final String containerId = resolveLabelToId(label);
        if(containerId == null) {
            return;
        }

        log("Stopping docker container: " + containerId);
        dockerExecThrough("stop", containerId);

        log("Removing docker container: " + containerId);
        dockerExecThrough("rm", "-f", "-v", containerId);

        if(datafile(label).delete()) {
            log("File deleted: " + datafile(label));
        }
    }


    public static boolean isRunning(String label) throws IOException, InterruptedException {
        final String containerId = resolveLabelToId(label);
        if(containerId == null) {
            return false;
        }

        Process p = dockerExec("ps", "-q", "--no-trunc");

        AtomicBoolean found = new AtomicBoolean(false);

        forEachOutputLine(p, false, line -> {
            Logger.of(DockerUtil.class).info(line);
            if(line.equals(containerId)) {
                found.set(true);
                return true;
            }
            return null;
        });

        return found.get();
    }

    
    public static void remove(String imageId) throws IOException, InterruptedException {
        log("Removing docker image: " + imageId);
        dockerExecThrough("rmi", imageId);
    }
    
    
    private DockerUtil() {
        // Nothing
    }

}

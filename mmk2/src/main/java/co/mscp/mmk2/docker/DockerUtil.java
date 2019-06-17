package co.mscp.mmk2.docker;

import co.mscp.mmk2.net.NetworkUtil;
import co.mscp.mmk2.logging.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DockerUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static PrintStream logWriter = null;
    private static Thread logThread = null;


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


    private static void log(String msg) {
        Logger.of(DockerUtil.class).info(msg);
        logToFile(msg);
    }


    private static void logDocker(String msg) {
        logToFile("Docker: " + msg);
    }


    private static void logToFile(String msg)  {
        if(logWriter == null) {
            try {
                logWriter = new PrintStream(new FileOutputStream(
                        getLogFile(), true));

                Runtime.getRuntime().addShutdownHook(new Thread(() -> logWriter.close()));
            } catch (FileNotFoundException ex) {
                logWriter = System.out;
            }
        }
        logWriter.println(msg);
        logWriter.flush();
    }


    public static File getLogFile() {
        return new File(logsDir(), "docker-util.log");
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
        return tmpFile(label);
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
            throw new DockerError("Docker operation failed (see logs for details): " + lastLine.get());
        }
    }


    public static void insertLogSeparator() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        logDocker("");
        logDocker("======================================");
        logDocker(f.format(new Date()));
        logDocker("======================================");
    }


    public static String build(URL dockerDir) throws IOException, InterruptedException {
        String path = NetworkUtil.getPathDecoded(dockerDir);

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
            throw new DockerError("Could not build docker image (see logs for details): " + lastLine.get());
        }

        return imageId;
    }


//    public static String run(String label, String imageName,
//                             Map<Integer, Integer> portMapping, String... environment)
//            throws IOException, InterruptedException
//    {
//        return run(label, imageName, portMapping, ContainerNetwork.host(), environment);
//    }


    public static String run(String label, String imageName,
                             Map<Integer, Integer> portMapping, ContainerNetwork network,
                             String... environment)
            throws IOException, InterruptedException
    {
        log("Running docker image \"" + label + "\": " + imageName);

        List<String> paramsList = new ArrayList<>();
        paramsList.add("run");
        paramsList.add("-idt");
        portMapping.forEach((k, v) -> {
            paramsList.add("-p");
            paramsList.add(v.toString() + ":" + k.toString());
        });
        for (String env: environment) {
            paramsList.add("-e");
            paramsList.add(env);
        }
        paramsList.add(containerNetworkToArg(network));
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
            throw new DockerError("Could not start docker container (see logs for details): " + lastLine.get());
        }

        log("Container ID: " + containerId);

        PrintWriter writer = new PrintWriter(tmpFile(label));
        writer.println(containerId);
        writer.close();

        return containerId;
    }


    public static void waitForContainerWithLabel(String label, String flag)
            throws IOException
    {
        final String containerId = resolveLabelToId(label);
        if(containerId == null) {
            throw new DockerError("No container with label: " + label);
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
            throw new DockerError("Docker container job completed by boot flag was not observed. See longs for details.");
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // Nothing
        }
    }


    public static void monitorLogs(String label) throws IOException {
        if(logThread != null) {
            return;
        }

        final String containerId = resolveLabelToId(label);
        if(containerId == null) {
            throw new DockerError("No container with label: " + label);
        }

        Process p = dockerExec("logs", "-f", containerId);

        logThread = new Thread(() -> {
            try {
                log("Monitoring: " + label);
                forEachOutputLine(p, true, line -> {
                    logDocker(label + ": " + line);
                    return null;
                });
                log("Container stopped: " + label);
            } catch (IOException e) {
                Logger.of(DockerUtil.class).error(e);
            }
        });

        logThread.start();
    }


    public static void stop(String label) throws IOException, InterruptedException {
        final String containerId = resolveLabelToId(label);
        if(containerId == null) {
            return;
        }

        log("Stopping container: " + label);
        dockerExecThrough("stop", containerId);

        log("Removing container: " + label);
        dockerExecThrough("rm", "-f", "-v", containerId);

        if(datafile(label).delete()) {
            log("File deleted: " + datafile(label));
        }
    }


    public static boolean isRunning(String label) throws IOException {
        final String containerId = resolveLabelToId(label);
        if(containerId == null) {
            return false;
        }

        Process p = dockerExec("ps", "-q", "--no-trunc");

        AtomicBoolean found = new AtomicBoolean(false);

        forEachOutputLine(p, false, line -> {
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


    private static JsonNode inspect(String label) throws IOException {
        final String containerId = resolveLabelToId(label);
        if(containerId == null) {
            throw new DockerError("No container with label: " + label);
        }

        StringBuilder builder = new StringBuilder();

        Process p = dockerExec("inspect", containerId);
        forEachOutputLine(p, true, builder::append);

        return MAPPER.readTree(builder.toString());
    }


    public static String getIpAddressOf(String label) throws IOException {
        return inspect(label)
                .get(0)
                .get("NetworkSettings")
                .get("IPAddress")
                .asText();
    }


    private static String containerNetworkToArg(ContainerNetwork n) {
        final String prefix = "--network=";
        switch (n.getType()) {
            case HOST:
                return prefix + "host";
            case BRIDGE:
                return prefix + "bridge";
            case CONTAINER:
                return prefix + "container:" + n.getParam();
            case NONE:
                return prefix + "none";
            case CUSTOM:
                return prefix + n.getParam();
        }
        return prefix + "host";
    }


    private DockerUtil() {
        // Nothing
    }
}

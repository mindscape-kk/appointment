package co.mscp;

import java.io.IOException;
import java.net.URL;
import java.util.Map;


public class DockerHelper {

    // --- INNER TYPES --- //

    public static class DockerParams {
        public final String label;
        public final String dir;
        public final Map<Integer, Integer> portMap;

        public DockerParams(String label, String dir, Map<Integer, Integer> portMap) {
            this.label = label;
            this.dir = dir;
            this.portMap = portMap;
        }
    }


    // --- STATIC FIELDS --- //

    private static final String[] DB_ENV = new String[]{"POSTGRES_PASSWORD=medicom"};
    private static final String LOAD_FLAG = "DB init process done. Ready for start up.";

    public static final int DB_PORT = 5432;

    public static final DockerParams DB = new DockerParams("medicom-db", "/db/Dockerfile",
            Map.of(5432, DB_PORT));




    // --- STATIC METHODS --- //

    public static void runDocker(DockerParams p) throws IOException, InterruptedException {
        if(!co.mscp.DockerUtil.isRunning(p.label)) {
            co.mscp.DockerUtil.insertLogSeparator();

            URL dockerFile = CommonSteps.class.getResource(p.dir);
            if(dockerFile == null) {
                throw new Error("Docker file could not found at " + p.dir);
            }
            String imageId = co.mscp.DockerUtil.build(dockerFile);
            co.mscp.DockerUtil.run(p.label, imageId, p.portMap, ContainerNetwork.bridge(), DB_ENV);

            co.mscp.DockerUtil.waitForContainerWithLabel(p.label,LOAD_FLAG);
        }
    }


    public static void stopDocker(DockerParams p) throws IOException, InterruptedException {
        if(co.mscp.DockerUtil.isRunning(p.label)) {
            DockerUtil.stop(p.label);
        }
    }


    public static void runAll() throws IOException, InterruptedException {
        runDocker(DB);
    }


    public static void stopAll() throws IOException, InterruptedException {
        stopDocker(DB);
    }


    public static void main(String[] args) {
        try {
            stopAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

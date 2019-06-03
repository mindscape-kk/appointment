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
    private static final String LOAD_FLAG = "MySQL init process done. Ready for start up.";

    public static final int COMMON_PORT = 3361;
    public static final int FINANCE_PORT = 3362;

    public static final DockerParams COMMON = new DockerParams("sqlbatch-spec-test-common", "/docker/common",
            FluentMap.with(3306, COMMON_PORT));

    public static final DockerParams FINANCE = new DockerParams("sqlbatch-spec-test-finance", "/docker/finance",
            FluentMap.with(3306, FINANCE_PORT));



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
            com.uzabase.jharmony.docker.DockerUtil.stop(p.label);
        }
    }


    public static void runAll() throws IOException, InterruptedException {
        runDocker(COMMON);
        runDocker(FINANCE);
    }


    public static void stopAll() throws IOException, InterruptedException {
        stopDocker(COMMON);
        stopDocker(FINANCE);
    }


    public static void main(String[] args) {
        try {
            stopAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

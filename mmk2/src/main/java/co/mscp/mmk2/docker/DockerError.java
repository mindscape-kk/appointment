package co.mscp.mmk2.docker;

public class DockerError extends RuntimeException {

    public DockerError(String message) {
        super(message);
    }

    public DockerError(String message, Throwable cause) {
        super(message, cause);
    }

    public DockerError(Throwable cause) {
        super(cause);
    }

}

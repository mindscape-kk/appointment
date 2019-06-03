package co.mscp;

public class ContainerNetwork {
    public static enum Type {
        BRIDGE,
        NONE,
        HOST,
        CONTAINER,
        CUSTOM
    }
    
    public static ContainerNetwork bridge() {
        return new ContainerNetwork(Type.BRIDGE, null);
    }
    
    public static ContainerNetwork none() {
        return new ContainerNetwork(Type.NONE, null);
    }
    
    public static ContainerNetwork host() {
        return new ContainerNetwork(Type.HOST, null);
    }
    
    public static ContainerNetwork sameAsContainer(String id) {
        return new ContainerNetwork(Type.CONTAINER, id);
    }
    
    public static ContainerNetwork custom(String name) {
        return new ContainerNetwork(Type.CUSTOM, name);
    }
    
    
    private final Type t;
    private final String p;
    
    
    private ContainerNetwork(Type t, String param) {
        this.t = t;
        this.p = param;
    }
    
    
    public Type getType() {
        return t;
    }
    
    
    public String getParam() {
        return p;
    }
}

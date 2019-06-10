package co.mscp.net;


import co.mscp.Json;
import co.mscp.MonitoredError;
import co.mscp.PartialList;
import co.mscp.StringUtil;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Pattern;



public class CrudClient<T> {
    
    public static final String SIZE = "size";
    public static final String OFFSET = "offset";
    private static final Pattern PATH_SEGMENT_PATTERN = Pattern.compile(
        "[\\w-]+");
    

    private static URI build(URIBuilder builder) {
        try {
            return builder.build();
        } catch(URISyntaxException e) {
            throw new Error("This should not have happened", e);
        }
    }
    private final String host;
    private final Class<T> typeReference;
    private final CloseableHttpClient client;
    
    
    private CrudClient(
        String host,
        Class<T> cls,
        CloseableHttpClient client)
    {
        this.client = client;
        
        if(!host.startsWith("http")) {
            host = "http://" + host;
        }
        
        this.host = host.replaceFirst("/$", "");
        
        try {
            new URI(host);
        } catch(URISyntaxException e) {
            throw new MonitoredError(e);
        }
        
        this.typeReference = cls;
    }
    
    
    public CrudClient(String host, Class<T> cls) {
        this(host, cls, HttpClients.createDefault());
    }
    
    
    public CrudClient<T> subClient(String path) {
        return subClient(path, typeReference);
    }
    
    public <U > CrudClient<U> subClient(
        String path,
        Class<U> cls)
    {
        if(!PATH_SEGMENT_PATTERN.matcher(path).find()) {
            throw new MonitoredError("Invalid path segment: " + path);
        }
        
        return new CrudClient<U>(host + "/" + path, cls, client);
    }
    
    
    private URIBuilder uri(String path) {
        try {
            return new URIBuilder(host + path);
        } catch(URISyntaxException e) {
            throw new MonitoredError(e);
        }
    }
    
    
    protected <E> E performRequest(
        HttpUriRequest request,
        Class<E> cls, Class<?> genericParam) throws HttpError
    {
        try(CloseableHttpResponse response = client.execute(request)) {
            final int code = response.getStatusLine().getStatusCode();
            if(code == HttpStatus.SC_OK || code == HttpStatus.SC_CREATED) {
                String content = EntityUtils.toString(response.getEntity(),
                    StandardCharsets.UTF_8.name());
                
                if(genericParam == null) {
                    return Json.parse(content, cls);
                }
                return Json.parse(content, cls, genericParam);
            }
            
            final HttpErrorStatus status = HttpErrorStatus.of(code);
            if(status != null) {
                final ContentType contentType = ContentType.get(
                    response.getEntity());
                if(contentType != null
                    && contentType.getMimeType()
                    .equalsIgnoreCase("application/json")) {
                    String content = EntityUtils.toString(response.getEntity(),
                        StandardCharsets.UTF_8.name());

                    HttpErrorModel errModel = Json.parse(content,
                        HttpErrorModel.class);
                    throw errModel.toHttpError();
                }
                throw new HttpError(status);
            }
            
            throw new HttpError(HttpErrorStatus.SERVICE_UNAVAILABLE,
                "Response code returned by server is invalid: " + code);
            
        } catch(IOException e) {
            throw new HttpError(HttpErrorStatus.SERVICE_UNAVAILABLE, e);
        }
    }
    
    
    public String getHost() {
        return host;
    }
    
    
    public Class<T> getEntityType() {
        return typeReference;
    }
    
    
    public T read() throws HttpError {
        return performRequest(new HttpGet(host), typeReference, null);
    }
    
    
    public T read(String id) throws HttpError {
        return performRequest(new HttpGet(host + "/" + id), typeReference,
            null);
    }
    
    
    public PartialList<T> read(int offset, int size) throws HttpError {
        return read(offset, size, null);
    }
    
    
    @SuppressWarnings("unchecked")
    public PartialList<T> read(int offset, int size, Map<String, String> params)
        throws HttpError
    {
        URIBuilder builder = uri("")
            .addParameter(SIZE, Integer.toString(size))
            .addParameter(OFFSET, Integer.toString(offset));
        
        if(params != null) {
            params.forEach((k, v) -> builder.addParameter(k, v));
        }
        
        return performRequest(new HttpGet(build(builder)), PartialList.class,
            typeReference);
    }
    
    
    public T create(T newEntity) throws HttpError {
        HttpPost request = new HttpPost(host);
        request.setEntity(new StringEntity(Json.toString(newEntity),
            ContentType.APPLICATION_JSON));
        return performRequest(request, typeReference, null);
    }
    
    
    public T update(String id, T updatedEntity) throws HttpError {
        HttpPut request = new HttpPut(host + "/" + id);
        request.setEntity(new StringEntity(Json.toString(updatedEntity),
            ContentType.APPLICATION_JSON));
        return performRequest(request, typeReference, null);
    }
    
    
    public T update(T updatedEntity) throws HttpError {
        HttpPut request = new HttpPut(host);
        request.setEntity(new StringEntity(Json.toString(updatedEntity),
            ContentType.APPLICATION_JSON));
        return performRequest(request, typeReference, null);
    }
    
    
    public T update(String id, T updatedEntity, Map<String, String> params)
        throws HttpError
    {
        URIBuilder builder = uri("/" + id);
        
        if(params != null) {
            params.forEach(builder::addParameter);
        }
        
        HttpPut request = new HttpPut(build(builder));
        request.setEntity(new StringEntity(Json.toString(updatedEntity),
            ContentType.APPLICATION_JSON));
        return performRequest(request, typeReference, null);
    }
    
    
    public T updateProperty(String id, String property, String value)
        throws HttpError
    {
        HttpPut request = new HttpPut(
            host + "/" + id + "/" + property + "/" + StringUtil.encodeUrl(value));
        return performRequest(request, typeReference, null);
    }
    
    
    public T delete(String id) throws HttpError {
        HttpDelete request = new HttpDelete(host + "/" + id);
        return performRequest(request, typeReference, null);
    }
}

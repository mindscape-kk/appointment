package co.mscp.mmk2.net;


import co.mscp.mmk2.Json;
import co.mscp.mmk2.collection.PartialList;
import co.mscp.mmk2.logging.Logger;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
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

    // --- STATIC FIELDS --- //

    private static final String SIZE = "size";
    private static final String OFFSET = "offset";
    private static final Pattern PATH_SEGMENT_PATTERN = Pattern.compile(
        "[\\w-]+");
    


    // --- STATIC METHODS --- //

    private static URI build(URIBuilder builder) {
        try {
            return builder.build();
        } catch(URISyntaxException e) {
            throw new Error("This should not have happened", e);
        }
    }



    // --- FIELDS --- //

    private final String host;
    private final Class<T> typeReference;
    private final CloseableHttpClient client;
    


    // --- CONSTRUCTORS --- //

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
            throw new RuntimeException(e);
        }
        
        this.typeReference = cls;
    }
    
    
    public CrudClient(String host, Class<T> cls) {
        this(host, cls, HttpClients.createDefault());
    }
    


    // --- METHODS --- //

    public CrudClient<T> subClient(String path) {
        return subClient(path, typeReference);
    }
    
    public <U > CrudClient<U> subClient(
        String path,
        Class<U> cls)
    {
        if(!PATH_SEGMENT_PATTERN.matcher(path).find()) {
            throw new RuntimeException("Invalid path segment: " + path);
        }
        
        return new CrudClient<U>(host + "/" + path, cls, client);
    }
    
    
    private URIBuilder uri(String path) {
        try {
            return new URIBuilder(host + path);
        } catch(URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    protected <E> E performRequest(HttpUriRequest request,Class<E> cls,
       Class<?> genericParam) throws ServiceError, IOException
    {
        try(CloseableHttpResponse response = client.execute(request)) {
            final StatusLine status = response.getStatusLine();
            final int code = status.getStatusCode();
            if(code == HttpStatus.SC_OK || code == HttpStatus.SC_CREATED) {
                String content = EntityUtils.toString(response.getEntity(),
                    StandardCharsets.UTF_8.name());
                
                if(genericParam == null) {
                    return Json.parse(content, cls);
                }
                return Json.parse(content, cls, genericParam);
            }
            
            final ContentType contentType = ContentType.get(
                response.getEntity());

            final String content = EntityUtils.toString(response.getEntity(),
                    StandardCharsets.UTF_8.name());

            if(contentType != null
                && contentType.getMimeType().equalsIgnoreCase("application/json"))
            {
                throw ServiceError.of(content);
            }

            Logger.of(this).info(content);

            throw ServiceError.clientError(
                String.format("%d %s", code, status.getReasonPhrase()));
        }
    }
    
    
    public String getHost() {
        return host;
    }
    
    
    public Class<T> getEntityType() {
        return typeReference;
    }
    
    
    public T read() throws ServiceError, IOException {
        return performRequest(new HttpGet(host), typeReference, null);
    }
    
    
    public T read(String id)
        throws ServiceError, IOException
    {
        return performRequest(new HttpGet(host + "/" + id), typeReference,
            null);
    }
    
    
    public PartialList<T> read(int offset, int size)
        throws ServiceError, IOException
    {
        return read(offset, size, null);
    }
    
    
    @SuppressWarnings("unchecked")
    public PartialList<T> read(int offset, int size, Map<String, String> params)
        throws ServiceError, IOException
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
    
    
    public T create(T newEntity) throws ServiceError, IOException {
        return create(newEntity, null);
    }


    public T create(T newEntity, Map<String, String> params) throws ServiceError, IOException {
        URIBuilder builder = uri("");

        if(params != null) {
            params.forEach(builder::addParameter);
        }

        HttpPost request = new HttpPost(build(builder));
        String content = Json.toString(newEntity);
        request.setEntity(new StringEntity(content,
                ContentType.APPLICATION_JSON));

        return performRequest(request, typeReference, null);
    }


    public T update(String id, T updatedEntity) throws ServiceError, IOException
    {
        HttpPut request = new HttpPut(host + "/" + id);
        request.setEntity(new StringEntity(Json.toString(updatedEntity),
            ContentType.APPLICATION_JSON));
        return performRequest(request, typeReference, null);
    }
    
    
    public T update(T updatedEntity, Map<String, String> params) throws ServiceError, IOException
    {
        URIBuilder builder = uri("");

        if(params != null) {
            params.forEach(builder::addParameter);
        }

        HttpPut request = new HttpPut(build(builder));

        request.setEntity(new StringEntity(Json.toString(updatedEntity),
            ContentType.APPLICATION_JSON));
        return performRequest(request, typeReference, null);
    }

    public T update(T updatedEntity) throws ServiceError, IOException
    {

        return update(updatedEntity, null);
    }
    
    
    public T update(String id, T updatedEntity, Map<String, String> params)
        throws ServiceError, IOException
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
        throws ServiceError, IOException
    {
        HttpPut request = new HttpPut(
            host + "/" + id + "/" + property + "/" + NetworkUtil.encodeUrl(value));
        return performRequest(request, typeReference, null);
    }
    
    
    public T delete(String id) throws ServiceError, IOException {
        HttpDelete request = new HttpDelete(host + "/" + id);
        return performRequest(request, typeReference, null);
    }
}

package co.mscp.mmk2.net;

import co.mscp.mmk2.Json;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class ServiceError extends RuntimeException {

    // --- NESTED TYPES --- //

    public enum Code {
        BAD_URL("URL is invalid"),
        BAD_AUTHORIZATION("Client is not authorized to perform this request"),
        BAD_ID("There is no entity with provided ID"),
        INVALID_PARAMETERS("There are invalid parameters in URL"),
        INVALID_ENTITY("Data provided by client failed validation"),
        UNATTAINABLE_STATE("Fulfilling the request is not feasible"),
        INTERNAL_SERVER_ERROR("Abnormal server error"),
        CLIENT_ERROR("Abnormal client error");

        public final String description;

        Code(String description) {
            this.description = description;
        }
    }


    public final static class Data {
        private Data(){
            this(null,null,null,null,null);
        }

        public static Data of(String json) throws IOException {
            return Json.parse(json, Data.class);
        }

        public final Code code;
        public final String description;
        public final String id;
        public final String entityType;
        public final Collection<ValidationIssue> issues;

        public Data(Code code, String description, String id, String entityType,
            Collection<ValidationIssue> issues)
        {
            this.code = code;
            this.description = description;
            this.id = id;
            this.entityType = entityType;
            this.issues = issues==null
                ?null
                :Collections.unmodifiableCollection(issues);
        }

        public String toJson() {
            return Json.toString(this);
        }

        public String toString() {
            return Json.toPrettyFormattedString(this);
        }
    }



    // --- STATIC METHODS --- //

    public static ServiceError badUrl(String url) {
        return new ServiceError(new Data(Code.BAD_URL, "URL is invalid: " + url,
            null, null, ValidationIssue.one("url", url, "resolvable")));
    }


    public static ServiceError badAuthorization(CrudAction method, Class<?> entityType, String id) {
        String description = "Client is not authorized to perform "
            + method.name() + " operation on " + entityType.getSimpleName();

        if(id != null) {
            description += " with id " + id;
        }

        return new ServiceError(new Data(Code.BAD_AUTHORIZATION, description, id,
            entityType.getSimpleName(), null));
    }


    public static ServiceError badAuthorization(CrudAction method, Class<?> entityType) {
        return badAuthorization(method, entityType, null);
    }


    public static ServiceError badId(Class<?> entityType, String id) {
        return new ServiceError(new Data(Code.BAD_ID,
            "No instance of entity of type " + entityType.getSimpleName()
                    + " with ID " + id,
            id, entityType.getSimpleName(), null));
    }


    public static ServiceError invalidParameters(Collection<ValidationIssue> issues) {
        return new ServiceError(new Data(Code.INVALID_PARAMETERS,
            "Request URL contains invalid parameters", null, null, issues));
    }


    public static ServiceError invalidEntity(Class<?> entityType, Collection<ValidationIssue> issue) {
        String description = "Entity of type " + entityType.getSimpleName()
            + " provided by client failed validation";

        return new ServiceError(new Data(Code.INVALID_ENTITY,
            description, null, entityType.getSimpleName(), issue));
    }


    public static ServiceError unattainableState(Class<?> entityType,
        String description)
    {
        return new ServiceError(new Data(Code.UNATTAINABLE_STATE, description,
            entityType.getSimpleName(), null, null));
    }


    public static ServiceError internal(String description) {
        return new ServiceError(new Data(Code.INTERNAL_SERVER_ERROR,
            description, null, null, null));
    }


    public static ServiceError internal(Throwable th) {
        return new ServiceError(new Data(Code.INTERNAL_SERVER_ERROR,
            th.getMessage(), null, null, null));
    }


    public static ServiceError clientError(String description) {
        return new ServiceError(new Data(Code.CLIENT_ERROR, description,
            null, null, null));
    }


    public static ServiceError of(String json) throws IOException {
        return new ServiceError(Data.of(json));
    }



    // --- FIELDS --- //

    private final Data data;



    // --- CONSTRUCTORS --- //

    private ServiceError(ServiceError.Data data) {
        super(data.description);
        this.data = data;
    }


    // --- METHODS --- //

    public Data getData() {
        return data;
    }


    public String toJson() {
        return data.toJson();
    }

}

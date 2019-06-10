package co.mscp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;



public class Json {

    private static final ObjectMapper MAPPER = new ObjectMapper();


    public static <T> T parse(String str, Class<T> cls, Class<?> genericArg)
            throws IOException
    {
        JavaType typeRef = Json.MAPPER.getTypeFactory()
                .constructParametricType(cls, genericArg);
        return MAPPER.readValue(str, typeRef);
    }


    public static <T> T parse(String str, Class<T> cls) throws IOException {
        return MAPPER.readValue(str, cls);
    }


    public static <T> T parse(File f, Class<T> cls, Class<?> genericArg)
            throws IOException
    {
        JavaType typeRef = Json.MAPPER.getTypeFactory()
                .constructParametricType(cls, genericArg);
        return MAPPER.readValue(f, typeRef);
    }


    public static <T> T parse(File f, Class<T> cls) throws IOException {
        return MAPPER.readValue(f, cls);
    }


    public static <T> T parse(URL url, Class<T> cls) throws IOException {
        return MAPPER.readValue(url, cls);
    }


    public static <T> T parse(URL url, Class<T> cls, Class<?> genericArg)
            throws IOException
    {
        JavaType typeRef = Json.MAPPER.getTypeFactory()
                .constructParametricType(cls, genericArg);
        return MAPPER.readValue(url, typeRef);
    }


    public static void printObject(Object obj) {
        try {
            System.out.println(MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(obj));
        } catch(JsonProcessingException e) {
            System.out.println("Error printing object as JSON.");
        }
    }


    public static String toPrettyFormattedString(Object obj) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(obj);
        } catch(JsonProcessingException e) {
            throw new Error(e);
        }
    }


    public static String toString(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch(JsonProcessingException e) {
            throw new Error(e);
        }
    }





}
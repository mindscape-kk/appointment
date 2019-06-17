package co.mscp.mmk2.net;

import java.util.Collection;
import java.util.Collections;

public final class ValidationIssue {

    // --- STATIC METHODS --- //

    public static Collection<ValidationIssue> one(String field, String value,
        String constraint)
    {
        return Collections.singleton(
            new ValidationIssue(field, value, constraint));
    }


    // --- FIELDS --- //

    public final String field;
    public final String value;
    public final String constraint;


    // --- CONSTRUCTORS --- //

    public ValidationIssue(String field, String value, String constraint) {
        this.field = field;
        this.value = value;
        this.constraint = constraint;
    }
}

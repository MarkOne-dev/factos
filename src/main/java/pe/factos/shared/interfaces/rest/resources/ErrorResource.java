package pe.factos.shared.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Standard error response resource returned from REST endpoints.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResource(
        String code,
        String message,
        String details) {

    public ErrorResource(String code, String message) {
        this(code, message, null);
    }
}

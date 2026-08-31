package pe.factos.shared.application.result;

/**
 * Represents an error that occurred in the application layer.
 * Designed to be easily mapped to HTTP responses and provide structured error information.
 *
 * @param code    A machine-readable error code (e.g., "COMPROBANTE_NOT_FOUND")
 * @param message A human-readable error message
 * @param details Optional additional context about the error
 */
public record ApplicationError(
        String code,
        String message,
        String details) {

    public ApplicationError(String code, String message) {
        this(code, message, null);
    }

    public static ApplicationError validationError(String fieldOrConcept, String reason) {
        return new ApplicationError(
                "VALIDATION_ERROR",
                "Validation failed: %s".formatted(fieldOrConcept),
                reason);
    }

    public static ApplicationError notFound(String resourceType, String identifier) {
        return new ApplicationError(
                "%s_NOT_FOUND".formatted(resourceType.toUpperCase()),
                "%s not found: %s".formatted(resourceType, identifier),
                null);
    }

    public static ApplicationError businessRuleViolation(String rule, String reason) {
        return new ApplicationError(
                "BUSINESS_RULE_VIOLATION",
                "Business rule violation: %s".formatted(rule),
                reason);
    }

    public static ApplicationError conflict(String resource, String reason) {
        return new ApplicationError(
                "%s_CONFLICT".formatted(resource.toUpperCase()),
                "Conflict with %s".formatted(resource),
                reason);
    }

    public static ApplicationError unexpected(String context, String reason) {
        return new ApplicationError(
                "UNEXPECTED_ERROR",
                "Unexpected error in %s".formatted(context),
                reason);
    }
}

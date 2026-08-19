package pe.factos.billing.domain.port;

public record CpeSubmissionResult(
        boolean accepted,
        String cdrDescription,
        String errorCode,
        String errorMessage,
        byte[] signedXml,
        byte[] cdrZip
) {
    public static CpeSubmissionResult success(String cdrDescription, byte[] signedXml, byte[] cdrZip) {
        return new CpeSubmissionResult(true, cdrDescription, null, null, signedXml, cdrZip);
    }

    public static CpeSubmissionResult failure(String errorCode, String errorMessage, byte[] signedXml) {
        return new CpeSubmissionResult(false, null, errorCode, errorMessage, signedXml, null);
    }
}

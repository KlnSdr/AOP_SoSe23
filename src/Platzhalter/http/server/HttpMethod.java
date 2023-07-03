package Platzhalter.http.server;

public enum HttpMethod {
    GET, POST, UNSUPPORTED_METHOD;

    /**
     * Convert a String to the appropriate HTTP method
     *
     * @param raw The string containing the name of the used method
     * @return The HttpMethod values of the input or UNSUPPORTED_METHOD for unknown/unhandled methods
     */
    public static HttpMethod fromString(String raw) {
        for (HttpMethod method : HttpMethod.values()) {
            if (method.name().equalsIgnoreCase(raw)) {
                return method;
            }
        }
        return UNSUPPORTED_METHOD;
    }
}

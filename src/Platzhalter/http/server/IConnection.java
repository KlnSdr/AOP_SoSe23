package Platzhalter.http.server;

import com.sun.net.httpserver.Headers;
import Platzhalter.http.Json;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IConnection {
    /**
     * Set the HTTP status code for the response
     *
     * @param code The HTTP code to send with the request
     */
    void setResponseCode(ResponseCode code);

    /**
     * Get the extracted JSON object representing the requests POST body
     *
     * @return the body
     */
    Json getRequestBody();

    /**
     * Get the query parameters of a URL
     *
     * @return A Map containing the keys and values of the query
     */
    Map<String, List<String>> getUriParams();

    String getPath();

    /**
     * Construct and send a response with an empty body to the client
     *
     * @throws IOException the output stream is unavailable
     */
    void sendResponse() throws IOException;

    /**
     * Construct and send a response containing JSON to the client
     *
     * @param payload The JSON payload to send to the client
     * @throws IOException the output stream is unavailable
     */
    void sendResponse(Json payload) throws IOException;

    Headers getHeaders();

    boolean hasHeader(String key);

    Optional<List<String>> getHeader(String key);
}

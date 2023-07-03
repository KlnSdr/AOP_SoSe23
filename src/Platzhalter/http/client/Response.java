package Platzhalter.http.client;

import Platzhalter.http.Json;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;

public class Response {
    private Json body;
    private int statusCode;
    private HttpHeaders headers;

    /**
     * Creates a new Response object with the given body, status code and headers.
     *
     * @param httpResponse the {@link HttpResponse} object to be converted to a Response object
     */
    public static Response buildResponse(HttpResponse<String> httpResponse) {
        Response res = new Response();
        res.body = Json.parse(httpResponse.body());
        res.statusCode = httpResponse.statusCode();
        res.headers = httpResponse.headers();
        return res;
    }

    /**
     * Returns the status code of the response.
     *
     * @return the status code of the response
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns the body of the response.
     *
     * @return the body of the response
     */
    public Json getBody() {
        return body;
    }

    /**
     * Returns the headers of the response.
     *
     * @return the headers of the response
     */
    public HttpHeaders getHeaders() {
        return headers;
    }
}

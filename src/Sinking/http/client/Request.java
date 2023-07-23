package Sinking.http.client;

import Sinking.http.Json;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Request {
    private final String url;
    private final HashMap<String, String> headers = new HashMap<>();
    private final HashMap<String, String> query = new HashMap<>();
    private final Json reqBody = new Json();
    private String route = "";

    /**
     * Creates a new Request object with the given url.
     *
     * @param url the url to which the request will be sent
     */
    public Request(String url) {
        this.url = url;
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
    }

    /**
     * Sets a key in the request body to the provided value
     *
     * @param key   the key of the body
     * @param value the value of the body
     */
    public void setBody(String key, String value) {
        this.reqBody.set(key, value);
    }

    /**
     * Sets the route to which the request will be sent.
     *
     * @param route the route to which the request will be sent
     */
    public void setRoute(String route) {
        this.route = route;
    }

    /**
     * Sets a header in the request to the provided value.
     *
     * @param key   the key of the header
     * @param value the value of the header
     */
    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    /**
     * Sets a query parameter in the request to the provided value.
     *
     * @param key   the key of the query parameter
     * @param value the value of the query parameter
     */
    public void setQuery(String key, String value) {
        this.query.put(key, value);
    }

    /**
     * Builds a {@link HttpRequest} object from this
     *
     * @return the built request
     */
    public HttpRequest buildGet() {
        HttpRequest.Builder builder = build();
        return builder.build();
    }

    /**
     * Builds a {@link HttpRequest} object from this and adds the request body to it
     *
     * @return the built request
     */
    public HttpRequest buildPost() {
        HttpRequest.Builder builder = build();
        return builder.POST(HttpRequest.BodyPublishers.ofString(reqBody.toString())).build();
    }

    private HttpRequest.Builder build() {
        HttpRequest.Builder req = HttpRequest.newBuilder();

        StringBuilder rawQuery = new StringBuilder();
        for (String key : query.keySet()) {
            rawQuery.append(URLEncoder.encode(key, StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(query.get(key), StandardCharsets.UTF_8)).append("&");
        }
        if (rawQuery.length() > 0) {
            rawQuery = new StringBuilder(rawQuery.substring(0, rawQuery.length() - 1));
        }

        req.uri(URI.create(this.url + this.route + (rawQuery.length() > 0 ? "?" + rawQuery : "")));
        req.version(HttpClient.Version.HTTP_1_1);
        for (String key : headers.keySet()) {
            req.header(key, headers.get(key));
        }

        return req;
    }
}

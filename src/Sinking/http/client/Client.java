package Sinking.http.client;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class Client {
    private final HttpClient httpClient;
    private final String url;

    /**
     * Creates a new Http Client with the given url.
     *
     * @param url the base url to which requests will be sent
     */
    public Client(String url) {
        httpClient = HttpClient.newHttpClient();
        this.url = url;
    }

    /**
     * Creates a new Http Client with the given url and the given timeout in ms.
     *
     * @param url     the base url to which requests will be sent
     * @param timeout the request timeout in ms
     */
    public Client(String url, int timeout) {
        httpClient = HttpClient.newBuilder().connectTimeout(java.time.Duration.ofMillis(timeout)).build();
        this.url = url;
    }

    /**
     * Sends a GET request to the given route.
     *
     * @param request the {@link Request} object containing the request to be sent
     * @param handler the handler to handle the response
     */
    public void get(Request request, IResponseHandler handler) {
        sendRequest(request.buildGet(), handler);
    }

    public void get(Request request, IResponseHandler handler, IRequestErrorHandler errorHandler) {
        sendRequest(request.buildGet(), handler, errorHandler);
    }

    /**
     * Sends a POST request to the given route.
     *
     * @param request the {@link Request} object containing the request to be sent
     * @param handler the handler to handle the response
     */
    public void post(Request request, IResponseHandler handler) {
        sendRequest(request.buildPost(), handler);
    }

    public void post(Request request, IResponseHandler handler, IRequestErrorHandler errorHandler) {
        sendRequest(request.buildPost(), handler, errorHandler);
    }

    private void sendRequest(HttpRequest request, IResponseHandler handler) {
        sendRequest(request, handler, error -> {
        });
    }

    private void sendRequest(HttpRequest request, IResponseHandler handler, IRequestErrorHandler errorHandler) {
        Consistency.getInstance().incrementRequestCount();
        try {
            CompletableFuture<HttpResponse<String>> responseFuture = this.httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            responseFuture.thenAccept(response -> {
                Response res = Response.buildResponse(response);
                handler.handle(res);
            }).join();
        } catch (Exception e) {
            Consistency.getInstance().reportFailedRequest();
            errorHandler.handle(e);
        }
    }

    /**
     * Creates a new {@link Request} object with the given route.
     *
     * @param route the route to which the request will be sent
     * @return a new {@link Request} object
     */
    public Request newRequest(String route) {
        Request req = new Request(this.url);
        req.setRoute(route);
        return req;
    }
}

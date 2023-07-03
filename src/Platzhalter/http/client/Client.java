package Platzhalter.http.client;

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
     * Sends a GET request to the given route.
     *
     * @param request the {@link Request} object containing the request to be sent
     * @param handler the handler to handle the response
     */
    public void get(Request request, IResponseHandler handler) {
        sendRequest(request.buildGet(), handler);
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

    private void sendRequest(HttpRequest request, IResponseHandler handler) {
        CompletableFuture<HttpResponse<String>> responseFuture = this.httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        responseFuture.thenAccept(response -> {
            Response res = Response.buildResponse(response);
            handler.handle(res);
        }).join();
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

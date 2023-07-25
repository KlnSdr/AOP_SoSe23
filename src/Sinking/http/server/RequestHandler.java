package Sinking.http.server;

import Sinking.http.Json;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

class RequestHandler implements HttpHandler {
    private final Map<HttpMethod, IRequestHandler> handlers = new HashMap<>();
    private final String path;

    /**
     * Create a new RequestHandler for a specific path
     *
     * @param path the path to handle
     */
    public RequestHandler(String path) {
        this.path = path;
    }

    /**
     * Handle a request
     *
     * @param httpExchange the {@link HttpExchange} object
     * @throws IOException the output stream is unavailable
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Connection connection = new Connection(httpExchange);
        String methodString = httpExchange.getRequestMethod().toUpperCase();
        HttpMethod method = HttpMethod.fromString(methodString);

        if (connection.getPath().equalsIgnoreCase(this.path) && this.handlers.containsKey(method)) {
            try {
                this.handlers.get(method).handle(connection);
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException |
                     NoSuchMethodException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            connection.setResponseCode(ResponseCode.NOT_FOUND);
            Json responsePayload = new Json();
            responsePayload.set("msg", "Handler for '" + methodString + " " + connection.getPath() + "' not found!");
            connection.sendResponse(responsePayload);
        }
    }

    /**
     * Add a handler for a specific HTTP method
     *
     * @param method  the {@link HttpMethod} to handle
     * @param handler the handler to use
     */
    public void addMethod(HttpMethod method, IRequestHandler handler) {
        if (handlers.containsKey(method)) {
            System.out.println("WARN: method " + method + " will be overwritten for " + this.path);
        }

        this.handlers.put(method, handler);
    }
}

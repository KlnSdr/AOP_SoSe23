package Sinking.http.server;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private final HttpServer server;
    private final int port;
    private final Map<String, RequestHandler> handlerMap = new HashMap<>();
    private final Map<String, HttpContext> contextMap = new HashMap<>();
    private boolean running = false;

    /**
     * Create a new server
     *
     * @param port the port to listen on
     * @throws IOException the port is already in use
     */
    public Server(int port) throws IOException {
        this.port = port;
        server = HttpServer.create(new InetSocketAddress(this.port), 1);
    }

    /**
     * Add a route to the server
     *
     * @param method  the {@link HttpMethod} to handle
     * @param path    the path to handle
     * @param handler the handler to use
     */
    public void addRoute(HttpMethod method, String path, IRequestHandler handler) {
        HttpContext context;
        RequestHandler reqHandler;

        if (contextMap.containsKey(path)) {
            reqHandler = handlerMap.get(path);
            reqHandler.addMethod(method, handler);
        } else {
            context = server.createContext(path);
            reqHandler = new RequestHandler(path);
            reqHandler.addMethod(method, handler);
            context.setHandler(reqHandler);

            contextMap.put(path, context);
            handlerMap.put(path, reqHandler);
        }
    }

    /**
     * Start the server
     */
    public void run() {
        if (!running) {
            server.start();
            System.out.println("server running on port " + port + "...");
            running = true;
        }
    }

    /**
     * Stop the server
     */
    public void stop() {
        if (running) {
            System.out.println("stopping server...");
            this.server.stop(5);
        }
    }
}

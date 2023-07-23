package Sinking.http.server;

import Sinking.http.HttpHandler;

import java.io.IOException;

public interface IRequestHandler extends HttpHandler<IConnection> {
    void handle(IConnection connection) throws IOException;
}

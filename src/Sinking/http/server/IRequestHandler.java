package Sinking.http.server;

import Sinking.http.HttpHandler;

public interface IRequestHandler extends HttpHandler<IConnection> {
    void handle(IConnection connection);
}

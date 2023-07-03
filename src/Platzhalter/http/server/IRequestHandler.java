package Platzhalter.http.server;

import Platzhalter.http.HttpHandler;

public interface IRequestHandler extends HttpHandler<IConnection> {
    void handle(IConnection connection);
}

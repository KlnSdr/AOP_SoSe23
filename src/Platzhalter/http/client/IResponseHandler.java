package Platzhalter.http.client;

import Platzhalter.http.HttpHandler;

public interface IResponseHandler extends HttpHandler<Response> {
    void handle(Response response);
}

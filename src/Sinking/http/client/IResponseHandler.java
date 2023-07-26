package Sinking.http.client;

import Sinking.http.HttpHandler;

public interface IResponseHandler extends HttpHandler<Response> {
    void handle(Response response);
}

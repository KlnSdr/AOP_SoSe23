package Sinking.http.client;

import Sinking.http.HttpHandler;

public interface IRequestErrorHandler extends HttpHandler<Exception> {
    void handle(Exception exception);
}

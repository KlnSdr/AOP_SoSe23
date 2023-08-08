package Sinking.http.server;

import Sinking.http.HttpHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface IRequestHandler extends HttpHandler<IConnection> {
    void handle(IConnection connection) throws IOException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException;
}

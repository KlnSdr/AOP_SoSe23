package Sinking.http.routes;

import Sinking.http.Json;
import Sinking.http.server.Annotations.Get;
import Sinking.http.server.Annotations.Post;
import Sinking.http.server.IConnection;
import Sinking.http.server.ResponseCode;

import java.io.IOException;

public class ExampleRoute {
    @Get(route = "/example")
    public void sendGreeting(IConnection connection) throws IOException {
        Json response = new Json();
        response.set("msg", "Hello World");
        connection.setResponseCode(ResponseCode.FORBIDDEN);
        connection.sendResponse(response);
    }

    @Post(route = "/example")
    public void pingPongBody(IConnection connection) throws IOException {
        Json response = connection.getRequestBody();
        connection.sendResponse(response);
    }
}

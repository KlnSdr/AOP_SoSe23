package Sinking.http;

import java.io.IOException;

public interface HttpHandler<T> {
    void handle(T t) throws IOException;
}

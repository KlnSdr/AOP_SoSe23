package Sinking.http;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface HttpHandler<T> {
    void handle(T t) throws IOException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException;
}

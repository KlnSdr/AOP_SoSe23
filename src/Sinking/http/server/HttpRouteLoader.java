package Sinking.http.server;

import Sinking.common.Classloader;
import Sinking.http.server.Annotations.Get;
import Sinking.http.server.Annotations.Post;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class HttpRouteLoader extends Classloader<Object> {
    private HttpRouteLoader(String packageName) {
        this.packageName = packageName;
    }

    public static void loadRoutes(String packageName, Server server) {
        HttpRouteLoader loader = new HttpRouteLoader(packageName);
        loader.loadClasses(packageName).forEach(clazz -> loader.analyzeClassAndMethods(clazz, server));
    }

    private void analyzeClassAndMethods(Class<?> clazz, Server server) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (!isValidHttpHandler(method)) {
                continue;
            }
            if (method.isAnnotationPresent(Get.class)) {
                Get annotation = method.getAnnotation(Get.class);
                server.addRoute(HttpMethod.GET, annotation.route(), (connection) -> method.invoke(clazz.getDeclaredConstructor().newInstance(), connection));
            } else if (method.isAnnotationPresent(Post.class)) {
                Post annotation = method.getAnnotation(Post.class);
                server.addRoute(HttpMethod.POST, annotation.route(), (connection) -> method.invoke(clazz.getDeclaredConstructor().newInstance(), connection));
            }
        }
    }

    private boolean isValidHttpHandler(Method method) {
        Type[] paramTypes = method.getGenericParameterTypes();
        return paramTypes.length == 1 && paramTypes[0] == IConnection.class;
    }

    @Override
    protected Class<?> filterClasses(String line) {
        return defaultClassFilter(line);
    }
}

package Sinking.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Classloader<T> {
    protected String packageName;

    protected Set<Class<? extends T>> loadClasses(String packageName) {
        InputStream istream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replace(".", "/"));
        if (istream == null) {
            throw new RuntimeException("Could not load views. Package " + packageName + " not found.");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(istream));

        Set<Class<? extends T>> clazzes = reader.lines().filter(line -> line.endsWith(".class")).map(this::filterClasses).collect(Collectors.toSet());
        clazzes = clazzes.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        return clazzes;
    }

    protected abstract Class<? extends T> filterClasses(String line);

    protected Class<?> defaultClassFilter(String line) {
        try {
            return Class.forName(packageName + "." + line.substring(0, line.lastIndexOf('.')/*remove ".class" from line*/));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

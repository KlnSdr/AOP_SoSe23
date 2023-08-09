package Sinking;

import Sinking.common.Classloader;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

public class AiLoader extends Classloader<IAi> {
    private static AiLoader instance;
    private final HashMap<String, Class<? extends IAi>> ais = new HashMap<>();

    private AiLoader() {
        this.packageName = "Sinking.Ai";
        this.loadClasses().forEach(aiRef -> {
            if (aiRef == null) {
                return;
            }
            try {
                IAi tmp = aiRef.getDeclaredConstructor().newInstance();
                this.ais.put(tmp.getName(), aiRef);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                // welp maybe not so valid ai, pass
            }
        });
    }

    public static AiLoader getInstance() {
        if (instance == null) {
            instance = new AiLoader();
        }
        return instance;
    }

    public Set<String> getAiNames() {
        return this.ais.keySet();
    }

    public IAi getInstanceOf(String name) {
        Class<? extends IAi> aiRef = this.ais.get(name);
        if (aiRef == null) {
            return null;
        }
        try {
            return aiRef.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            return null;
        }
    }

    @Override
    protected Class<? extends IAi> filterClasses(String line) {
        return defaultImplementsFilter(line, IAi.class);
    }
}

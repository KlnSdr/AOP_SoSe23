package Sinking.UI;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class ViewLoader {
    private static final String packageName = "Sinking.UI.views";
    private static ViewLoader instance;
    private final HashMap<String, Class<? extends IView>> views = new HashMap<>();
    private final Window window;
    private IView currentView;

    private ViewLoader() {
        discoverViews();
        this.window = new Window();
    }

    public static ViewLoader getInstance() {
        if (instance == null) {
            instance = new ViewLoader();
        }
        return instance;
    }

    // https://www.baeldung.com/java-find-all-classes-in-package
    private void discoverViews() {
        InputStream istream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replace(".", "/"));
        if (istream == null) {
            throw new RuntimeException("Could not load views. Package " + packageName + " not found.");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(istream));

        Set<Class<? extends IView>> clazzes = reader.lines().filter(line -> line.endsWith(".class")).map(this::getClass).collect(Collectors.toSet());

        clazzes.forEach(clazz -> {
            if (clazz == null) {
                return;
            }
            views.put(clazz.getSimpleName(), clazz);
        });
    }

    private Class<? extends IView> getClass(String line) {
        try {
            Class<?> clazz = Class.forName(packageName + "." + line.substring(0, line.lastIndexOf('.')/*remove ".class" from line*/));
            // check if the loaded class implements IView
            if (IView.class.isAssignableFrom(clazz)) {
                return clazz.asSubclass(IView.class);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadView(String name) {
        if (views.containsKey(name)) {
            try {
                if (currentView != null) {
                    currentView.unload();
                }
                this.window.getContentPane().removeAll();

                currentView = views.get(name).getConstructor().newInstance();
                SwingUtilities.invokeLater(() -> {
                    currentView.load(this.window);

                    this.window.invalidate();
                    this.window.validate();
                    this.window.repaint();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("View " + name + " not found.");
        }
    }
}

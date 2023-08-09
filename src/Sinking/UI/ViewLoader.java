package Sinking.UI;

import Sinking.common.Classloader;

import javax.swing.*;
import java.util.HashMap;
import java.util.Set;

public class ViewLoader extends Classloader<IView> {
    private static ViewLoader instance;
    private final HashMap<String, Class<? extends IView>> views = new HashMap<>();
    private final Window window;
    private IView currentView;

    private ViewLoader() {
        this.packageName = "Sinking.UI.views";
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
        Set<Class<? extends IView>> clazzes = loadClasses();
        clazzes.forEach(clazz -> {
            views.put(clazz.getSimpleName(), clazz);
        });
    }

    protected Class<? extends IView> filterClasses(String line) {
        return defaultImplementsFilter(line, IView.class);
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

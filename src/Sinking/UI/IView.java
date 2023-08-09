package Sinking.UI;

import Sinking.common.Json;

import javax.swing.*;

public interface IView {
    void load(JFrame window, Json data);

    void unload();
}

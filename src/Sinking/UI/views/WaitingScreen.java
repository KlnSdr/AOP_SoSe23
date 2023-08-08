package Sinking.UI.views;

import Sinking.UI.IView;
import Sinking.UI.ViewLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Sinking.UI.Window.baseTitle;

public class WaitingScreen implements IView {
    @Override
    public void load(JFrame window) {
        window.setTitle(baseTitle);
        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new GridBagLayout());
        centerContainer.setBackground(Color.WHITE);
        centerContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        window.add(centerContainer);

        String url = createNewOnlineGame();

        JLabel linkLabel = new JLabel(url);
        GridBagConstraints gbcLinkLabel = new GridBagConstraints();
        gbcLinkLabel.gridx = 0;
        gbcLinkLabel.gridy = 0;
        gbcLinkLabel.gridwidth = 2;
        gbcLinkLabel.anchor = GridBagConstraints.CENTER;
        gbcLinkLabel.insets = new Insets(0, 0, 15, 0);
        centerContainer.add(linkLabel, gbcLinkLabel);

        JButton copyButton = new JButton("Link kopieren");
        copyButton.setPreferredSize(new Dimension(200, 20));
        GridBagConstraints gbcCopyButton = new GridBagConstraints();
        gbcCopyButton.gridx = 0;
        gbcCopyButton.gridy = 1;
        gbcCopyButton.anchor = GridBagConstraints.CENTER;
        gbcCopyButton.insets = new Insets(0, 0, 30, 10);
        copyButton.addActionListener(e -> {
            System.out.println("Copy Link");
            copyURL(url);
        });
        centerContainer.add(copyButton, gbcCopyButton);

        JLabel waitLabel = new JLabel("... Warten auf 2. Spieler ...");
        GridBagConstraints gbcWaitLabel = new GridBagConstraints();
        gbcWaitLabel.gridx = 0;
        gbcWaitLabel.gridy = 2;
        gbcWaitLabel.gridwidth = 2; // Spannend über 2 Spalten
        gbcWaitLabel.anchor = GridBagConstraints.CENTER;
        gbcWaitLabel.insets = new Insets(0, 0, 15, 0);
        centerContainer.add(waitLabel, gbcWaitLabel);

        JButton backButton = new JButton("Zurück zum Hauptmenü");
        backButton.setPreferredSize(new Dimension(200, 20));
        GridBagConstraints gbcBackButton = new GridBagConstraints();
        gbcBackButton.gridx = 0;
        gbcBackButton.gridy = 3;
        gbcBackButton.anchor = GridBagConstraints.CENTER;
        gbcBackButton.insets = new Insets(0, 0, 15, 0);
        backButton.addActionListener(e -> {
            System.out.println("Loading Main Menu");
            ViewLoader.getInstance().loadView("MainMenu");
        });
        centerContainer.add(backButton, gbcBackButton);
    }

    //https://stackoverflow.com/questions/6710350/copying-text-to-the-clipboard-using-java
    private void copyURL(String url) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(url);
        clipboard.setContents(selection, null);

        JDialog dialog = new JDialog((Frame) null, "", true);
        dialog.setLayout(new FlowLayout());
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(null);

        JLabel label = new JLabel("Link erfolgreich kopiert.");
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = 0;
        gbcLabel.gridy = 0;
        gbcLabel.gridheight = GridBagConstraints.REMAINDER;
        gbcLabel.gridwidth = GridBagConstraints.REMAINDER;
        gbcLabel.anchor = GridBagConstraints.CENTER;
        dialog.add(label, gbcLabel);

        ActionListener timerAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        };
        Timer timer = new Timer(2000, timerAction);
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);
        System.out.println("Text wurde in den Zwischenspeicher kopiert.");
    }
    private String createNewOnlineGame() {
        //creates the link for joining the online game
        return "www.placeholder.de/AOP_SoSe23";
    }

    @Override
    public void unload() {System.out.println("Unloading WaitingScreen");}
}

package gm.swing;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class AboutDialog extends JDialog {
    private static final String infoUrl = "info/information.html";

    public AboutDialog(Frame owner) {
        super(owner, "关于这款游戏", true);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLocationRelativeTo(owner);
        this.setVisible(false);

        JPanel panel = new JPanel();

        File infoFile = new File(infoUrl);

        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(e -> {
            if(e.getEventType() ==  HyperlinkEvent.EventType.ACTIVATED){
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(e.getURL().toURI());
                } catch (IOException | URISyntaxException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        try {
            editorPane.setPage("file:" + infoFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }


        panel.add(editorPane);
        this.add(BorderLayout.CENTER, panel);
        this.pack();
    }
}

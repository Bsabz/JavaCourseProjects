package Penta.Framez;

import java.awt.*;
import java.util.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.*;

/**
 *
 * @author Brian Sabz
 */
public class PentaFrame extends JFrame {

    private PentaPanel pentaPanel;

    public PentaFrame() {
        super("Penta Frame");
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        layoutGUI();
    }

    public void layoutGUI() {
        pentaPanel = new PentaPanel();
        this.add(pentaPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        PentaFrame frame = new PentaFrame();
        frame.setVisible(true);
    }
}

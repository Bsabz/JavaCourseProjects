/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Penta.Framez;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author Brian Sabz
 */
public class PentaPanel extends JPanel {

    // EVENT HANDLERS
    private MouseHandler mouseHandler;
    private MouseMotionHandler mouseMotionHandler;

	// WE'LL USE THESE VECTORS FOR STORING THE VERTICES
    // AND COLORS OF OUR PENTAGONS
    private Vector<int[]> xPoints;
    private Vector<int[]> yPoints;
    private Vector<Color> colors;

    public PentaPanel() {
        // AND OUR PENTAGRAM VERTEX ARRAYS
        xPoints = new Vector<int[]>();
        yPoints = new Vector<int[]>();
        colors = new Vector<Color>();

		// LET'S LISTEN FOR MOUSE CLICKS 
        // AND DRAGGING ON THE PANEL
        mouseHandler = new MouseHandler();
        this.addMouseListener(mouseHandler);
        mouseMotionHandler = new MouseMotionHandler();
        this.addMouseMotionListener(mouseMotionHandler);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // DRAW ALL THE PENTAGONS
        for (int i = 0; i < xPoints.size(); i++) {
            int[] xVertices = xPoints.get(i);
            int[] yVertices = yPoints.get(i);
            Color color = colors.get(i);
            g.setColor(color);
            g.fillPolygon(xVertices, yVertices, 5);
            g.setColor(Color.black);
            g.drawPolygon(xVertices, yVertices, 5);
        }
    }

    class MouseHandler extends MouseAdapter {

        public void mouseClicked(MouseEvent me) {
            if (me.getClickCount() == 2) {
                xPoints.clear();
                yPoints.clear();
                colors.clear();
                repaint();
            }
        }
    }

    class MouseMotionHandler extends MouseMotionAdapter {

        public void mouseDragged(MouseEvent me) {
            // THE LOCATION OF WHERE WE'RE DRAGGING THE MOUSE
            int x = me.getX();
            int y = me.getY();

            // THESE WILL BE THE VERTICES
            int[] xs = new int[5];
            int[] ys = new int[5];

            // TOP CENTER POINT
            xs[0] = x;
            ys[0] = y - (int) (Math.random() * 20) - 1;

            // TOP-RIGHT POINT
            xs[1] = x + (int) (Math.random() * 15) + 1;
            ys[1] = y - (int) (Math.random() * 10) - 1;
            // BOTTOM-RIGHT POINT
            xs[2] = x + (int) (Math.random() * 10) + 1;
            ys[2] = y + (int) (Math.random() * 15) + 1;

            // BOTTOM-LEFT POINT
            xs[3] = x - (int) (Math.random() * 10) - 1;
            ys[3] = y + (int) (Math.random() * 15) + 1;

            // TOP-LEFT POINT
            xs[4] = x - (int) (Math.random() * 15) - 1;
            ys[4] = y - (int) (Math.random() * 10) - 1;

            xPoints.add(xs);
            yPoints.add(ys);
            int r = (int) (Math.random() * 256);
            int g = (int) (Math.random() * 256);
            int b = (int) (Math.random() * 256);
            colors.add(new Color(r, g, b));
            repaint();

        }
    }

}

/*
 * AudioSliceEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.asec;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;

/**
 *
 * @author desktopgame
 */
public class BasicAudioSliceEditorUI extends AudioSliceEditorUI {

    private AudioSliceEditor editor;
    private PropertyChangeHandler propertyChangeHandler;
    private MouseHandler mouseHandler;

    public BasicAudioSliceEditorUI() {
        this.propertyChangeHandler = new PropertyChangeHandler();
        this.mouseHandler = new MouseHandler();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c); //To change body of generated methods, choose Tools | Templates.
        this.editor = (AudioSliceEditor) c;
        editor.setPreferredSize(new Dimension(400, 200));
        editor.setBackground(Color.BLACK);
        editor.addMouseListener(mouseHandler);
        editor.addMouseMotionListener(mouseHandler);
        editor.addPropertyChangeListener(propertyChangeHandler);
    }

    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c); //To change body of generated methods, choose Tools | Templates.
        editor.removeMouseListener(mouseHandler);
        editor.removeMouseMotionListener(mouseHandler);
        editor.removePropertyChangeListener(propertyChangeHandler);
        this.editor = null;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c); //To change body of generated methods, choose Tools | Templates.
        Graphics2D g2 = (Graphics2D) g;
        Color col = g2.getColor();
        g2.setColor(editor.getBackground());
        g2.fillRect(0, 0, editor.getWidth(), editor.getHeight());

        int cy = getCenterY();
        int psi = getLeftPointX();
        int pei = getRightPointX();

        for (int i = 0; i < editor.getWidth(); i += 10) {
            g2.setColor(Color.GRAY);
            g2.drawLine(i, 0, i, editor.getHeight());
        }
        for (int i = 0; i < editor.getHeight(); i += 10) {
            g2.setColor(Color.GRAY);
            g2.drawLine(0, i, editor.getWidth(), i);
        }
        int ms = editor.getMarkerSize();
        g2.setColor(Color.GREEN);
        g2.drawLine(psi, cy, pei, cy);
        g2.setColor(Color.RED);
        g2.fillOval(psi, cy - (ms / 2), ms, ms);
        g2.fillOval(pei - ms, cy - (ms / 2), ms, ms);

        g2.setColor(Color.GREEN);
        if (psi != 0) {
            g2.drawLine(0, editor.getHeight(), psi, cy);
        }
        if (pei != editor.getWidth()) {
            g2.drawLine(pei, cy, editor.getWidth(), editor.getHeight());
        }

        g2.setColor(col);
    }

    private int getCenterY() {
        int my = 0;
        for (int i = 0; i < editor.getHeight(); i += 10) {
            my++;
        }
        int cy = 10 * (my / 2);
        return cy;
    }

    private int getLeftPointX() {
        float ps = editor.getPeakStartPosition();
        int psi = (int) (ps * (float) editor.getWidth());
        return psi;
    }

    private int getRightPointX() {
        float pe = editor.getPeakEndPosition();
        int pei = (int) (pe * (float) editor.getWidth());
        return pei;
    }

    private Rectangle getLeftRect() {
        Rectangle rect = new Rectangle();
        rect.x = getLeftPointX();
        rect.y = getCenterY() - (editor.getMarkerSize() / 2);
        rect.width = editor.getMarkerSize();
        rect.height = editor.getMarkerSize();
        return rect;
    }

    private Rectangle getRightRect() {
        Rectangle rect = new Rectangle();
        rect.x = getRightPointX() - editor.getMarkerSize();
        rect.y = getCenterY() - (editor.getMarkerSize() / 2);
        rect.width = editor.getMarkerSize();
        rect.height = editor.getMarkerSize();
        return rect;
    }

    private class PropertyChangeHandler implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent arg0) {
            String name = arg0.getPropertyName();
            if (name.equals("peakStartPosition") || name.equals("peakEndPosition") || name.equals("markerSize")) {
                editor.repaint();
            }
        }

    }

    private class MouseHandler extends MouseAdapter {

        private boolean drag;
        private int marker;

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e); //To change body of generated methods, choose Tools | Templates.
            float f = (float) e.getX() / (float) editor.getWidth();
            if (marker == -1) {
                editor.setPeakStartPosition(f);
            } else {
                editor.setPeakEndPosition(f);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e); //To change body of generated methods, choose Tools | Templates.
            this.drag = false;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e); //To change body of generated methods, choose Tools | Templates.
            if (getLeftRect().contains(e.getPoint())) {
                this.drag = true;
                this.marker = -1;
            } else if (getRightRect().contains(e.getPoint())) {
                this.drag = true;
                this.marker = 1;

            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e); //To change body of generated methods, choose Tools | Templates.
            if (getLeftRect().contains(e.getPoint()) || getRightRect().contains(e.getPoint())) {
                editor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                editor.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }

    }

}

package be.afhistos.discord.extra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WindowMover extends MouseAdapter {
    private Point clic;
    private JFrame frame;

    public WindowMover(JFrame frame){this.frame = frame;}

    @Override
    public void mouseDragged(MouseEvent e) {
        if(this.clic != null){
            Point draggedPoint = MouseInfo.getPointerInfo().getLocation();
            this.frame.setLocation((int)draggedPoint.getX() - (int)this.clic.getX(),(int)draggedPoint.getY() - (int)this.clic.getY());

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.clic = e.getPoint();
    }
}
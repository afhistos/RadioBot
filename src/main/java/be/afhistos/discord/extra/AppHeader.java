package be.afhistos.discord.extra;

import be.afhistos.discord.Main;
import be.afhistos.discord.ScannerThread;
import be.afhistos.discord.graphics.BotManagerPanel;
import be.afhistos.discord.graphics.Launch;
import be.afhistos.discord.graphics.LaunchPanel;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AppHeader implements MouseListener {
    private JButton close;//42 x 26
    private JButton minimize;//42 x 26
    private JLabel mover;
    public JPanel panel;
    private Boolean child;
    Color customColor = new Color(12,88,210);

    public AppHeader(JPanel panel, String title, Boolean centeredTitle, int width, Boolean child){
        this.panel = panel;
        this.child = child;
        this.close = new JButton("X");
        this.close.setBounds(child? width-42: width,0,this.close.getMinimumSize().width, this.close.getMinimumSize().height);
        this.close.addMouseListener(this);
        this.close.setBackground(customColor);
        this.close.setBorderPainted(false);
        this.close.setFocusPainted(false);
        if(!child){
            this.minimize = new JButton("_");
            this.minimize.setBounds(width - 42, 0, this.close.getMinimumSize().width, this.close.getMinimumSize().height);
            this.minimize.addMouseListener(this);
            this.minimize.setBackground(customColor);
            this.minimize.setBorderPainted(false);
            this.minimize.setFocusPainted(false);
            panel.add(minimize);
        }
        if(!centeredTitle) {
            this.mover = new JLabel("  " + title);
        }else{
            this.mover = new JLabel(title, SwingConstants.CENTER);
        }
        this.mover.setBackground(customColor);
        this.mover.setOpaque(true);
        this.mover.setBounds(0,0,width-42, this.close.getMinimumSize().height);
        panel.add(mover);
        panel.add(close);

    }



    @Override
    public void mouseClicked(MouseEvent e) {
        JComponent component = (JComponent) e.getSource();
        if(component.equals(close)) {
            if(component.getParent().equals(BotManagerPanel.getPing_panel())){
                BotManagerPanel.getPing_frame().dispose();
            } else if(component.getParent().equals(BotManagerPanel.getName_panel())){
                BotManagerPanel.getName_frame().dispose();
            }else if (component.getParent().equals(LaunchPanel.getInstance())) {
                System.out.println("Bot isn't started.");
                System.exit(0);
            }else {
                System.out.println("nothing found");
                Main.consoleCommand("stop");
            }
        }else if(component.equals(minimize)){
            Launch.getInstance().setState(Frame.ICONIFIED);
            minimize.setBackground(customColor);
            this.mouseEntered(e);
        }


    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        e.getComponent().setBackground(e.getComponent().getBackground().darker());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        e.getComponent().setBackground(e.getComponent().getBackground().brighter());
    }

    public JLabel getMover() {
        return mover;
    }

    public JButton getClose() {
        return close;
    }
}

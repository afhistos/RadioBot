package be.afhistos.discord.extra;

import javax.swing.*;
import java.awt.*;

public class BlueButton extends JButton {
    private Color blue = new Color(53,124,208);

    public BlueButton(String text, Rectangle size, Boolean setPainted){
        this.setBackground(blue);
        this.setText(text);
        this.setBounds(size);
        this.setBorderPainted(setPainted);
        this.setFocusPainted(setPainted);

    }
    public BlueButton(String text, int x, int y, Boolean setPainted){
        this.setBackground(blue);
        this.setText(text);
        this.setBounds(x, y,this.getMinimumSize().width,this.getMinimumSize().height);
        this.setBorderPainted(setPainted);
        this.setFocusPainted(setPainted);
    }

}

package be.afhistos.discord.extra;

import javax.swing.*;
import java.awt.*;

public class BlueSlider extends JSlider {
    private Color blue = new Color(53,124,208);

    public BlueSlider(int min, int max,Boolean painted, Boolean addBorder){
        this.setForeground(blue);
        this.setMinimum(min);
        this.setValue(85); //DefaultVolume
        this.setMaximum(max);
        this.setPaintLabels(painted);
        this.setPaintTicks(painted);
        this.setMajorTickSpacing(40);//Grand trait tout les x
        this.setMinorTickSpacing(5); //petit trait tout les x
        if(addBorder){
            this.setBorder(BorderFactory.createLineBorder(blue.darker()));
        }

    }
}

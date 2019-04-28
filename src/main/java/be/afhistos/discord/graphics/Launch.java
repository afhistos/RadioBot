package be.afhistos.discord.graphics;

import be.afhistos.discord.extra.Functions;
import be.afhistos.discord.extra.WindowMover;

import javax.swing.*;

public class Launch extends JFrame {
    private static Launch instance;
    private static WindowMover windowMover;

    public Launch(){
        windowMover = new WindowMover(this);
        this.setTitle("RadioBot - Starting . . .");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        this.setIconImage(Functions.getImage("icon_radiobot.png"));
        this.setSize(720,400);
        this.setVisible(true);
    }
    public static void main(String[] args) {instance = new Launch();}

}

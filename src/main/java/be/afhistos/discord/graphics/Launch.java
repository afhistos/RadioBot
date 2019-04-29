package be.afhistos.discord.graphics;

import be.afhistos.discord.extra.Functions;
import be.afhistos.discord.extra.WindowMover;

import javax.swing.*;

public class Launch extends JFrame {
    private static Launch instance;
    private static WindowMover windowMover;
    private LaunchPanel launchPanel;
    private MainPanel mainPanel;

    public Launch(){
        windowMover = new WindowMover(this);
        this.setTitle("RadioBot - Starting . . .");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLocation(this.getLocation().x-500, this.getLocation().y-350);
        this.setUndecorated(true);
        this.setIconImage(Functions.getImage("icon_radiobot.png"));
        this.setSize(1080,660);
        this.launchPanel = new LaunchPanel();
        this.mainPanel=  new MainPanel();
        this.setContentPane(this.launchPanel);
        this.setVisible(true);
    }
    public static void main(String[] args) {instance = new Launch();}

    public static Launch getInstance() {
        return instance;
    }
    public void hideWindow(Boolean hidden){
        getInstance().setVisible(!hidden);
        getInstance().setContentPane(launchPanel);
    }

    public LaunchPanel getLaunchPanel() {
        return launchPanel;
    }


    public static WindowMover getWindowMover() {
        return windowMover;
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }
}

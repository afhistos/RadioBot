package be.afhistos.discord.graphics;

import be.afhistos.discord.extra.Functions;
import be.afhistos.discord.extra.WindowMover;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordUser;

import javax.swing.*;

public class Launch extends JFrame {
    private static Launch instance;
    private static WindowMover windowMover;
    private static DiscordRPC lib;
    private String username;
    private LaunchPanel launchPanel;
    private MainPanel mainPanel;

    public Launch() {
        System.out.println("Started");
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
        LaunchPanel.setText("Connecting to discord . . .");
        lib = DiscordRPC.INSTANCE;
        String appId = "574202145289797643";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = ((discordUser) ->{
            this.username = discordUser.username+"#"+discordUser.discriminator;
            System.out.println("Connected to "+this.username);
            LaunchPanel.setText("Bienvenue "+this.username);
        });
        lib.Discord_Initialize(appId, handlers, true, null);
        DiscordRichPresence discordPresence = new DiscordRichPresence();
        discordPresence.state = "Playing 'Author'";
        discordPresence.details = "'Song name'";
        discordPresence.startTimestamp = 0;
        discordPresence.endTimestamp = 0;
        discordPresence.largeImageKey = "bassboost_png";
        discordPresence.largeImageText = "'Song Name";
        discordPresence.smallImageKey = "manager_png";
        discordPresence.smallImageText = "Manager  | 'Volume'";
        lib.Discord_UpdatePresence(discordPresence);
        LaunchPanel.setText("Connected! getting discord' data . . .");
        Thread discord = new Thread(() ->{
            this.setName("Discord-Callbacks-Handler");
            while (!Thread.currentThread().isInterrupted()){
                lib.Discord_RunCallbacks();
                try{
                    Thread.sleep(2000); //Update toutes les 2 secondes
                } catch (InterruptedException ign) {}
            }
        });
        discord.start();

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

    public static void updatePresence(DiscordRichPresence presence){
        lib.Discord_UpdatePresence(presence);
    }
}

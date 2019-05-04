package be.afhistos.discord;

import be.afhistos.discord.commands.*;
import be.afhistos.discord.graphics.LaunchPanel;
import be.afhistos.discord.music.Audio;
import be.afhistos.discord.news.News;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.Scanner;

public class Main implements Runnable{
    private static JDA jda;
    private static final CommandClientBuilder builder = new CommandClientBuilder();
    public static Scanner scanner = new Scanner(System.in);
    private static CommandClient client;
    private static Thread mainThread;
    public static Thread t;
    public static boolean running;

    public Main(String token) throws LoginException, InterruptedException{
        builder.setStatus(OnlineStatus.INVISIBLE);
        builder.setGame(Game.playing("Starting . . ."));
        builder.setPrefix(",");
        String[] features = {"Afficher les dernières nouvelles", "Jouer de la musique, controlée par MK_16", "*attends mais il va foutre la merde là*"};
        builder.addCommand(new CommandJoin());
        builder.addCommand(new CommandLeft());
        builder.addCommand(new CommandNews());
        builder.addCommand(new CommandListRadios());
        builder.addCommand(new CommandAbout(new Color(37,105,160), "Voici quelques infos à propos de moi", features));
        builder.setOwnerId("279597100961103872");
        builder.setEmojis("\u2705", "\u26a0", "\u274c");
        client = builder.build();
        jda = new JDABuilder(AccountType.BOT).setToken(token).addEventListener(new Audio()).addEventListener(client).addEventListener(new EventsListener()).buildAsync();
        jda.awaitReady();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
    }

    public static void main(String[] args) {
        try{
            LaunchPanel.setText("Démarrage du bot . . .");
             Main main = new Main(args[0]);
             mainThread = new Thread(main, "radioBot-Thread");
             mainThread.start();
             LaunchPanel.setText("Bot démarré sous le nom: "+mainThread.getName());
             System.out.println(mainThread.getName()+" started");
        } catch (LoginException e) {
            e.printStackTrace();
            LaunchPanel.setText("Erreur ! Token incorrect");
        } catch (InterruptedException e) {
            e.printStackTrace();
            LaunchPanel.setText("Erreur ! Le processus s'est brusquement arrêté.");
        }
        LaunchPanel.setText("Démarrage du scanner . . .");
        running = true;
        //Infos
        t = new ScannerThread();
        t.start();
        LaunchPanel.setText("Scanner démarré sous le nom: "+t.getName());
        System.out.println(t.getName()+" started");
        System.out.println("running from Main.java: "+running);
        News news = new News(args[1], args[2]);
        LaunchPanel.setText("Système de News démarré");
        System.out.println("Initialized news!");
    }

    public Boolean getRunning(){return this.running;}
    public static JDA getJda() {return jda;}
    public static CommandClientBuilder getBuilder() {return builder;}
    public static CommandClient getClient() {return client;}


    public static void consoleCommand(String command){
        if(command.equalsIgnoreCase("stop")){
            System.out.println("Stopping bot . . .");
            t.interrupt();
            running = false;
            mainThread.interrupt();
            System.out.println("RadioBot was properly shutdowned.");
            jda.shutdown();
            System.exit(0);
        }
    }

    @Override
    public void run() {

    }
}

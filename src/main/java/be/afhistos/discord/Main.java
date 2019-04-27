package be.afhistos.discord;

import be.afhistos.discord.commands.*;
import be.afhistos.discord.music.Audio;
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
    private Scanner scanner = new Scanner(System.in);
    private static CommandClient client;
    private static boolean running;

    public Main(String token) throws LoginException, InterruptedException{
        builder.setGame(Game.playing("Starting . . ."));
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.setPrefix(",");
        String[] features = {"Afficher les dernières nouvelles", "Jouer de la musique, controlée par MK_16", "*attends mais il va foutre la merde là*"};
        builder.addCommand(new CommandJoin());
        builder.addCommand(new CommandLeft());
        builder.addCommand(new CommandAbout(new Color(37,105,160), "Voici quelques infos à propos de moi", features));
        builder.setOwnerId("279597100961103872");
        builder.setEmojis("\u2705", "\u26a0", "\u274c");
        client = builder.build();
        jda = new JDABuilder(AccountType.BOT).setToken(token).addEventListener(new Audio()).addEventListener(client).addEventListener(new EventsListener()).buildAsync();
        jda.awaitReady();
        jda.getPresence().setGame(Game.playing("Prêt pour mettre le feu!"));
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
    }

    public static void main(String[] args) {
        try{
            Main main  = new Main(args[0]);
            new Thread(main, "radioBot-Thread").start();
            Thread.sleep(5000);
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void setRunning(boolean running) {Main.running = running;}
    public static Boolean getRunning(){return Main.running;}
    public static JDA getJda() {return jda;}
    public static CommandClientBuilder getBuilder() {return builder;}
    public static CommandClient getClient() {return client;}

    @Override
    public void run() {
        running = true;
        while (running){
            if(scanner.hasNextLine()){
                consoleCommand(scanner.nextLine());
            }
        }
        scanner.close();
        System.out.println("RadioBot was properly shutdowned.");
        jda.shutdown();
        System.exit(0);
    }
    private void consoleCommand(String command){
        if(command.equalsIgnoreCase("stop")){
            System.out.println("Stopping bot . . .");
            setRunning(false);
        }
    }
}

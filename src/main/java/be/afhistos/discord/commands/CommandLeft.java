package be.afhistos.discord.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.core.managers.AudioManager;

@CommandInfo(name = "exit", description = "Permet de me déconnecter du salon vocal")
@Author("afhistos")
public class CommandLeft extends Command {
    public CommandLeft(){
        this.name = "exit";
        this.help = "Permet de couper la radio";
        this.guildOnly = true;
        this.category = new Category("Gestion de la radio");
        this.requiredRole = "DJ";
    }
    @Override
    protected void execute(CommandEvent event) {
        AudioManager audioManager = event.getGuild().getAudioManager();
        if(event.getSelfMember().getVoiceState().inVoiceChannel()){
            audioManager.closeAudioConnection();
            event.getMessage().clearReactions().complete();
            if(event.getSelfMember().getVoiceState().inVoiceChannel()){
                event.getMessage().clearReactions().complete();
                event.reactError();
                event.reply("Une erreur est apparue (Like a pokemon)! Réessaie.");
            }else {
                event.reactSuccess();
            }
        } else {
            event.getMessage().clearReactions().complete();
            event.reactWarning();
            event.reply("Il faut que je sois dans un salon vocal pour le quitter chef !");
        }
    }
}

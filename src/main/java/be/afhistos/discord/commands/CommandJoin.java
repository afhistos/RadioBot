package be.afhistos.discord.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

@CommandInfo(name = "join", description = "Rejoint le salon vocal de l'éxécuteur de la commande")
@Author("afhistos")
public class CommandJoin extends Command {
    public CommandJoin(){
        this.name = "join";
        this.help = "Permet de mettre en place la radio";
        this.guildOnly = true;
        this.category = new Category("Gestion de la radio");
        this.requiredRole = "DJ";
    }

    @Override
    protected void execute(CommandEvent event) {
        Member member= event.getGuild().getMember(event.getAuthor());
        if(member.getVoiceState().inVoiceChannel()){
            AudioManager audioManager = event.getGuild().getAudioManager();
            if(!audioManager.isConnected() && !audioManager.isAttemptingToConnect()){
                VoiceChannel channel = event.getGuild().getMember(event.getAuthor()).getVoiceState().getChannel();
                audioManager.openAudioConnection(channel);
                event.getMessage().clearReactions().queue();
                event.reactSuccess();
            }else{
                event.getMessage().clearReactions().queue();
                event.reactWarning();
                event.reply("Je suis déjà dans un salon vocal / J'essaie déjà de me connecter dans un salon vocal ! ");
            }
        } else{
            event.getMessage().clearReactions().complete();
            event.reactError();
            event.reply("Tu dois être dans un salon vocal pour que je puisse te rejoindre !");
        }
    }
}

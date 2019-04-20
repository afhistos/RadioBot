package be.afhistos.discord.commands;

import be.afhistos.discord.music.Audio;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;

@CommandInfo(name = "play", description = "Permet de jouer une musique")
@Author("afhistos")
public class CommandPlay extends Command {
    public CommandPlay(){
        this.name = "play";
        this.help = "Permet de couper la radio";
        this.guildOnly = true;
        this.arguments = "<Lien>";
        this.category = new Category("Gestion de la radio");
        this.requiredRole = "DJ";
    }
    @Override
    protected void execute(CommandEvent event) {
        Guild guild = event.getGuild();
        if(!guild.getAudioManager().isConnected()  && !guild.getAudioManager().isAttemptingToConnect()){
            VoiceChannel voiceChannel = guild.getMember(event.getAuthor()).getVoiceState().getChannel();
            if(voiceChannel == null){
                event.reactWarning();
                event.reply("Tu dois être dans un salon vocal pour éxécuter cette commande");
            } else{
                Audio.getInstance().loadAndPlay(event.getTextChannel(), event.getArgs());
            }
        }
    }
}

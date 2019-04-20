package be.afhistos.discord.commands;

import be.afhistos.discord.music.Audio;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;

@CommandInfo(name = "pause", description = "Mets la radio en pause/ le relance")
@Author("afhistos")
public class CommandPause extends Command {

    public CommandPause(){
        this.name = "pause";
        this.help = "Permet de mettre la radio en pause ou de la relancer";
        this.guildOnly = true;
        this.category = new Category("Gestion de la radio");
        this.requiredRole = "DJ";
    }
    @Override
    protected void execute(CommandEvent event) {
        Guild guild = event.getGuild();
        if(guild.getAudioManager().isConnected()){
            VoiceChannel voiceChannel = guild.getMember(event.getAuthor()).getVoiceState().getChannel();
            if(voiceChannel == null){
                event.reactWarning();
                event.reply("Tu dois être dans un salon vocal pour éxécuter cette commande");
            } else{
                Audio.getInstance().switchPause(guild);
            }
        }
    }
}

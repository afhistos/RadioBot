package be.afhistos.discord.commands;

import be.afhistos.discord.music.Audio;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.core.entities.Guild;

@CommandInfo(name = "volume", description = "Permet de changer le volume de la radio")
@Author("afhistos")
public class CommandVolume extends Command {
    public CommandVolume(){
        this.name = "volume";
        this.aliases = new String[]{"vol"};
        this.help = "Permet changer le volume de la radio";
        this.guildOnly = true;
        this.category = new Category("Gestion de la radio");
        this.requiredRole = "DJ";
    }

    @Override
    protected void execute(CommandEvent event) {
        Guild guild = event.getGuild();
        int volume = Integer.parseInt(event.getArgs());
        if(volume < 0 || volume > 100){
            event.replyWarning("Le volume doit être compris entre 0 et 100 !");
        } else{
            String successful = Audio.getInstance().setVolume(guild, volume);
           event.reply(successful);
           event.reply(Audio.getInstance().getDebug(event.getGuild()));
        }
    }
}



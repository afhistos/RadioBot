package be.afhistos.discord.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageEmbed.Field;

import java.awt.*;

@CommandInfo(name = "listradios", description = "Liste les radios disponibles pour le bot")
@Author("Afhistos")
public class CommandListRadios extends Command {
    public CommandListRadios(){
        this.name ="listradios";
        this.aliases= new String[]{"lr", "listr", "radios"};
        this.guildOnly = false;
    }
    @Override
    protected void execute(CommandEvent commandEvent) {
        String[] list = new String[]{"Mouv' Direct::http://direct.mouv.fr/live/mouv-midfi.mp3",
                "Mouv' 100% Mix::https://direct.mouv.fr/live/mouv100p100mix-hifi.mp3",
                "Mouv' Rap Francais::https://direct.mouv.fr/live/mouvrapfr-hifi.mp3?ID=radiofrance",
                "Mouv' Rap US::https://direct.mouv.fr/live/mouvrapus-hifi.mp3?ID=radiofrance",
                "Mouv' R'n'B & Soul::https://direct.mouv.fr/live/mouvrnb-hifi.mp3?ID=radiofrance",
                "Mouv' DanceHall::https://direct.mouv.fr/live/mouvdancehall-hifi.mp3?ID=radiofrance",
                "Mouv' Classics::https://direct.mouv.fr/live/mouvclassics-hifi.mp3?ID=radiofrance",
                "FunRadio BE::http://live.funradio.be/funradiobe-high.mp3",
                "NRJ BE::http://streamingp.shoutcast.com/NRJPremium"};
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Liste des radios disponibles pour les bots");
        embed.setColor(new Color(12,88,210));

        for(int i = 0; i<list.length; i++){
            String str = list[i];
            embed.addField(str.split("::")[0], str.split("::")[1], false);
        }
        commandEvent.reply(embed.build());

    }
}

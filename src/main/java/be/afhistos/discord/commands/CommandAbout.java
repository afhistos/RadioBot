package be.afhistos.discord.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.JDAUtilitiesInfo;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import javafx.application.Application;
import net.dv8tion.jda.bot.entities.ApplicationInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

@CommandInfo(name = "About", description = "Donne quelques infos à propos de moi ;)")
@Author("afhistos")
public class CommandAbout extends Command {
    private boolean is_author = false;
    private final Color color;
    private String replacement_icon = "+";
    private String description;
    private final Permission[] permissions;
    private String oauthLink;
    private final String[] features;

    public CommandAbout(Color color, String description, String[] features, Permission... permissions){
        this.color = color;
        this.description=description;
        this.features=features;
        this.name="about";
        this.help="Donne quelques infos à propos de moi ;)";
        this.guildOnly=false;
        this.permissions=permissions;
        this.botPermissions= new Permission[] {Permission.MESSAGE_EMBED_LINKS};
    }
    @Override
    protected void execute(CommandEvent event) {
        if(oauthLink == null){
            try{
                ApplicationInfo info = event.getJDA().asBot().getApplicationInfo().complete();
                oauthLink = info.isBotPublic() ? info.getInviteUrl(0L, permissions) : "";
            }catch (Exception e){
                Logger log = LoggerFactory.getLogger("OAuth2");
                log.error("Impossible de générer le lien d'invitation");
            }
        }
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(event.getGuild() == null ? color : event.getGuild().getSelfMember().getColor());
        builder.setAuthor("Tout à propos de "+event.getSelfUser().getName() +"!", null, event.getSelfUser().getAvatarUrl());
        boolean join = !(event.getClient().getServerInvite() == null || event.getClient().getServerInvite().isEmpty());
        boolean inv = !oauthLink.isEmpty();
        String invLine = "\n"+(join?"Rejoint mon serveur [`ici`]("+event.getClient().getServerInvite()+")" : (inv ? "Tu peux m'" : ""))+(inv?(join? ", ou ":"")+"[`inviter`]("+oauthLink+") sur ton serveur":"")+"!";
        String author = event.getJDA().getUserById(event.getClient().getOwnerId())==null ? "<@"+event.getClient().getOwnerId()+">":event.getJDA().getUserById(event.getClient().getOwnerId()).getName();
        StringBuilder descr = new StringBuilder().append("Salut! Je suis **").append(event.getSelfUser().getName()).append("**, ")
                .append(description).append("\nJ'").append(is_author ? "'ai été écrit en Java par" : "appartient à").append(" **")
                .append(author).append("** en utilisant [Les extensions de commandes](" + JDAUtilitiesInfo.GITHUB + ") (" + JDAUtilitiesInfo.AUTHOR + " ")
                .append(JDAUtilitiesInfo.VERSION).append(") et la  [Librairie JDA](https://github.com/DV8FromTheWorld/JDA) (")
                .append(JDAInfo.VERSION).append(")\nÉcris `").append(event.getClient().getTextualPrefix()).append(event.getClient().getHelpWord())
                .append("` afin de voir toutes mes commandes!").append(join || inv ? invLine : "").append("\n\nVoici un résumé de ce que je peux faire: ```css");
        for (String feature : features)
            descr.append("\n").append(event.getClient().getSuccess().startsWith("<") ? replacement_icon : event.getClient().getSuccess()).append(" ").append(feature);
        descr.append(" ```");
        builder.setDescription(descr);
        if (event.getJDA().getShardInfo() == null)
        {
            builder.addField("Stats", event.getJDA().getGuilds().size() + " Serveurs \n1 Shard", true);
            builder.addField("Utilisateurs", event.getJDA().getUsers().size() + " Unique\n" + event.getJDA().getGuilds().stream().mapToInt(g -> g.getMembers().size()).sum() + " Total", true);
            builder.addField("Salons", event.getJDA().getTextChannels().size() + " Textuels\n" + event.getJDA().getVoiceChannels().size() + " Vocaux", true);
        }
        else
        {
            builder.addField("Stats", (event.getClient()).getTotalGuilds() + " Serveurs\nShard " + (event.getJDA().getShardInfo().getShardId() + 1)
                    + "/" + event.getJDA().getShardInfo().getShardTotal(), true);
            builder.addField("Ce \"shard\"", event.getJDA().getUsers().size() + " Utilisateurs\n" + event.getJDA().getGuilds().size() + " Serveurs", true);
            builder.addField("", event.getJDA().getTextChannels().size() + " Salons textuels\n" + event.getJDA().getVoiceChannels().size() + " Salons vocaux", true);
        }
        builder.setFooter("Dernier redémarrage", null);
        builder.setTimestamp(event.getClient().getStartTime());
        event.reply(builder.build());
    }
}

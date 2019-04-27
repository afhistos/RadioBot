package be.afhistos.discord.commands;

import be.afhistos.discord.news.News;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

import java.io.IOException;

@CommandInfo(name = "news")
@Author("Afhistos")
public class CommandNews extends Command {

    public CommandNews(){
        this.name="news";
        this.help="Affiche les dernières nouvelles";
        this.guildOnly = true;
        this.category = new Category("News");
    }
    @Override
    protected void execute(CommandEvent commandEvent) {
        try {
            commandEvent.reply(News.getNews());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

package be.afhistos.discord.news;

import be.afhistos.discord.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class News {
    private int hour;
    private int minute;
    private static int lastNewsId;
    private static String key;
    public News(String time, String apiKey) {
        Integer hour = Integer.valueOf(time.split(":")[0]);
        Integer minute = Integer.valueOf(time.split(":")[1]);
        this.hour = hour;
        this.minute = minute;
        key = apiKey;
        lastNewsId = 0;
        startTime();
    }

    private void startTime() {
        int tHour = this.hour;
        int tMin = this.minute;
        new Thread(() -> {
            try{
                while(true){
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    int hour = Integer.valueOf(sdf.format(date).split(":")[0]);
                    int minute = Integer.valueOf(sdf.format(date).split(":")[1]);
                    if(tHour == hour){
                        if(tMin == minute){
                            sendNews();
                        }
                    }
                    Thread.sleep(10000); // 1 minute
                }
            }catch (Exception ign) {}
        }).start();
    }
    public static MessageEmbed getNews()throws IOException{
        String json = get("https://newsapi.org/v2/top-headlines?country=be&category=general&apiKey="+key);
        JSONObject object = new JSONObject(json);
        JSONObject article = object.getJSONArray("articles").getJSONObject(lastNewsId);
        Object author = article.get("author");
        Object title = article.get("title");
        Object url = article.get("url");
        Object imgUrl = article.get("urlToImage");
        Object source = article.getJSONObject("source").get("name");
        Object desc = article.get("description");
        Object content = article.get("content");
        Object publicationTime = article.get("publishedAt");
        Object maxContent = object.get("totalResults");
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(String.valueOf(title), String.valueOf(url));
        embed.setAuthor(String.valueOf(author), "https://"+String.valueOf(source).toLowerCase());
        embed.setFooter("Via newsapi.org", "https://newsapi.org/images/n-logo-border.png");
        embed.setColor(new Color(37,105,160));
        embed.setThumbnail(String.valueOf(imgUrl));
        embed.setDescription(String.valueOf(desc));
        embed.addField("Publié le: "+String.valueOf(publicationTime).split("T")[0], String.valueOf(content),true);
        lastNewsId++;
        if(lastNewsId >= Integer.valueOf(String.valueOf(maxContent))){
            lastNewsId = 0;
        }
        return embed.build();
    }
    private static void sendNews() throws IOException {
        for(Guild guild : Main.getJda().getGuilds()){
            try {
                System.out.println(guild.getId());
                guild.getTextChannelsByName("news", true).get(0).sendMessage(getNews()).queue();
            } catch (Exception ign) {}
        }

    }

    private static String get(String url) throws IOException{
        String source ="";
        URL oracle = new URL(url);
        URLConnection yc = oracle.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine;
        while((inputLine = in.readLine()) != null)
        source += inputLine;
        in.close();
        return source;
    }
}

package be.afhistos.discord.music;

/**
 * Class copied and modified from
 * 'https://github.com/sedmelluq/lavaplayer/blob/master/demo-jda/src/main/java/com/sedmelluq/discord/lavaplayer/demo/jda/Main.java'
 */

import be.afhistos.discord.Main;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackState;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Audio extends ListenerAdapter {
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;
    public static TextChannel channel;

    public Audio() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] command = event.getMessage().getContentRaw().split(" ", 2);
        Guild guild = event.getGuild();

        if (guild != null) {
            if (",play".equals(command[0]) && command.length == 2) {
                loadAndPlay(event.getTextChannel(), command[1]);
            } else if (",skip".equals(command[0])) {
                skipTrack(event.getTextChannel());
            } else if(",vol".equals(command[0]) || ",volume".equals(command[0])){
                setVolume(event.getTextChannel(), command[1]);
            } else if(",nowplaying".equals(command[0]) || ",np".equals(command[0])){
                sendNowPlaying(event.getTextChannel());
            }
        }

        super.onMessageReceived(event);
    }

    public void sendNowPlaying(TextChannel channel) {
        AudioTrack track = getGuildAudioPlayer(channel.getGuild()).player.getPlayingTrack();
        EmbedBuilder embed = new EmbedBuilder();
        if(track == null){
            embed.setTitle("Aucun morceau n'est joué pour le moment!");
            embed.setColor(new Color(45,45,45));
            embed.addField("Pour jouer de la musique:", ",play <Lien youtube/soundcloud/radios/...>", true);
            embed.addBlankField(true);
            embed.addField("Pour obtenir la liste des radios disponibles: ", ",listradios", true);
        }else {
            embed.setTitle("Morceau actuellement joué:", track.getInfo().uri);
            embed.setColor(track.getSourceManager().getSourceName().equalsIgnoreCase("youtube") ? new Color(255, 75, 75) : new Color(45, 45, 45));
            embed.addField(track.getInfo().title, "par " + track.getInfo().author.replace("- Topic", ""), false);
            embed.addField("Position:", Audio.getTimestamp(track.getPosition()) + "/" + Audio.getTimestamp(track.getDuration()), true);
            String status = Audio.getStatus(track);
            embed.addField("Status: ", status, true);
            String stream = track.getInfo().isStream ? "Oui" : "Non";
            embed.addField("Joue un stream ?", stream, false);
            embed.addField("Volume:", getGuildAudioPlayer(channel.getGuild()).player.getVolume()+"%", true);
        }
        embed.setThumbnail("https://img.youtube.com/vi/"+track.getIdentifier()+"/hqdefault.jpg");
        embed.setFooter("RadioBot, pour vous servir", Main.getJda().getSelfUser().getAvatarUrl());
        channel.sendMessage(embed.build()).queue();
    }

    public static String getStatus(AudioTrack track) {
        String status;
        if(track.getState().equals(AudioTrackState.PLAYING)){
            status = "En train de jouer";
        } else if(track.getState().equals(AudioTrackState.LOADING)){
            status = "Charge le morceau...";
        } else if(track.getState().equals(AudioTrackState.INACTIVE)){
            status = "Inactif";
        } else if(track.getState().equals(AudioTrackState.FINISHED)){
            status = "A fini de jouer";
        } else if(track.getState().equals(AudioTrackState.SEEKING)){
            status = "Recherche le morceau à jouer";
        } else if(track.getState().equals(AudioTrackState.STOPPING)){
            status = "En train de s'arrêter";
        }else{
            status = "Status inconnu";
        }
        return status;
    }

    private void setVolume(TextChannel textChannel, String s) {
        int vol;
        try{
            vol = Integer.valueOf(s);
        }catch (Exception ign){
            vol = 35; //Default
        }
        if(vol < 0 || vol > 120){
            textChannel.sendMessage(":warning: Le volume doit être un nombre compris entre **0** et **120**!").queue();
            return;
        }
        getGuildAudioPlayer(textChannel.getGuild()).player.setVolume(vol);
        textChannel.sendMessage("Le volume est maintenant à **"+vol+"%**!").queue();
    }

    private void loadAndPlay(final TextChannel channel, final String trackUrl) {
        this.channel = channel;
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        AudioListener listener = new AudioListener(channel);
        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessage("Ajout du morceau suivant à la file d'attente: " + track.getInfo().title).queue();


                play(channel.getGuild(), musicManager, track, channel);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }
                for(AudioTrack track : playlist.getTracks()){
                    getGuildAudioPlayer(channel.getGuild()).scheduler.queue(track);
                }

                channel.sendMessage("Ajout du morceau suivant à la file d'attente: " + firstTrack.getInfo().title + " (premier morceau de la playlist **" + playlist.getName() + "**)").queue();

                play(channel.getGuild(), musicManager, firstTrack, channel);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Aucun morceau n'a été trouvé à l'adresse __" + trackUrl+"__").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Impossible de jouer le morceau: " + exception.getMessage()).queue();
            }
        });
    }

    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track, TextChannel channel) {
        connectToFirstVoiceChannel(guild.getAudioManager());
        musicManager.scheduler.queue(track);
    }

    private void skipTrack(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        channel.sendMessage("Saut du morceau actuel.").queue();
    }

    private static void connectToFirstVoiceChannel(AudioManager audioManager) {
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
                audioManager.openAudioConnection(voiceChannel);
                break;
            }
        }
    }
    public static String getTimestamp(long ms){
        int sec = (int) (ms /1000) % 60;
        int min = (int) ((ms /(1000 *60)) %60);
        int h = (int) ((ms /(1000 *60 *60)) %54);
        if(h > 0){
            return String.format("%02d:%02d:%02d", h,min,sec);
        } else{
            return String.format("%02d:%02d", min,sec);
        }
    }

}

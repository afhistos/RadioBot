package be.afhistos.discord.music;


import be.afhistos.discord.Main;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.requests.RestAction;

import java.util.HashMap;
import java.util.Map;

public class Audio {
    private static Audio instance = new Audio();
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers = new HashMap<Long, GuildMusicManager>();
    private Audio(){
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild){
        long guildId  =guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);
        if(musicManager == null){
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }
        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }
    public void loadAndPlay(final TextChannel channel, final String trackUrl){
        RestAction<Message> msg = channel.getMessageById(channel.getLatestMessageId());
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessage("Ajout de "+track.getInfo().title+" à la file d'attente").queue(message -> {
                    message.addReaction(Main.getClient().getSuccess());
                });
                play(channel.getGuild(), musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                if(firstTrack == null){
                    firstTrack = playlist.getTracks().get(0);
                }
                channel.sendMessage("Ajout de "+firstTrack.getInfo().title+" à la file d'attente.").queue();
                msg.queue(message -> {
                    message.addReaction(Main.getClient().getSuccess());
                });
                play(channel.getGuild(), musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                msg.queue(message -> {
                    message.addReaction(Main.getClient().getWarning());
                });
                channel.sendMessage("Aucun morceau n'a été trouvé à l'adresse "+trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                msg.queue(message -> {
                    message.addReaction(Main.getClient().getError());
                    channel.sendMessage("Impossible de jouer le morceau demandé: "+e.getMessage()).queue();
                });
            }
        });
    }

    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track) {
        connectToFirstVoiceChannel(guild.getAudioManager());
        musicManager.scheduler.queue(track);
    }
    public String setVolume(Guild guild, int v){
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        AudioTrack track = musicManager.player.getPlayingTrack();
        musicManager.player.stopTrack();
        musicManager.player.setVolume(v);
        musicManager.player.startTrack(track, true);
        return "New volume :" +musicManager.player.getVolume();
    }
    public String getDebug(Guild guild){
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        AudioPlayer player = musicManager.player;
        int volume = player.getVolume();
        AudioTrack track = player.getPlayingTrack();
        TrackScheduler scheduler = musicManager.scheduler;
        return "**Débug**\n```Markdown\n<GuildMusicManager musicManager=\""+musicManager+"\">\n<AudioPlayer player=\""+player
            +"\">\n<int volume=\""+volume+"\">\n<AudioTrack track=\""+track+"\">\n<TrackScheduler scheduler=\""+scheduler+"\">\n```";
    }
    public void skipTrack(TextChannel channel){
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();
        channel.sendMessage("Saut du morceau actuel").queue();
    }
    public void switchPause(Guild guild){
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        if(musicManager.player.isPaused()){
            musicManager.player.setPaused(false);
        }else{
            musicManager.player.setPaused(true);
        }
    }
    private static void connectToFirstVoiceChannel(AudioManager audioManager){
        if(!audioManager.isConnected() && !audioManager.isAttemptingToConnect()){
            for(VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()){
                audioManager.openAudioConnection(voiceChannel);
                break;
            }
        }
    }

    public static Audio getInstance() {
        return new Audio();
    }
}

/**
 * Hello, I have a problem with the lavaplayer library: when I try to change the sound, it stops playing the music so early, I have to make it leave the vocal channel and come back so that it can play again, and the sound is finally not changed. Same when I pause it: impossible to restart the music. Do you have a solution? (I use the classes given with the example for JDA <https://github.com/sedmelluq/lavaplayer/tree/master/demo-jda/src/main/java/com/sedmelluq/discord/lavaplayer/demo/jda> and here is the class I use to modify the sound etc... https://hastebin.com/uvufekokev.java) Thank you in advance :)
 *
 * Translated with www.DeepL.com/Translator
 */

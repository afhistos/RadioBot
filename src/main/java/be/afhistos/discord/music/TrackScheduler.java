package be.afhistos.discord.music;

import be.afhistos.discord.Main;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;

    /**
     * @param player The audio player this scheduler uses
     */
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        player.startTrack(queue.poll(), false);
        AudioListener listener = new AudioListener(Audio.channel);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        sendNowPlaying(Audio.channel, track);
    }

    public void sendNowPlaying(TextChannel channel, AudioTrack track) {
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
            embed.addField("Joue un stream ?", stream, true);
            embed.addField("Volume:", player.getVolume()+"%", false);
        }
        embed.setThumbnail("https://img.youtube.com/vi/"+track.getIdentifier()+"/hqdefault.jpg");
        embed.setFooter("RadioBot, pour vous servir", Main.getJda().getSelfUser().getAvatarUrl());
        channel.sendMessage(embed.build()).queue();
    }


}
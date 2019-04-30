package be.afhistos.discord.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.TextChannel;

public class AudioListener extends AudioEventAdapter {

    private TextChannel channel;

    public AudioListener(TextChannel channel){
        this.channel = channel;
    }
    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {

    }
}

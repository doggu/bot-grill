package utilities;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import netscape.javascript.JSObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;

public class JsoupPlayground {
    public static void main(String[] args) throws IOException, URISyntaxException {
        InputStream is = new URL("https://feheroes.gamepedia.com/api.php").openStream();//new URL("https://en.wikipedia.org/w/api.php?action=query&generator=allpages&gaplimit=3&gapfrom=Ba&prop=links%7Ccategories&format=json").openStream();

        Scanner s = new Scanner(is);

        while (s.hasNextLine()) {
            System.out.println(s.nextLine());
        }

        /*
        Document anna = Jsoup.parse(new URL(
                "https://feheroes.gamepedia.com/" +
                        "Anna:_Wealth-Wisher/Quotes"),
                3000);

        Elements tables = anna.select("table[class*=wikitable]");

        ArrayList<String> audio_paths = new ArrayList<>(tables.size());

        for (int i=0; i<tables.size(); i++) {
            audio_paths.addAll(tables.get(i).select("audio")
                    .eachAttr("src"));
        }

        AudioPlayerManager manager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(manager);

        AudioPlayer player = manager.createPlayer();
        AudioEventAdapter audioEventListener = new AudioEventAdapter() {
            @Override
            public void onPlayerPause(AudioPlayer player) {
                super.onPlayerPause(player);
            }
        };
        player.addListener(audioEventListener);

        manager.loadItem("html", new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                player.playTrack(audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                //playlist?
            }

            @Override
            public void noMatches() {
                // Notify the user that we've got nothing
            }

            @Override
            public void loadFailed(FriendlyException throwable) {
                // Notify the user that everything exploded
            }
        });

        AudioSendHandler g = new AudioSendHandler() {
            @Override
            public boolean canProvide() {
                return false;
            }

            @Nullable
            @Override
            public ByteBuffer provide20MsAudio() {
                return null;
            }
        };

        for (String set: audio_paths) {
            System.out.println(set);
        }

        /**/
    }
}

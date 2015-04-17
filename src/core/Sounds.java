package core;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import static javafx.scene.media.MediaPlayer.INDEFINITE;
import static javafx.scene.media.MediaPlayer.Status.PLAYING;
import javax.sound.midi.*;
import static javax.sound.midi.Sequencer.LOOP_CONTINUOUSLY;
import javax.sound.sampled.*;

public abstract class Sounds {

    private static HashMap<String, Sequencer> midiMap = new HashMap();
    private static HashMap<String, MediaPlayer> mp3Map = new HashMap();
    private static HashMap<String, Clip> wavMap = new HashMap();
    private static String path = "sounds/";
    public static double GLOBAL_VOLUME = 1;

    public static ArrayList<String> all() {
        ArrayList r = new ArrayList();
        for (String name : midiMap.keySet()) {
            if (midiMap.get(name).isRunning()) {
                r.add(name);
            }
        }
        for (String name : mp3Map.keySet()) {
            if (mp3Map.get(name).getStatus() == PLAYING) {
                r.add(name);
            }
        }
        for (String name : wavMap.keySet()) {
            if (wavMap.get(name).isRunning()) {
                r.add(name);
            }
        }
        return r;
    }

    public static boolean existsSound(String name) {
        if (name.endsWith(".mid")) {
            return existsMidi(name);
        }
        if (name.endsWith(".mp3")) {
            return existsMp3(name);
        }
        if (name.endsWith(".wav")) {
            return existsWav(name);
        }
        return false;
    }

    private static boolean existsMidi(String name) {
        return midiMap.get(name) != null && midiMap.get(name).isRunning();
    }

    private static boolean existsMp3(String name) {
        return mp3Map.get(name) != null && mp3Map.get(name).getStatus() == PLAYING;
    }

    private static boolean existsWav(String name) {
        return wavMap.get(name) != null && wavMap.get(name).isRunning();
    }

    public static void playSound(String name) {
        playSound(name, false, 1);
    }

    public static void playSound(String name, boolean loop, double volume) {
        volume *= GLOBAL_VOLUME;
        try {
            if (name.endsWith(".mid")) {
                playMidi(name, loop, volume);
            }
            if (name.endsWith(".mp3")) {
                playMp3(name, loop, volume);
            }
            if (name.endsWith(".wav")) {
                playWav(name, loop, volume);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void playMidi(String name, boolean loop, double volume) throws MidiUnavailableException, FileNotFoundException, IOException, InvalidMidiDataException {
        // Obtains the default Sequencer connected to a default device.
        Sequencer sequencer = MidiSystem.getSequencer();
        // Opens the device, indicating that it should now acquire any system resources it requires and become operational.
        sequencer.open();
        // create a stream from a file
        InputStream is = new BufferedInputStream(new FileInputStream(new File(path + name)));
        // Sets the current sequence on which the sequencer operates.
        // The stream must point to MIDI file data.
        sequencer.setSequence(is);
        //Set looping
        if (loop) {
            sequencer.setLoopCount(LOOP_CONTINUOUSLY);
        }
        // Starts playback of the MIDI data in the currently loaded sequence.
        sequencer.start();
        midiMap.put(name, sequencer);
    }

    private static void playMp3(String name, boolean loop, double volume) {
        new javafx.embed.swing.JFXPanel();
        String uriString = new File(path + name).toURI().toString();
        MediaPlayer mp = new MediaPlayer(new Media(uriString));
        if (loop) {
            mp.setCycleCount(INDEFINITE);
        }
        mp.setVolume(volume);
        mp.play();
        mp3Map.put(name, mp);
    }

    private static void playWav(String name, boolean loop, double volume) throws FileNotFoundException, IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path + name));
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        if (loop) {
            clip.loop(-1);
        }
        ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue((float) (Math.log(volume) / Math.log(10.) * 20.));
        clip.start();
        wavMap.put(name, clip);
//        // open the sound file as a Java input stream
//        InputStream in = new FileInputStream(path + name);
//        // create an audiostream from the inputstream
//        AudioStream audioStream = new AudioStream(in);
//        // play the audio clip with the audioplayer class
//        AudioPlayer.player.start(audioStream);
//        wavMap.put(name, audioStream);
    }

    public static void stopAll() {
        try {
            for (String name : midiMap.keySet()) {
                stopMidi(name);
            }
            for (String name : mp3Map.keySet()) {
                stopMp3(name);
            }
            for (String name : wavMap.keySet()) {
                stopWav(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopSound(String name) {
        try {
            if (name.endsWith(".mid")) {
                stopMidi(name);
            }
            if (name.endsWith(".mp3")) {
                stopMp3(name);
            }
            if (name.endsWith(".wav")) {
                stopWav(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void stopMidi(String name) throws MidiUnavailableException, FileNotFoundException, IOException, InvalidMidiDataException {
        midiMap.get(name).stop();
    }

    private static void stopMp3(String name) {
        mp3Map.get(name).stop();
    }

    private static void stopWav(String name) throws FileNotFoundException, IOException {
        wavMap.get(name).stop();
    }
}

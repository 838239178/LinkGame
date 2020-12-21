package gm.swing;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.MultimediaInfo;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * 部分使用了根目录下‘java-1.0.2.jar’内的代码
 *
 * @author http://www.java2s.com/Code/Jar/j/Downloadjave102jar.htm
 */
public class Sound {
    public static class Path {
        public static final String LINK_SOUND = "mic/link_sound.wav";
        public static final String TOUCH_SOUND = "mic/touch_sound.wav";
        public static final String BGM = "mic/bgm.wav";
    }

    private SourceDataLine dataLine;
    private AudioFormat format;
    private byte[] data;
    private long duration;

    public Sound(String name) throws IOException, UnsupportedAudioFileException {
        File source = new File(name);
        //region ...java-1.0.2.jar
        Encoder encoder = new Encoder();
        try {
            MultimediaInfo m = encoder.getInfo(source);
            duration = m.getDuration();
        } catch (EncoderException e) {
            e.printStackTrace();
        }
        //endregion

        AudioInputStream in = AudioSystem.getAudioInputStream(source);
        format = in.getFormat();
        data = new byte[in.available()];
        in.read(data);
        in.close();

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        try {
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public long getDuration() {
        return duration;
    }

    public void play() {
        new Thread(() -> {
            try {
                dataLine.open(format, data.length*2);
                dataLine.start();
                //写入line
                dataLine.write(data, 0, data.length);
                dataLine.drain();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dataLine.close();
            }
        }).start();
    }
}

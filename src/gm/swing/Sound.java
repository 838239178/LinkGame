package gm.swing;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    private SourceDataLine dataLine;
    private AudioFormat format;
    private byte[] data;

    public Sound(String name) throws IOException, UnsupportedAudioFileException {
        AudioInputStream in = AudioSystem.getAudioInputStream(new File(name));
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

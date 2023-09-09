import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;

import javax.swing.*;
import java.io.File;

public class AudioTest {
    public static void main(String[] args) throws StreamPlayerException {
        StreamPlayer streamPlayer = new StreamPlayer();
        String audioAbsolutePath = "E:\\song\\songfile\\窃窃_兰音.wav";
        streamPlayer.open(new File(audioAbsolutePath));
        JFrame frame = new JFrame();
        frame.setSize(300, 300);
        JButton button = new JButton("Play");
        JButton button2 = new JButton("Pause");
        JButton button3 = new JButton("switch");
        frame.add(button);
        frame.add(button2);
        frame.add(button3);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setVisible(true);
        button.addActionListener(e -> {
            try {
                if (streamPlayer.isPaused()) {
                    streamPlayer.resume();
                }else {
                    streamPlayer.play();
                }
            } catch (StreamPlayerException ex) {
                throw new RuntimeException(ex);
            }
        });
        button2.addActionListener(e -> {
            streamPlayer.pause();
        });
        button3.addActionListener(e -> {
            try {
                if (streamPlayer.isPaused()) {
                    streamPlayer.resume();
                } else if (streamPlayer.isPlaying()) {
                    streamPlayer.pause();
                }else {
                    streamPlayer.play();
                }

            } catch (StreamPlayerException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}

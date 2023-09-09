package component;

import obj.AudioManager;
import obj.Song;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AudioManagerPanel extends JPanel{
    private static AudioManagerPanel uniqueInstance = new AudioManagerPanel();
    private JLabel songname = new JLabel();
    private JButton Button = new JButton();

    AudioManager audioManager = AudioManager.getInstance();
    private Song playingSong = null;

    public static AudioManagerPanel getUniqueInstance(){return uniqueInstance;}

    ImageIcon playIcon = JSongList.reSize(new ImageIcon("src\\main\\resources\\icon\\play.png"),30,30);
    ImageIcon pauseIcon = JSongList.reSize(new ImageIcon("src\\main\\resources\\icon\\pause.png"),30,30);
    private AudioManagerPanel(){

        Button.setIcon(playIcon);
        Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                {

                    if (audioManager.isPlaying()){
                        System.out.println("从播放时暂停");
                        audioManager.pause();
                        if (playingSong == null)
                            songname.setText("当前未播放歌曲");
                        else
                            songname.setText("已暂停：" + playingSong.getName());
                        // 换图标
                        Button.setIcon(playIcon);
                        Button.setToolTipText("播放");
                    }else{
                        audioManager.start();
                        System.out.println("从暂停时播放");
                        if (playingSong == null)
                            songname.setText("当前未播放歌曲");
                        else
                            songname.setText("正在播放：" + playingSong.getName());
                        // 换图标
                        Button.setIcon(pauseIcon);
                        Button.setToolTipText("暂停");
                    }
                }
            }
        });


        songname.setFont(new Font("仿宋",Font.BOLD,20));
        songname.setText("当前未播放歌曲");

        Box box = Box.createHorizontalBox();
        box.add(songname);
        box.add(Box.createHorizontalStrut(200));
        box.add(Button);

        this.setBounds(0,0,600,100);
        this.setBackground(Color.GRAY);
        this.add(box);
    }

    public boolean setSong(Song song){
        this.playingSong = song;
        boolean setSong = audioManager.setSong(song);
        boolean start = audioManager.start();
        Button.setIcon(pauseIcon);
        songname.setText("正在播放：" + song.getName());
        return (setSong & start);

    }
}

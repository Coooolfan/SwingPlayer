package component;

import Interface.AudioManagerInterface;
import object.AudioManager;
import object.Song;
import proxy.AudioManagerProxy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AudioManagerPanel extends JPanel{
    /**
     * 只出现一个实例
     * 不允许多个实例存在
     */
    private static AudioManagerPanel uniqueInstance = new AudioManagerPanel();
    public static AudioManagerPanel getUniqueInstance(){return uniqueInstance;}

    /*内部元素*/
    private JLabel songname = new JLabel();
    private JButton Button = new JButton();
    AudioManager audioManager = AudioManager.getInstance();
    AudioManagerInterface audioManagerProxy = AudioManagerProxy.createProxy(audioManager);
    private Song playingSong = null;
    ImageIcon playIcon = SongListPanel.reSize(new ImageIcon("src\\main\\resources\\icon\\play.png"),30,30);
    ImageIcon pauseIcon = SongListPanel.reSize(new ImageIcon("src\\main\\resources\\icon\\pause.png"),30,30);

    /**
     * 构造函数
     * 播放暂停按钮逻辑
     * 页面设置
     */
    private AudioManagerPanel(){
        Button.setIcon(playIcon);

        /*播放&暂停按钮*/
        Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (audioManagerProxy.isPlaying()){
//                    System.out.println("从播放时暂停");
                    audioManagerProxy.pause();
                    if (playingSong == null)
                        songname.setText("当前未播放歌曲");
                    else
                        songname.setText("已暂停：" + playingSong.getName());
                    Button.setIcon(playIcon);
                    Button.setToolTipText("播放");
                }else{
                    audioManagerProxy.start();
//                    System.out.println("从暂停时播放");
                    if (playingSong == null)
                        songname.setText("当前未播放歌曲");
                    else
                        songname.setText("正在播放：" + playingSong.getName());
                    Button.setIcon(pauseIcon);
                    Button.setToolTipText("暂停");
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

    /**
     * 设置播放歌曲
     * @param song  Song 歌曲
     * @return boolean 是否成功
     */
    public boolean setSong(Song song){
        this.playingSong = song;
        boolean setSong = audioManagerProxy.setSong(song);
        boolean start = audioManagerProxy.start();
        Button.setIcon(pauseIcon);
        songname.setText("正在播放：" + song.getName());
        return (setSong & start);
    }
}

package component;

import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;
import obj.AudioManager;
import obj.Song;
import obj.SongList;
import obj.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class JSongList extends JPanel {
    private SongList songList;
    private JPanel top =  new JPanel();
    private JLabel imgicon = new JLabel();
    private JLabel nameLabel = new JLabel();
    private JLabel desLabel = new JLabel();
    private JPanel buttom = new JPanel();
    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem delete = new JMenuItem("删除此歌单");
    JMenuItem repaint = new JMenuItem("刷新此页");
    private DefaultTableModel dtm;

    public JSongList(MainFrame rootFrame,SongList songList,User user){
        this.setLayout(new BorderLayout());
        this.songList = songList;
        imgicon.setIcon(reSize(new ImageIcon(String.valueOf(songList.getIcon())),50,50));

        nameLabel.setText(songList.getName());
        desLabel.setText(songList.getDes());
        nameLabel.setFont(new Font("仿宋", Font.BOLD, 30));
        desLabel.setFont(new Font("仿宋", Font.BOLD, 15));
        Box box = Box.createVerticalBox();
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        desLabel.setHorizontalAlignment(SwingConstants.CENTER);
        box.add(nameLabel);
        box.add(desLabel);

        delete.addActionListener(e -> {
            boolean result = JOptionPane.showConfirmDialog(this,"是否删除此歌单","提示",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
            if(result){
                boolean remove = songList.remove();
                if (remove)
                    JOptionPane.showMessageDialog(this,"删除成功,重启主页面后生效");
                else JOptionPane.showMessageDialog(this,"删除失败","提示",JOptionPane.ERROR_MESSAGE);
            }
        });

        repaint.addActionListener(e->{
            rootFrame.repaint(songList);
        });

        popupMenu.add(delete);
        popupMenu.add(repaint);

        top.add(imgicon, BorderLayout.WEST);
        top.add(box,BorderLayout.CENTER);

        ArrayList<Song> songs = songList.getSongs();



        buttom.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        for (int i = 0; i < songs.size(); i++) {

            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.anchor = GridBagConstraints.WEST;
            ImageIcon icon = new ImageIcon(String.valueOf(songs.get(i).getIcon()));
            icon = reSize(icon,50,50);
            buttom.add(new JLabel(icon),gbc);

            gbc.gridx = 1;
            gbc.gridy = i;
            gbc.weightx = 80;
            gbc.weighty = 0;
            gbc.anchor = GridBagConstraints.WEST;
            JLabel label = new JLabel("<html>" +songs.get(i).getName()+"</html>");
            label.setPreferredSize(new Dimension(150,50));
            buttom.add(label,gbc);

            gbc.gridx = 2;
            gbc.gridy = i;
            gbc.weightx = 80;
            gbc.weighty = 0;
            gbc.anchor = GridBagConstraints.WEST;
            label = new JLabel("<html>"+songs.get(i).getSinger()+"</html>");
            label.setPreferredSize(new Dimension(150,50));
            buttom.add(label,gbc);

            gbc.gridx = 3;
            gbc.gridy = i;
            gbc.weightx = 80;
            gbc.weighty = 0;
            gbc.anchor = GridBagConstraints.WEST;
            label = new JLabel("<html>"+songs.get(i).getAlbum()+"</html>");
            label.setPreferredSize(new Dimension(150,50));
            buttom.add(label,gbc);
            gbc.gridx = 4;
            gbc.gridy = i;
            gbc.weightx = 30;
            gbc.weighty = 30;
            gbc.anchor = GridBagConstraints.WEST;
            ImageIcon buttonIcon=reSize(new ImageIcon("src\\main\\resources\\icon\\play.png"),30,30);
            JButton player = new JButton(buttonIcon);
            player.setToolTipText("播放歌曲");
            buttom.add(player,gbc);

//          向AudioManager中添加歌曲
            int finalI3 = i;
            player.addActionListener(e -> {
                AudioManagerPanel.getUniqueInstance().setSong(songs.get(finalI3));
            });

            if (songList.isLibrary()){
                // 添加到歌单
                gbc.gridx = 5;
                gbc.gridy = i;
                gbc.weightx = 30;
                gbc.weighty = 30;
                gbc.anchor = GridBagConstraints.WEST;
                ImageIcon addIcon=reSize(new ImageIcon("src\\main\\resources\\icon\\add2List.png"),30,30);
                JButton addSong = new JButton(addIcon);
                addSong.setToolTipText("添加到歌单");
                buttom.add(addSong,gbc);
                int finalI2 = i;
                addSong.addActionListener(e -> {
                    ArrayList<SongList> songLists = user.getSongLists();
                    String[] options = new String[songLists.size()];

                    for (int j = 0; j < songLists.size(); j++)
                        options[j] = songLists.get(j).getName();
                    String targetSongListName = (String) JOptionPane.showInputDialog(this, "添加到歌单：", "目标歌单选择", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if(targetSongListName == null)return;
                    int targetListID=-1;
                    for (int j = 0; j < songLists.size(); j++)
                        if (targetSongListName.equals(songLists.get(j).getName()))
                             targetListID = songLists.get(j).getListID();

                    // 判断歌单是否存在这首歌
                    SongList targetSongList = SongList.getSongListByListID(targetListID);
                    for (int j = 0; j < targetSongList.getSongs().size(); j++) {
                        if (targetSongList.getSongs().get(j).getName().equals(songs.get(finalI2).getName())){
                            JOptionPane.showMessageDialog(this,"歌单中已存在此歌","提示",JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    }
                    targetSongList.add(songs.get(finalI2));


                });
                //从资源库移除
                gbc.gridx = 6;
                gbc.gridy = i;
                gbc.weightx = 30;
                gbc.weighty = 30;
                gbc.anchor = GridBagConstraints.WEST;
                ImageIcon removeIcon=reSize(new ImageIcon("src\\main\\resources\\icon\\del.png"),30,30);
                JButton removeSong = new JButton(removeIcon);
                removeSong.setToolTipText("从资源库移除");
                buttom.add(removeSong,gbc);
                int finalI = i;
                removeSong.addActionListener(e -> {
                    boolean result = JOptionPane.showConfirmDialog(this,"是否从资源库移除","提示",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                    if(result){
                        songs.get(finalI).remove();
                        rootFrame.repaint(songList);
                    }
                });
            }else {
                //从歌单移除
                gbc.gridx = 5;
                gbc.gridy = i;
                gbc.weightx = 30;
                gbc.weighty = 30;
                gbc.anchor = GridBagConstraints.WEST;
                ImageIcon moveOutIcon=reSize(new ImageIcon("src\\main\\resources\\icon\\del.png"),30,30);
                JButton moveOutSong = new JButton(moveOutIcon);
                moveOutSong.setToolTipText("从歌单移除");
                buttom.add(moveOutSong,gbc);
                int finalI1 = i;
                moveOutSong.addActionListener(e -> {
                    boolean result = JOptionPane.showConfirmDialog(this,"是否从歌单移除","提示",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                    if(result){
                        //TODO 异常
//                        songs.get(finalI1).remove();
                        boolean test = songList.removeSong(songs.get(finalI1).getSongID());
                        System.out.println(test);
                        rootFrame.repaint(songList);
                    }
                });
            }
        }

        top.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println("点击了"+songList.getName());
//                单击右键的时候展示
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popupMenu.show(top, e.getX(), e.getY());
                }
            }
        });

        this.add(top,BorderLayout.NORTH);
        buttom.setSize(500,600);
        this.add(new JScrollPane(buttom));
    }
    public static ImageIcon reSize(ImageIcon image, int width, int height){
        Image img=image.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(img);
    }


}

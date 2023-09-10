package component;

import Interface.SongInterface;
import Interface.SongListInterface;
import Interface.UserInterface;
import object.Song;
import object.SongList;
import object.User;
import proxy.SongListProxy;
import proxy.SongProxy;
import proxy.UserProxy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SongListPanel extends JPanel {
    private JPanel titlePanel =  new JPanel();
    private JLabel imgicon = new JLabel();
    private JLabel nameLabel = new JLabel();
    private JLabel desLabel = new JLabel();
    private JPanel mainPanel = new JPanel();
    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem delete = new JMenuItem("删除此歌单");
    JMenuItem repaint = new JMenuItem("刷新此页");

    /**
     * 构造函数
     * @param rootFrame 主窗体
     * @param songList  歌单
     * @param user  用户
     */
    public SongListPanel(MainFrame rootFrame, SongList songList, User user){
        /*创建歌单代理*/
        SongListInterface songListProxy = SongListProxy.createProxy(songList);
        ArrayList<Song> songs = songListProxy.getSongs();

        /*设置title页面布局*/
        Box box = Box.createVerticalBox();
        this.setLayout(new BorderLayout());
        imgicon.setIcon(reSize(new ImageIcon(String.valueOf(songList.getIcon())),50,50));
        nameLabel.setText(songList.getName());
        desLabel.setText(songList.getDes());
        nameLabel.setFont(new Font("仿宋", Font.BOLD, 30));
        desLabel.setFont(new Font("仿宋", Font.BOLD, 15));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        desLabel.setHorizontalAlignment(SwingConstants.CENTER);
        box.add(nameLabel);
        box.add(desLabel);
        titlePanel.add(imgicon, BorderLayout.WEST);
        titlePanel.add(box,BorderLayout.CENTER);

        /*鼠标右键按钮事件*/
        delete.addActionListener(e -> {
            boolean result = JOptionPane.showConfirmDialog(this,"是否删除此歌单","提示",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
            if(result){
                if (songListProxy.remove()) {
                    JOptionPane.showMessageDialog(this, "删除成功,重启主页面后生效");
                    //TODO 自动刷新页面
                }
                else {
                    JOptionPane.showMessageDialog(this, "删除失败", "提示", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        repaint.addActionListener(e->{
            rootFrame.repaint();
        });
        popupMenu.add(delete);
        popupMenu.add(repaint);

        /*设置main页面布局*/
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        for (int i = 0; i < songs.size(); i++) {
            /*图标*/
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.anchor = GridBagConstraints.WEST;
            ImageIcon icon = new ImageIcon(String.valueOf(songs.get(i).getIcon()));
            icon = reSize(icon,50,50);
            mainPanel.add(new JLabel(icon),gbc);

            /*歌名*/
            gbc.gridx = 1;
            gbc.gridy = i;
            gbc.weightx = 80;
            gbc.weighty = 0;
            gbc.anchor = GridBagConstraints.WEST;
            JLabel label = new JLabel("<html>" +songs.get(i).getName()+"</html>");
            label.setPreferredSize(new Dimension(150,50));
            mainPanel.add(label,gbc);

            /*歌手*/
            gbc.gridx = 2;
            gbc.gridy = i;
            gbc.weightx = 80;
            gbc.weighty = 0;
            gbc.anchor = GridBagConstraints.WEST;
            label = new JLabel("<html>"+songs.get(i).getSinger()+"</html>");
            label.setPreferredSize(new Dimension(150,50));
            mainPanel.add(label,gbc);

            /*所属专辑*/
            gbc.gridx = 3;
            gbc.gridy = i;
            gbc.weightx = 80;
            gbc.weighty = 0;
            gbc.anchor = GridBagConstraints.WEST;
            label = new JLabel("<html>"+songs.get(i).getAlbum()+"</html>");
            label.setPreferredSize(new Dimension(150,50));
            mainPanel.add(label,gbc);

            /*播放按钮*/
            gbc.gridx = 4;
            gbc.gridy = i;
            gbc.weightx = 30;
            gbc.weighty = 30;
            gbc.anchor = GridBagConstraints.WEST;
            ImageIcon buttonIcon=reSize(new ImageIcon("src\\main\\resources\\icon\\play.png"),30,30);
            JButton player = new JButton(buttonIcon);
            player.setToolTipText("播放歌曲");
            mainPanel.add(player,gbc);

            /*播放按钮事件*/
            int line = i;
            player.addActionListener(e -> {
                if (!AudioManagerPanel.getUniqueInstance().setSong(songs.get(line)))
                    System.out.println("播放弃异常");
            });

            /*区分资源库与歌单*/
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
                mainPanel.add(addSong,gbc);
                int finalI2 = i;

                /*添加按钮事件*/
                addSong.addActionListener(e -> {
                    UserInterface userProxy = UserProxy.createProxy(user);
                    ArrayList<SongList> songLists = userProxy.getSongLists();
                    String[] options = new String[songLists.size()];
                    for (int j = 0; j < songLists.size(); j++){
                        SongListInterface songListProxy2 =SongListProxy.createProxy(songLists.get(j));
                        options[j] = songListProxy2.getName();
                    }
                    String targetSongListName = (String) JOptionPane.showInputDialog(this, "添加到歌单：", "目标歌单选择", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if(targetSongListName == null)return;
                    int targetListID=-1;
                    for (int j = 0; j < songLists.size(); j++)
                        if (targetSongListName.equals(songLists.get(j).getName()))
                             targetListID = songLists.get(j).getListID();

                    // 判断歌单是否已经存在这首歌
                    SongList targetSongList = SongList.getSongListByListID(targetListID);
                    SongListInterface targetSongListProxy = SongListProxy.createProxy(targetSongList);
                    for (int j = 0; j < targetSongListProxy.getSongs().size(); j++) {
                        if (targetSongListProxy.getSongs().get(j).getName().equals(songs.get(finalI2).getName())){
                            JOptionPane.showMessageDialog(this,"歌单中已存在此歌","提示",JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    }
                    if (targetSongListProxy.add(songs.get(finalI2)))
                        JOptionPane.showMessageDialog(this,"成功添加到歌单","提示",JOptionPane.INFORMATION_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(this,"出现错误","提示",JOptionPane.INFORMATION_MESSAGE);
                });

                /*移除按钮事件*/
                gbc.gridx = 6;
                gbc.gridy = i;
                gbc.weightx = 30;
                gbc.weighty = 30;
                gbc.anchor = GridBagConstraints.WEST;
                ImageIcon removeIcon=reSize(new ImageIcon("src\\main\\resources\\icon\\del.png"),30,30);
                JButton removeSong = new JButton(removeIcon);
                removeSong.setToolTipText("从资源库移除");
                mainPanel.add(removeSong,gbc);
                int finalI = i;
                removeSong.addActionListener(e -> {
                    boolean result = JOptionPane.showConfirmDialog(this,"是否从资源库移除","提示",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                    if(result){
                        SongInterface songProxy = SongProxy.createProxy(songs.get(finalI));
                        songProxy.remove();
                        rootFrame.repaint();
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
                mainPanel.add(moveOutSong,gbc);
                int finalI1 = i;
                moveOutSong.addActionListener(e -> {
                    boolean result = JOptionPane.showConfirmDialog(this,"是否从歌单移除","提示",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                    if(result){
                        SongInterface songProxy = SongProxy.createProxy(songs.get(finalI1));
                        if (!songListProxy.removeSong(songProxy.getSongID()))
                            System.out.println("移除错误");
                        rootFrame.repaint();
                    }
                });
            }
        }

        /*标题鼠标右键事件*/
        titlePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popupMenu.show(titlePanel, e.getX(), e.getY());
                }
            }
        });

        this.add(titlePanel,BorderLayout.NORTH);
        mainPanel.setSize(500,600);
        this.add(new JScrollPane(mainPanel));
    }

    /**
     * 更改icon大小
     * @param image ImageIcon 图标
     * @param width int 宽度
     * @param height    int 高度
     * @return ImageIcon 更改后的图标
     */
    public static ImageIcon reSize(ImageIcon image, int width, int height){
        Image img=image.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(img);
    }


}

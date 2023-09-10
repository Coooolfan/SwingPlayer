package component;

import Interface.SongListInterface;
import Interface.UserInterface;
import object.SongList;
import object.User;
import proxy.SongListProxy;
import proxy.UserProxy;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame {
    JFrame frame = new JFrame("SwingPlayer");
    JTabbedPane jTabbedPane = new JTabbedPane(SwingConstants.LEFT,JTabbedPane.WRAP_TAB_LAYOUT);
    User creator;

    /**
     * 组装主体页面
     * @param user User 用户信息
     */
    public void init(User user){
        creator = user;
        UserInterface userProxy = UserProxy.createProxy(user);
        ArrayList<SongList> songLists = userProxy.getSongLists();

        /*音乐库和设置*/
        jTabbedPane.addTab("音乐库",new SongListPanel(this,SongList.getLibrary(),user));
        jTabbedPane.addTab("设置",new SettingPanel(user,this.frame));

        /*单个用户拥有的歌单*/
        for (SongList songList : songLists) {
            SongListInterface songListProxy = SongListProxy.createProxy(songList);
            jTabbedPane.addTab(songListProxy.getName(), new SongListPanel(this,songList,user));
        }

        /*窗体设置*/
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(jTabbedPane,BorderLayout.CENTER);
        frame.add(AudioManagerPanel.getUniqueInstance(), BorderLayout.SOUTH);
        frame.setSize(800, 600);
        frame.setIconImage(new ImageIcon("src\\main\\resources\\icon\\logo.png").getImage());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    /**
     * 重新绘制主窗体
     */
    public void repaint(){
        try{
            int SelectedIndex = jTabbedPane.getSelectedIndex();
            MainFrame root = this;
            if (SelectedIndex == 0){
                jTabbedPane.setComponentAt(SelectedIndex,new SongListPanel(root,SongList.getLibrary(),creator));
            }else {
                jTabbedPane.setComponentAt(SelectedIndex,new SongListPanel(root,creator.getSongLists().get(SelectedIndex-2), creator));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

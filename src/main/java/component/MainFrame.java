package component;

import api.Sqliter;
import obj.SongList;
import obj.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame {
    JFrame frame = new JFrame("SwingPlayer");
    JTabbedPane jTabbedPane = new JTabbedPane(SwingConstants.LEFT,JTabbedPane.WRAP_TAB_LAYOUT);

    User creator;

    public boolean repaint(SongList songList){
        try{
            int SelectedIndex = jTabbedPane.getSelectedIndex();
            MainFrame root = this;
            if (SelectedIndex == 0){
                jTabbedPane.setComponentAt(SelectedIndex,new JSongList(root,SongList.getLibrary(),creator));
            }else {
                jTabbedPane.setComponentAt(SelectedIndex,new JSongList(root,creator.getSongLists().get(SelectedIndex-2), creator));
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void run(User user){
        creator = user;

        /*音乐库和设置*/
        jTabbedPane.addTab("音乐库",new JSongList(this,SongList.getLibrary(),user));
        jTabbedPane.addTab("设置",new SettingPanel(user,this.frame));

        /*单个用户拥有的歌单*/
        ArrayList<SongList> songLists = user.getSongLists();
        for (SongList songList : songLists) {
            jTabbedPane.addTab(songList.getName(), new JSongList(this,songList,user));
        }
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
    public  boolean close(){
        try {
            frame.dispose();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    //test
//    public static void main(String[] args) {
//        new MainFrame().run(new User("test","test",3));
//    }
}

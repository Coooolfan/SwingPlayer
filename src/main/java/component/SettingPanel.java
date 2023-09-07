package component;

import obj.Song;
import obj.SongList;
import obj.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Paths;

public class SettingPanel extends JPanel {
    private JLabel title = new JLabel("设置");
    private JLabel username = new JLabel("账号：");
    private JLabel passwd = new JLabel("密码：");
    private JTextField reUsername = new JTextField(15);
    private JTextField rePassword = new JTextField(15);
    private JButton reUsernameButton =new JButton("修改账号");
    private JButton rePasswordButton = new JButton("修改密码");
    private JButton uploadSongs = new JButton("上传歌曲");
    private JButton buildSongList = new JButton("新建歌单");
    private Box box1 = Box.createHorizontalBox();
    private Box box2 = Box.createHorizontalBox();
    private Box box3 = Box.createHorizontalBox();
    private Box vbox = Box.createVerticalBox();
    private JPanel panel =new JPanel();

    public SettingPanel(User user){
        this.setLayout(new BorderLayout());
        title.setFont(new Font("仿宋", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(title,BorderLayout.NORTH);
        reUsername.setText(user.getUsername());
        reUsername.setEnabled(false);
        rePassword.setEnabled(false);
        box1.add(username);
        box1.add(Box.createHorizontalStrut(5));
        box1.add(reUsername);
        box1.add(Box.createHorizontalStrut(5));
        box1.add(reUsernameButton);

        box2.add(passwd);
        box2.add(Box.createHorizontalStrut(5));
        box2.add(rePassword);
        box2.add(Box.createHorizontalStrut(5));
        box2.add(rePasswordButton);

        box3.add(uploadSongs);
        box3.add(Box.createHorizontalStrut(8));
        box3.add(buildSongList);

        vbox.add(Box.createVerticalStrut(5));
        vbox.add(box1);
        vbox.add(Box.createVerticalStrut(5));
        vbox.add(box2);
        vbox.add(Box.createVerticalStrut(5));
        vbox.add(box3);
        panel.setPreferredSize(new Dimension(200,300));
        panel.add(vbox);
        this.add(panel);

        reUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!reUsername.isEnabled()){
                    reUsername.setEnabled(true);
                }else {
                    user.setUsername(reUsername.getText());
                    user.save();
                }
            }
        });

        rePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!rePassword.isEnabled()){
                    rePassword.setEnabled(true);
                }else {
                    if (rePassword.getText().isEmpty()||rePassword.getText().isBlank()) {
                        JOptionPane.showMessageDialog(panel,"无效密码","提示",JOptionPane.WARNING_MESSAGE);
                    }else{
                        user.setPassword(rePassword.getText());
                        boolean save = user.save();
                        if (!save) {
                            JOptionPane.showMessageDialog(panel,"意外错误","提示",JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        JOptionPane.showMessageDialog(panel,"密码修改成功","提示",JOptionPane.INFORMATION_MESSAGE);
                        rePassword.setText("");
                        rePassword.setEnabled(false);
                    }
                }
            }
        });

        uploadSongs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog();
                dialog.setTitle("添加歌曲");
                JPanel panel = new JPanel();
                panel.setBounds(0,0,50,100);
                dialog.setLocationRelativeTo(null);
                JTextField usernameText = new JTextField(10);
                JTextField iconText = new JTextField(10);
                JTextField pathText = new JTextField(10);
                JTextField singerText =new JTextField(10);
                JTextField albumText= new JTextField(10);
                Box box1 = Box.createHorizontalBox();
                Box box2 = Box.createHorizontalBox();
                Box box3 = Box.createHorizontalBox();
                Box box4 = Box.createHorizontalBox();
                Box box5 = Box.createHorizontalBox();
                JButton choose1 = new JButton("浏览");
                JButton choose2 = new JButton("浏览");
                choose1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser jFileChooser = new JFileChooser();
                        int state=jFileChooser.showOpenDialog(null);
                        if (state == JFileChooser.APPROVE_OPTION)
                            iconText.setText(jFileChooser.getSelectedFile().getPath());
                    }
                });

                choose2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser jFileChooser = new JFileChooser();
                        int state=jFileChooser.showOpenDialog(null);
                        if (state == JFileChooser.APPROVE_OPTION)
                            pathText.setText(jFileChooser.getSelectedFile().getPath());
                    }
                });

                box1.add(new JLabel("歌名"));
                box1.add(Box.createHorizontalStrut(10));
                box1.add(usernameText);
                box2.add(new JLabel("封面"));
                box2.add(Box.createHorizontalStrut(10));
                box2.add(iconText);
                box2.add(choose1);
                box3.add(new JLabel("文件"));
                box3.add(Box.createHorizontalStrut(10));
                box3.add(pathText);
                box3.add(choose2);
                box4.add(new JLabel("歌手"));
                box4.add(Box.createHorizontalStrut(10));
                box4.add(singerText);
                box5.add(new JLabel("专辑"));
                box5.add(Box.createHorizontalStrut(10));
                box5.add(albumText);
                Box vbox = Box.createVerticalBox();
                JButton submit = new JButton("提交");

                vbox.add(box1);
                vbox.add(Box.createVerticalStrut(10));
                vbox.add(box2);
                vbox.add(Box.createVerticalStrut(10));
                vbox.add(box3);
                vbox.add(Box.createVerticalStrut(10));
                vbox.add(box4);
                vbox.add(Box.createVerticalStrut(10));
                vbox.add(box5);
                vbox.add(Box.createVerticalStrut(10));
                vbox.add(submit);
                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Song song = new Song(
                            usernameText.getText(),
                            Paths.get(iconText.getText()),
                            Paths.get(pathText.getText()),
                            singerText.getText(),
                            albumText.getText()
                        );
                        boolean save = song.save();
                        if(save){
                            JOptionPane.showMessageDialog(panel,"歌曲添加成功","提示",JOptionPane.INFORMATION_MESSAGE);
                        }else {
                            JOptionPane.showMessageDialog(panel,"歌曲添加失败","提示",JOptionPane.ERROR_MESSAGE);
                        }
                        /*上传歌曲*/
                    }
                });
                panel.add(vbox);
                dialog.add(panel);
                dialog.setIconImage(new ImageIcon("src\\main\\resources\\icon\\logo.png").getImage());
                dialog.setBounds(0,0,200,240);
                dialog.setLocationRelativeTo(null);
                dialog.setModal(true);
                dialog.setVisible(true);
            }
        });

        buildSongList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog();
                dialog.setTitle("新建歌单");
                JPanel panel = new JPanel();
                Box box1 = Box.createHorizontalBox();
                Box box2 = Box.createHorizontalBox();
                Box box3 = Box.createHorizontalBox();
                JTextField songlistTnameText = new JTextField();
                JTextField describeText = new JTextField();
                JTextField iconText = new JTextField();
                JButton choose = new JButton("浏览");
                choose.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser jFileChooser = new JFileChooser();
                        int state=jFileChooser.showOpenDialog(null);
                        if (state == JFileChooser.APPROVE_OPTION)
                            iconText.setText(jFileChooser.getSelectedFile().getPath());
                    }
                });
                JButton submit = new JButton("新建歌单");
                Box vbox = Box.createVerticalBox();
                box1.add(new JLabel("歌单名"));
                box1.add(Box.createHorizontalStrut(10));
                box1.add(songlistTnameText);
                box2.add(new JLabel("描述"));
                box2.add(Box.createHorizontalStrut(10));
                box2.add(describeText);
                box3.add(new JLabel("封面"));
                box3.add(Box.createHorizontalStrut(10));
                box3.add(iconText);
                box3.add(choose);
                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        /*歌单新建x*/
                        SongList songList = new SongList(
                                songlistTnameText.getText(),
                                describeText.getText(),
                                Paths.get(iconText.getText()),
                                user.getUUID()
                        );
                        boolean save = songList.save();
                         if(save){
                            JOptionPane.showMessageDialog(panel,"歌单新建成功","提示",JOptionPane.INFORMATION_MESSAGE);
                        }else {
                            JOptionPane.showMessageDialog(panel,"歌单新建失败","提示",JOptionPane.ERROR_MESSAGE);
                         }
                    }
                });

                vbox.add(box1);
                vbox.add(Box.createVerticalStrut(10));
                vbox.add(box2);
                vbox.add(Box.createVerticalStrut(10));
                vbox.add(box3);
                vbox.add(Box.createVerticalStrut(10));
                vbox.add(submit);
                panel.add(vbox);
                dialog.add(panel);
                dialog.setIconImage(new ImageIcon("src\\main\\resources\\icon\\logo.png").getImage());
                dialog.setBounds(0,0,200,180);
                dialog.setLocationRelativeTo(null);
                dialog.setModal(true);
                dialog.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SettingPanel test = new SettingPanel(new User("test3","test3"));
        JFrame frame = new JFrame();
        frame.add(test);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}

package component;

import obj.User;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.multi.MultiLookAndFeel;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.Arrays;

public class LandingFrame {
    JFrame frame = new JFrame();
    JPanel loginPanel = new JPanel();
    JPanel signUpPanel = new JPanel();

    /*************************************登入页面**************************************/

    /*登入页面元素*/
    JLabel title = new JLabel("SwingPlayer");   //标题
    Font font = new Font("仿宋", Font.PLAIN, 30);     //标题字体
    JLabel username = new JLabel("账号");
    JLabel password = new JLabel("密码");
    JTextField usernameText = new JTextField(10);
    JPasswordField passwordText = new JPasswordField(10);

    /*按钮*/
    JButton login = new JButton("登入");
    JButton singUp = new JButton("注册");

    /*布局box*/
    Box usernameBox = Box.createHorizontalBox();
    Box passwordBox = Box.createHorizontalBox();
    Box buttonBox = Box.createHorizontalBox();
    Box sumaryLoginBox = Box.createVerticalBox();


    /*************************************注册页面*********************************/

    /*注册页面元素*/
    JLabel SignUpTitle = new JLabel("注册");
    Font signFont = new Font("仿宋", Font.PLAIN, 20);
    JLabel signUsername = new JLabel("用户名");
    JTextField signUserText = new JTextField(10);
    JLabel signPassword = new JLabel("密码");
    JPasswordField signPasswordText =new JPasswordField(10);
    JLabel signRepassword = new JLabel("确认密码");
    JPasswordField signRepasswordText =new JPasswordField(10);

    JButton SignUpButton = new JButton("提交");

    /*box组合*/
    Box signUsernameBox = Box.createHorizontalBox();
    Box signPasswordBox = Box.createHorizontalBox();
    Box signRepasswordBox = Box.createHorizontalBox();
    Box sumarySignUpBox = Box.createVerticalBox();


    /*********************************初始化**********************************/

    public void init() {
//      页面布局定义
        {
        /*标题字体设置*/
        title.setFont(font);
//        定义样式
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        loginPanel.setLayout(new BorderLayout());
        signUpPanel.setLayout(new BorderLayout());
        /*登入页面组装*/
        usernameBox.add(username);
        usernameBox.add(usernameText);
        usernameBox.setPreferredSize(new Dimension(200, 30));
        passwordBox.add(password);
        passwordBox.add(passwordText);
        passwordBox.setPreferredSize(new Dimension(200, 30));
        buttonBox.add(login);
        buttonBox.add(singUp);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        loginPanel.add(title, BorderLayout.NORTH);
        sumaryLoginBox.add(usernameBox);
        sumaryLoginBox.add(passwordBox);
        sumaryLoginBox.add(buttonBox);
        loginPanel.add(sumaryLoginBox, BorderLayout.CENTER);

        /*注册页面组装*/
        SignUpTitle.setFont(signFont);
        signUsernameBox.add(signUsername);
        signUsernameBox.add(signUserText);
        signUsernameBox.setPreferredSize(new Dimension(200,30));
        signPasswordBox.add(signPassword);
        signPasswordBox.add(signPasswordText);
        signPasswordBox.setPreferredSize(new Dimension(200,30));
        signRepasswordBox.add(signRepassword);
        signRepasswordBox.add(signRepasswordText);
        signRepasswordBox.setPreferredSize(new Dimension(200,30));
        signUpPanel.add(SignUpTitle, BorderLayout.NORTH);
        SignUpTitle.setHorizontalAlignment(SwingConstants.CENTER);
        sumarySignUpBox.add(signUsernameBox);
        sumarySignUpBox.add(signPasswordBox);
        sumarySignUpBox.add(signRepasswordBox);
        sumarySignUpBox.add(SignUpButton);
        signUpPanel.add(sumarySignUpBox, BorderLayout.CENTER);

        signUpPanel.add(SignUpTitle,BorderLayout.NORTH);
        signUpPanel.add(sumarySignUpBox,BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);

        }

//      登陆请求
        login.addActionListener(e -> {
            /*数据校验*/
            User user = new User(usernameText.getText(),passwordText.getText());
            if (!user.isTrue()) {
                JOptionPane.showMessageDialog(frame,"账号或者密码错误");
                usernameText.setText("");
                passwordText.setText("");
                return;
            }
            System.out.println("登陆成功");
            /*跳转到主页面*/
            frame.dispose();
            MainFrame mainFrame = new MainFrame();
            mainFrame.run(user);
        });
        frame.getRootPane().setDefaultButton(login);
//      跳转到注册页面
        singUp.addActionListener(e -> {

            frame.remove(loginPanel);
            frame.add(signUpPanel);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);

        });


        SignUpButton.addActionListener(e -> {
            /*存入数据库*/
            if (signRepasswordText.getPassword().length == 0 || signUserText.getText().isBlank() || signUserText.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame,"无效的用户或密码");
                return;
            }
            if (Arrays.equals(signRepasswordText.getPassword(),signPasswordText.getPassword())){
                System.out.println(new User(signUserText.getText(), String.valueOf(signPasswordText.getPassword())).save());
            }else{
                JOptionPane.showMessageDialog(frame,"两次密码不一致");
                return;
            }
            /*返回登入页面*/
            frame.remove(signUpPanel);
            frame.add(loginPanel);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
        });

        /*窗体设置*/
        frame.add(loginPanel);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setIconImage(new ImageIcon("src\\main\\resources\\icon\\logo.png").getImage());
        frame.setTitle("SwingPlayer");
        frame.setVisible(true);
    }

}

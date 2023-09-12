package component;

import Interface.UserInterface;
import object.User;
import proxy.UserProxy;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class LandingFrame {
    JFrame frame = new JFrame();
    JPanel loginPanel = new JPanel();
    JPanel signUpPanel = new JPanel();
    /*登入页面元素*/
    JLabel title = new JLabel("SwingPlayer");
    Font font = new Font("仿宋", Font.PLAIN, 30);
    JLabel username = new JLabel("账号");
    JLabel password = new JLabel("密码");
    JTextField usernameText = new JTextField(10);
    JPasswordField passwordText = new JPasswordField(10);
    JButton login = new JButton("登录");
    JButton singUp = new JButton("注册");
    Box usernameBox = Box.createHorizontalBox();
    Box passwordBox = Box.createHorizontalBox();
    Box buttonBox = Box.createHorizontalBox();
    Box sumaryLoginBox = Box.createVerticalBox();
    /*注册页面元素*/
    JLabel SignUpTitle = new JLabel("注册");
    Font signFont = new Font("仿宋", Font.PLAIN, 20);
    JLabel signUsername = new JLabel("用户名");
    JTextField signUserText = new JTextField(10);
    JLabel signPassword = new JLabel("密码");
    JPasswordField signPasswordText =new JPasswordField(10);
    JLabel signRePassword = new JLabel("确认密码");
    JPasswordField signRePasswordText =new JPasswordField(10);
    JButton submit = new JButton("提交");
    Box signUsernameBox = Box.createHorizontalBox();
    Box signPasswordBox = Box.createHorizontalBox();
    Box signRepasswordBox = Box.createHorizontalBox();
    Box sumarySignUpBox = Box.createVerticalBox();

    /**
     * init()无参函数用于组装页面
     * 使用setLookAndFeel方法实现风格转换
     * 同时实现登录逻辑以及注册逻辑
     */
    public void init() {
        /*样式设置*/
        title.setFont(font);
        loginPanel.setLayout(new BorderLayout());
        signUpPanel.setLayout(new BorderLayout());
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

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
        signRepasswordBox.add(signRePassword);
        signRepasswordBox.add(signRePasswordText);
        signRepasswordBox.setPreferredSize(new Dimension(200,30));
        signUpPanel.add(SignUpTitle, BorderLayout.NORTH);
        SignUpTitle.setHorizontalAlignment(SwingConstants.CENTER);
        sumarySignUpBox.add(signUsernameBox);
        sumarySignUpBox.add(signPasswordBox);
        sumarySignUpBox.add(signRepasswordBox);
        sumarySignUpBox.add(submit);
        signUpPanel.add(sumarySignUpBox, BorderLayout.CENTER);
        signUpPanel.add(SignUpTitle,BorderLayout.NORTH);
        signUpPanel.add(sumarySignUpBox,BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);

        /*窗体设置*/
        frame.getRootPane().setDefaultButton(login);
        frame.add(loginPanel);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setIconImage(new ImageIcon("src\\main\\resources\\icon\\logo.png").getImage());
        frame.setTitle("SwingPlayer");
        frame.setVisible(true);
    }

    /**
     * 构造函数，用于加载按钮事件
     * 登录按钮的事件监听：实现用户登录功能
     * 注册按钮的事件监听：跳转到注册页面
     * 提交按钮的事件监听：实现用户注册功能
     * 注册时验证数据，注册成功后返回登录页面
     */
    public LandingFrame(){
        /*登录按钮*/
        login.addActionListener(e -> {
            User user = new User(usernameText.getText(),String.valueOf(passwordText.getPassword()));
            UserInterface userProxy = UserProxy.createProxy(user);

            if (!userProxy.login()) {
                System.out.println("登录失败");
                JOptionPane.showMessageDialog(frame,"账号或者密码错误");
                usernameText.setText("");
                passwordText.setText("");
                return;
            }
            System.out.println("登录成功");
            frame.dispose();
            MainFrame mainFrame = new MainFrame();
            mainFrame.init(user);
        });

        /*注册按钮*/
        singUp.addActionListener(e -> {
            frame.setResizable(false);
            frame.remove(loginPanel);
            frame.add(signUpPanel);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);

        });

        /*注册提交按钮*/
        submit.addActionListener(e -> {
            /*格式判断*/
            if (signRePasswordText.getPassword().length == 0 || signUserText.getText().isBlank() || signUserText.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame,"无效的用户或密码");
                return;
            }
            /*两次密码判断*/
            if (Arrays.equals(signRePasswordText.getPassword(),signPasswordText.getPassword())){
                User user = new User(signUserText.getText(), String.valueOf(signPasswordText.getPassword()));
                UserInterface userProxy = UserProxy.createProxy(user);
                userProxy.save();
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
    }

}

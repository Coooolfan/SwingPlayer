package object;

import Interface.UserInterface;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;

public class User implements UserInterface {

    private String username;        //用户名
    private String password_MD5;    //加盐密码
    private int UUID;               //用户ID

    /**
     * 构造方法，传入用户名和密码，存储用户信息并加盐密码
     * @param username String 用户名
     * @param password String 用户输入的密码
     */
    public User(String username,String password){
        this.username = username;
        this.password_MD5 = getHash(password);
    }

    /**
     * 构造方法，传入用户名、密码、用户ID，存储用户信息并加盐密码
     * @param username String 用户名
     * @param password String 用户输入的密码
     * @param UUID int 用户ID
     */
    public User(String username, String password, int UUID) {
        this.username = username;
        this.password_MD5 = getHash(password);
        this.UUID = UUID;
    }

    /**
     * 通过UUID获取user信息
     * @param UUID int 用户编号
     * @return User 用户信息
     */
    public User getUserByUUID(int UUID){
        Sqliter sqliter = new Sqliter();
        User user = sqliter.getUserByUUID(UUID);
        sqliter.close();
        return user;
    }

    /**
     * 无参方法用于用户登入
     * @return boolean 登入结果
     */
    public boolean login(){
        Sqliter sqliter=new Sqliter();
        ArrayList<String> usernames= new ArrayList<>();
        usernames.add(this.username);
        ArrayList<User> res = sqliter.getUser(usernames);
        sqliter.close();
        if (res.isEmpty()) return false;
        if(getHash(this.password_MD5).equals(res.get(0).getPassword_MD5())){
            this.UUID = res.get(0).getUUID();
            return true;
        }
        return false;
    }

    /**
     * 跟新账号
     * @return  是否成功
     */
    public boolean updata(){
        try{
            Sqliter sqliter = new Sqliter();
            ArrayList<String> usernames = new ArrayList<>();
            usernames.add(this.username);
            ArrayList<User> res = sqliter.getUser(usernames);
            sqliter.updateUsers(new ArrayList<User>(){{add(User.this);}});
            sqliter.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 用于保存用户，若数据库中已存在用户则修改，若不存在则新建
     * @return boolean 保存操作是否成功
     */
    public boolean save(){
        try{
            Sqliter sqliter = new Sqliter();
            ArrayList<String> usernames = new ArrayList<>();
            usernames.add(this.username);
            ArrayList<User> res = sqliter.getUser(usernames);

            if (res.isEmpty())
                sqliter.saveUsers(new ArrayList<User>(){{add(User.this);}});
            else
                sqliter.updateUsers(new ArrayList<User>(){{add(User.this);}});
            sqliter.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取用户的所有歌单
     * @return ArrayList  &lt;SongList&gt; 歌单列表
     */
    public ArrayList<SongList> getSongLists(){
        Sqliter sqliter = new Sqliter();
        ArrayList<SongList> songLists = sqliter.getSongLists(this);
        sqliter.close();
        return songLists;
    }

    /**
     * 读取用户名
     * @return String 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     * @param username String 输入的用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 设置加盐密码
     * @param password String 密码明文
     * @return 是否成功
     */
    public boolean setPassword(String password){
        this.password_MD5 = getHash(password);
        return this.save();
    }

    /**
     * 获取加盐后密码
     * @return String 加盐密码
     */
    public String getPassword_MD5(){
        return this.password_MD5;
    }

    /**
     * 获取UUID
     * @return int UUID
     */
    public int getUUID() {
        return UUID;
    }

    /**
     * 设置UUID
     * @param UUID int 用户的UUID
     */
    public void setUUID(int UUID) {
        this.UUID = UUID;
    }

    /**
     * 重写了toString方法，将信息转换成字符串形式
     * @return String 转成字符串形式
     */
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password_MD5='" + password_MD5 + '\'' +
                ", UUID=" + UUID +
                '}';
    }

    /**
     * 密码加盐
     * @param password String 密码明文
     * @return String 加盐结果
     */
    public static String getHash(String password){
         password = password + "YT2DIYTD25HFKUY";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes)
                sb.append(String.format("%02x", b));
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}

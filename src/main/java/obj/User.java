package obj;

import api.Sqliter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;

public class User {

    private String username;
    private String password_MD5;
    private int UUID;


    public User(String username, String password, int UUID) {
        this.username = username;
        this.password_MD5 = getHash(password);
        this.UUID = UUID;
    }

    public static User getUserByUUID(int UUID){
        Sqliter sqliter = new Sqliter();
        User user = sqliter.getUserByUUID(UUID);
        sqliter.close();
        return user;
    }

    public User(String username,String password){
        this.username = username;

//      仅存储加盐后的MD5值
        this.password_MD5 = getHash(password);

    }
    public boolean isTrue(){
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


    public boolean save(){
        try{
//        先检查用户在数据库中已经存在
            Sqliter sqliter = new Sqliter();
            ArrayList<String> usernames = new ArrayList<>();
            usernames.add(this.username);
            ArrayList<User> res = sqliter.getUser(usernames);
            if (res.isEmpty()) {
                //如果不存在：新建
                System.out.println("新建了一个用户");
                sqliter.saveUsers(new ArrayList<User>(){{add(User.this);}});
            } else {
//        如果存在：修改
                sqliter.updateUsers(new ArrayList<User>(){{add(User.this);}});
            }
            sqliter.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<SongList> getSongLists(){
        Sqliter sqliter = new Sqliter();
        ArrayList<SongList> songLists = sqliter.getSongLists(this);
        sqliter.close();
        return songLists;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private void setPassword_MD5(String password_MD5){
        this.password_MD5 = password_MD5;
    }

    public boolean setPassword(String password){
        this.password_MD5 = getHash(password);
        return this.save();
    }

    public String getPassword_MD5(){
        return this.password_MD5;
    }

    public int getUUID() {
        return UUID;
    }

    public void setUUID(int UUID) {
        this.UUID = UUID;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password_MD5='" + password_MD5 + '\'' +
                ", UUID=" + UUID +
                '}';
    }

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

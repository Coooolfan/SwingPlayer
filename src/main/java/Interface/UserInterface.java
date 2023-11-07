package Interface;

import object.SongList;
import object.User;

import java.util.ArrayList;

public interface UserInterface {

    User getUserByUUID(int UUID);
    boolean login();
    boolean save();
    String getUsername();
    ArrayList<SongList> getSongLists();
    void setUsername(String username);
    boolean setPassword(String password);
    String getPassword_MD5();
    int getUUID();
    void setUUID(int UUID);
    boolean updata();
}

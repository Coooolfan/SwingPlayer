package Interface;

import object.Song;
import java.nio.file.Path;
import java.util.ArrayList;

public interface SongListInterface {
    boolean add(Song song);
    ArrayList<Song> getSongs();
    boolean save();
    boolean removeSong(int songID);
    boolean remove();
    boolean isLibrary();
    void setLibrary(boolean library);
    String getName();
    void setName(String name);
    String getDes();
    void setDes(String des);
    void setUUID(int UUID);
    Path getIcon() ;
    void setIcon(Path icon) ;
    int getListID();
    void setListID(int listID);
    int getUUID();

}

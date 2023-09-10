package Interface;

import java.nio.file.Path;

public interface SongInterface {
    boolean save();
    boolean remove();
    int getSongID();
    void setSongID(int songID);
    Path getIcon();
    void setIcon(Path icon);
    Path getFile();
    void setFile(Path file);
    String getName();
    void setName(String name);
    String getSinger();
    void setSinger(String singer);
    String getAlbum();
    void setAlbum(String album);
}

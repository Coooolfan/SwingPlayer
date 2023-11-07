package Interface;

import object.Song;
import object.SongList;
import object.User;
import java.util.ArrayList;

public interface SqliterInterface {
    void close();
    ArrayList<User> getUser(ArrayList<String> usernames) ;
    User getUserByUUID(int UUID);
    ArrayList<Song> getSongs(int listID);
    ArrayList<Song> getSongs();
    ArrayList<SongList> getSongLists(User user);
    boolean saveUsers(ArrayList<User> users);
    boolean updateUsers(ArrayList<User> users);
    boolean createMark(Song song, SongList songList,int uuid) ;
    boolean createSongList(SongList songList);
    boolean removeSongInSongList(SongList songList, int songID);
    boolean removeSongList(SongList songList);
    boolean createSong(Song song);
    boolean removeSong(Song song);
    boolean removeMarkBySong(int songID);
    boolean removeMarkBySongList(int SongListID);
    SongList getSongListByListID(int listID);
}

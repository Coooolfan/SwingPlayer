package object;

import Interface.SqliterInterface;

import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

public class Sqliter implements SqliterInterface {
    Connection c = null;
    Statement stmt = null;

    /**
     * 构造方法
     * 连接数据库
     */
    public Sqliter() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:sqlite.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * 数据库断开连接
     */
    public void close() {
        try {
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * 通过用户名获取所有用户
     * @param usernames ArrayList &lt;String&qt; 用户名
     * @return  ArrayList &lt;User&qt; 用户列表
     */
    public ArrayList<User> getUser(ArrayList<String> usernames) {
        ArrayList<User> resUsers = new ArrayList<>();
        for (String username : usernames) {
            try {
                String sql = "SELECT * FROM user WHERE name=?";
                PreparedStatement preparedStatement = c.prepareStatement(sql);
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    User user = new User(resultSet.getString("name"), resultSet.getString("password"), resultSet.getInt("UUID"));
                    resUsers.add(user);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return resUsers;
    }

    /**
     * 通过UUID获取用户
     * @param UUID int 用户UUID
     * @return User 用户
     */
    public User getUserByUUID(int UUID){
        try{
            String sql = "SELECT * FROM user WHERE UUID=?";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setInt(1,UUID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                return new User(resultSet.getString("name"), resultSet.getString("password"), resultSet.getInt("UUID"));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 通过歌单ID获取歌曲
     * @param listID    int 歌单ID
     * @return  ArrayList &lt;Song&qt; 歌曲列表
     */
    public ArrayList<Song> getSongs(int listID) {
        try {
            String sql = "SELECT * FROM mark WHERE listID=?";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setInt(1, listID);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Integer> songsID = new ArrayList<>();
            while (resultSet.next())
                songsID.add(resultSet.getInt("songID"));
            sql = "SELECT * FROM song WHERE songID=?";
            preparedStatement = c.prepareStatement(sql);
            ArrayList<Song> resSongs = new ArrayList<>();
            for (int songID : songsID) {
                preparedStatement.setInt(1, songID);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next())
                    resSongs.add(new Song(
                            resultSet.getString("name"),
                            resultSet.getInt("songID"),
                            Paths.get(resultSet.getString("icon")),
                            Paths.get(resultSet.getString("file")),
                            resultSet.getString("singer"),
                            resultSet.getString("album")
                    ));
            }
            return resSongs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取全部的歌曲
     * @return 所有歌曲
     */
    public ArrayList<Song> getSongs() {
        try {
            String sql = "SELECT * FROM song";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Song> resSongs = new ArrayList<>();
            while (resultSet.next()) {
                resSongs.add(new Song(
                        resultSet.getString("name"),
                        resultSet.getInt("songID"),
                        Paths.get(resultSet.getString("icon")),
                        Paths.get(resultSet.getString("file")),
                        resultSet.getString("singer"),
                        resultSet.getString("album")
                ));
            }
            return resSongs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过用户获取歌单
     * @param user User 用户
     * @return ArrayList &lt;SongList&qt; 歌单列表
     */
    public ArrayList<SongList> getSongLists(User user) {
        int userUUID = user.getUUID();
        System.out.println(userUUID);
        try {
            String sql = "SELECT * FROM songlist WHERE userID = ?";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setInt(1, userUUID);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<SongList> resSingLists = new ArrayList<>();
            while (resultSet.next())
                resSingLists.add(new SongList(
                        resultSet.getString("name"),
                        resultSet.getString("des"),
                        resultSet.getInt("userID"),
                        Paths.get(resultSet.getString("icon")),
                        resultSet.getInt("listID")
                ));
            return resSingLists;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 插入用户
     * @param users ArrayList&lt;User&qt; 用户列表
     * @return  是否成功
     */
    public boolean saveUsers(ArrayList<User> users) {
        try {
            for (User user : users) {
                System.out.println("数据库插入+");
                String sql = "INSERT INTO user (name, password) VALUES (?,?)";
                PreparedStatement preparedStatement = c.prepareStatement(sql);
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword_MD5());
                System.out.println(preparedStatement.executeUpdate());
            }
            c.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新用户
     * @param users ArrayList &lt;User&qt; 用户列表
     * @return  是否成功
     */
    public boolean updateUsers(ArrayList<User> users) {
        try {
            for (User user : users) {
                String sql = "UPDATE user set name=?,password=? WHERE UUID=?";
                PreparedStatement preparedStatement = c.prepareStatement(sql);
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword_MD5());
                preparedStatement.setInt(3, user.getUUID());
                int i = preparedStatement.executeUpdate();
                c.commit();
                return i > 0;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 建立ID之间的联系
     * @param song  Song 歌曲
     * @param songList  SongList 歌单
     * @param uuid  用户UUID
     * @return  boolean 是否成功
     */
    public boolean createMark(Song song, SongList songList,int uuid) {
        try {
            String sql = "INSERT INTO mark (songID,listID,userID) VALUES (?,?,?)";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setInt(1, song.getSongID());
            preparedStatement.setInt(2, songList.getListID());
            preparedStatement.setInt(3, uuid);
            int i = preparedStatement.executeUpdate();
            c.commit();
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建歌单
     * @param songList  SongList 歌单
     * @return  boolean 是否成功
     */
    public boolean createSongList(SongList songList) {
        try {
            String sql = "INSERT INTO songlist (name,des,userID,icon) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setString(1, songList.getName());
            preparedStatement.setString(2, songList.getDes());
            preparedStatement.setInt(3, songList.getUUID());
            preparedStatement.setString(4, songList.getIcon().toString());
            int i = preparedStatement.executeUpdate();
            c.commit();
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从歌单中移除歌曲
     * @param songList  SongList 歌单
     * @param songID  int 歌曲ID
     * @return boolean 是否成功
     */
    public boolean removeSongInSongList(SongList songList, int songID) {
        try {
            String sql = "DELETE FROM mark WHERE songID=? AND listID=?";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setInt(1, songID );
            preparedStatement.setInt(2, songList.getListID());
            int i = preparedStatement.executeUpdate();
            c.commit();
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除歌单
     * @param songList SongList 歌单
     * @return boolean 是否成功
     */
    public boolean removeSongList(SongList songList) {
        removeMarkBySongList(songList.getListID());
        try {
            String sql = "DELETE FROM songlist WHERE listID=?";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setInt(1, songList.getListID());
            int i = preparedStatement.executeUpdate();
            c.commit();
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建歌曲
     * @param song Song 歌曲
     * @return  boolean 是否成功
     */
    public boolean createSong(Song song) {
        try {
            String sql = "INSERT INTO song (name,icon,file,singer,album) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setString(1, song.getName());
            preparedStatement.setString(2, song.getIcon().toString());
            preparedStatement.setString(3, song.getFile().toString());
            preparedStatement.setString(4, song.getSinger());
            preparedStatement.setString(5, song.getAlbum());
            int i = preparedStatement.executeUpdate();
            c.commit();
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除歌曲
     * @param song Song 歌曲
     * @return boolean 是否成功
     */
    public boolean removeSong(Song song) {
        removeMarkBySong(song.getSongID());
        try {
            String sql = "DELETE FROM song WHERE songID=?";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setInt(1, song.getSongID());
            int i = preparedStatement.executeUpdate();
            c.commit();
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过歌曲删除mark
     * @param songID int 歌曲ID
     * @return boolean 是否成功
     */
    public boolean removeMarkBySong(int songID){
        try {
            String sql = "DELETE FROM mark WHERE songID=?";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setInt(1, songID);
            int i = preparedStatement.executeUpdate();
            c.commit();
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过歌单ID删除mark
     * @param SongListID int 歌单ID
     * @return boolean 是否成功
     */
    public boolean removeMarkBySongList(int SongListID){
        try{
            String sql = "DELETE FROM mark WHERE listID=?";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setInt(1,SongListID);
            int i = preparedStatement.executeUpdate();
            c.commit();
            return i > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过歌单ID获取歌曲
     * @param listID int 歌单ID
     * @return SongList 歌单
     */
    public SongList getSongListByListID(int listID) {
        try {
            String sql = "SELECT * FROM songlist WHERE listID=?";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setInt(1, listID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                return new SongList(
                        resultSet.getString("name"),
                        resultSet.getString("des"),
                        resultSet.getInt("userID"),
                        Paths.get(resultSet.getString("icon")),
                        resultSet.getInt("listID")
                );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
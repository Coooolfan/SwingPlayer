package api;

import obj.Song;
import obj.SongList;
import obj.User;

import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

public class Sqliter {
    Connection c = null;
    Statement stmt = null;

    //  构造函数，初始化数据库连接
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

    public void close() {
        try {
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

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

    public ArrayList<Song> getSongs(int listID) {
        try {
//            先获取到所有的歌曲ID
            String sql = "SELECT * FROM mark WHERE listID=?";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setInt(1, listID);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Integer> songsID = new ArrayList<>();
            while (resultSet.next())
                songsID.add(resultSet.getInt("songID"));
//            再从song表中查询所有歌曲
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

    public ArrayList<Song> getSongs(boolean ALLSongs) {
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

    public boolean createMark(Song song, SongList songList) {
        try {
            String sql = "INSERT INTO mark (songID,listID) VALUES (?,?)";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setInt(1, song.getSongID());
            preparedStatement.setInt(2, songList.getListID());
            int i = preparedStatement.executeUpdate();
            c.commit();
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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

    public boolean removeSongInSongList(SongList songList, int songID) {
        try {
            String sql = "DELETE FROM mark WHERE songID=? AND listID=?";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
//            preparedStatement.setInt(1, songList.getListID());
//            preparedStatement.setInt(2, songID);
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
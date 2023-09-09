package obj;

import api.Sqliter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SongList {

    private String name;
    private String des;
    private int UUID;
    private Path icon;
    private int listID;
    private boolean isLibrary = false;

    public SongList(String name, String des, int UUID, Path icon, int listID) {
        this.name = name;
        this.des = des;
        this.UUID = UUID;
        this.icon = icon;
        this.listID = listID;
    }

    public SongList(String name, String des, int UUID, Path icon) {
        this.name = name;
        this.des = des;
        this.UUID = UUID;
        this.icon = icon;
    }

    public boolean add(Song song){
        if (this.isLibrary)
            return false;
        Sqliter sqliter = new Sqliter();
        boolean mark = sqliter.createMark(song, this);
        sqliter.close();
        return mark;
    }
    public static SongList getLibrary(){
        SongList  songList = new SongList("资源库","SwingPlayer所管理的所有资源",-1, Paths.get("src\\main\\resources\\icon\\logo.png"),-1);
        songList.setLibrary(true);
        return  songList;
    }

    public static SongList getSongListByListID(int listID){
        Sqliter sqliter = new Sqliter();
        SongList songList = sqliter.getSongListByListID(listID);
        sqliter.close();
        return songList;
    }
    public ArrayList<Song> getSongs(){
        Sqliter sqliter = new Sqliter();
        if (this.isLibrary) {
            ArrayList<Song> songs = sqliter.getSongs(true);
            sqliter.close();
            return songs;
        } else {
            ArrayList<Song> songs = sqliter.getSongs(this.listID);
            sqliter.close();
            return songs;
        }
    }

    public boolean save(){
        Sqliter sqliter = new Sqliter();
//        检查成员变量是否合法
        if (this.name.isEmpty() || this.des.isEmpty() || !this.icon.toFile().isFile()|| this.isLibrary)
            return false;
        boolean songList = sqliter.createSongList(this);
        sqliter.close();
        return songList;
    }

    public boolean removeSong(int songID){
        Sqliter sqliter = new Sqliter();
        boolean b = sqliter.removeSongInSongList(this, songID);
        sqliter.close();
        return b;
    }

    public boolean remove(){
//        检查成员变量是否合法
        if (this.name == null || this.des == null || this.icon == null || this.UUID == 0|| this.isLibrary)
            return false;
        Sqliter sqliter = new Sqliter();
        boolean b = sqliter.removeSongList(this);
        sqliter.close();
        return b;
    }

    public boolean isLibrary() {
        return isLibrary;
    }

    public void setLibrary(boolean library) {
        isLibrary = library;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setUUID(int UUID) {
        this.UUID = UUID;
    }

    public Path getIcon() {
        return icon;
    }

    public void setIcon(Path icon) {
        this.icon = icon;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public int getUUID() {
        return UUID;
    }

    @Override
    public String toString() {
        return "SongList{" +
                "name='" + name + '\'' +
                ", des='" + des + '\'' +
                ", UUID='" + UUID + '\'' +
                ", icon=" + icon +
                ", listID=" + listID +
                '}';
    }
}

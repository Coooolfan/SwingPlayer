package obj;

import api.Sqliter;

import java.nio.file.Path;

public class Song {
    private String name;
    private int songID;
    private Path icon;
    private Path file;
    private String singer;
    private String album; //专辑

    public Song(String name, Path icon, Path file, String singer, String album) {
        this.name = name;
        this.songID = -1;
        this.icon = icon;
        this.file = file;
        this.singer = singer;
        this.album = album;
    }
    public Song(String name, int songID, Path icon, Path file, String singer, String album) {
        this.name = name;
        this.songID = songID;
        this.icon = icon;
        this.file = file;
        this.singer = singer;
        this.album = album;
    }
    public boolean addToSongList(SongList songList){
        Sqliter sqliter = new Sqliter();
        boolean mark = sqliter.createMark(this, songList);
        sqliter.close();
        return mark;

    }

    public boolean save(){
//        检查成员变量是否合法
        if (this.name == null || this.icon == null || this.file == null || this.singer == null || this.album == null)
            return false;
        Sqliter sqliter = new Sqliter();
        boolean song = sqliter.createSong(this);
        sqliter.close();
        return song;
    }

    public boolean remove(){
        Sqliter sqliter = new Sqliter();
        boolean b = sqliter.removeSong(this);
        sqliter.close();
        return b;
    }
    public int getSongID() {
        return songID;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }

    public Path getIcon() {
        return icon;
    }

    public void setIcon(Path icon) {
        this.icon = icon;
    }

    public Path getFile() {
        return file;
    }

    public void setFile(Path file) {
        this.file = file;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

}

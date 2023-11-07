package object;

import Interface.SongListInterface;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SongList implements SongListInterface {

    private String name;            //歌单名
    private String des;             //歌单描述
    private int UUID;               //歌单所属用户ID
    private Path icon;              //歌单图标
    private int listID;             //歌单ID
    private boolean isLibrary = false;          //资源库标记

    /**
     * 构造函数
     * @param name String 歌单名
     * @param des   String 歌单描述
     * @param UUID  int 歌单所属用户ID
     * @param icon  Path 歌单图标路径
     * @param listID    int 歌单ID
     */
    public SongList(String name, String des, int UUID, Path icon, int listID) {
        this.name = name;
        this.des = des;
        this.UUID = UUID;
        this.icon = icon;
        this.listID = listID;
    }

    /**
     * 构造函数
     * @param name String 歌单名
     * @param des   String 歌单描述
     * @param UUID  int 歌单所属用户ID
     * @param icon  Path 歌单图标路径
     */
    public SongList(String name, String des, int UUID, Path icon) {
        this.name = name;
        this.des = des;
        this.UUID = UUID;
        this.icon = icon;
    }

    /**
     * 歌单添加歌曲
     * @param song 需要添加的歌曲
     * @return  是否成功
     */
    public boolean add(Song song){
        if (this.isLibrary)
            return false;
        Sqliter sqliter = new Sqliter();
        boolean mark = sqliter.createMark(song, this, this.getUUID());
        sqliter.close();
        return mark;
    }

    /**
     * 设置资源库
     * @return SongList 资源库信息
     */
    public static SongList getLibrary(){
        SongList  songList = new SongList("资源库",
                "SwingPlayer所管理的所有资源",-1,
                Paths.get("src\\main\\resources\\icon\\logo.png"),-1);
        songList.setLibrary(true);
        return  songList;
    }

    /**
     * 通过歌单ID获取歌单
     * @param listID  int 歌单ID
     * @return  SongList 歌单信息
     */
    public static SongList getSongListByListID(int listID){
        Sqliter sqliter = new Sqliter();
        SongList songList = sqliter.getSongListByListID(listID);
        sqliter.close();
        return songList;
    }

    /**
     * 获取歌单中的歌曲
     * @return ArrayList&lt;Song&gt; 返回歌曲列表
     */
    public ArrayList<Song> getSongs(){
        Sqliter sqliter = new Sqliter();
        if (this.isLibrary) {
            ArrayList<Song> songs = sqliter.getSongs();
            sqliter.close();
            return songs;
        } else {
            ArrayList<Song> songs = sqliter.getSongs(this.listID);
            sqliter.close();
            return songs;
        }
    }

    /**
     * 上传歌曲时，将歌曲保存到数据库
     * @return boolean 是否成功
     */
    public boolean save(){
        Sqliter sqliter = new Sqliter();
        if (this.name.isEmpty() || this.des.isEmpty() || !this.icon.toFile().isFile()|| this.isLibrary)
            return false;
        boolean res = sqliter.createSongList(this);
        sqliter.close();
        return res;
    }

    /**
     * 移除歌曲
     * @param songID int 歌曲ID
     * @return boolean 是否成功
     */
    public boolean removeSong(int songID){
        Sqliter sqliter = new Sqliter();
        boolean res = sqliter.removeSongInSongList(this, songID);
        sqliter.close();
        return res;
    }

    /**
     * 删除歌单
     * @return boolean 是否成功
     */
    public boolean remove(){
        if (this.name == null || this.des == null || this.icon == null || this.UUID == 0|| this.isLibrary)
            return false;
        Sqliter sqliter = new Sqliter();
        boolean res = sqliter.removeSongList(this);
        sqliter.close();
        return res;
    }

    /**
     * 返回是否为资源库
     * @return boolean 是否为资源库
     */
    public boolean isLibrary() {
        return isLibrary;
    }

    /**
     * 设置为资源库
     * @param library boolean 资源库标志
     */
    public void setLibrary(boolean library) {
        isLibrary = library;
    }

    /**
     * 获取歌单名
     * @return String 歌单名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置歌单名
     * @param name String 歌单名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取歌单描述
     * @return String 歌单描述
     */
    public String getDes() {
        return des;
    }

    /**
     * 设置歌单描述
     * @param des String 描述内容
     */
    public void setDes(String des) {
        this.des = des;
    }

    /**
     * 设置歌单所属用户
     * @param UUID int 用户ID
     */
    public void setUUID(int UUID) {
        this.UUID = UUID;
    }

    /**
     * 获取图标路径
     * @return 图标路径
     */
    public Path getIcon() {
        return icon;
    }

    /**
     * 设置图标路径
     * @param icon 图标路径
     */
    public void setIcon(Path icon) {
        this.icon = icon;
    }

    /**
     * 获取歌单ID
     * @return 歌单ID
     */
    public int getListID() {
        return listID;
    }

    /**
     * 设置歌单ID
     * @param listID int 歌单ID
     */
    public void setListID(int listID) {
        this.listID = listID;
    }

    /**
     * 获取歌单所属用户ID
     * @return 用户ID
     */
    public int getUUID() {
        return UUID;
    }

    /**
     * 歌单信息转换为字符串
     * @return String 歌单信息
     */
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

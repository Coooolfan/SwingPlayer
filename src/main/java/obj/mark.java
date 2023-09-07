package obj;

public class mark {
    private Song song;
    private SongList songList;

    public mark(Song song, SongList songList) {
        this.song = song;
        this.songList = songList;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public SongList getSongList() {
        return songList;
    }

    public void setSongList(SongList songList) {
        this.songList = songList;
    }
}

package Interface;

import object.Song;

public interface AudioManagerInterface {
    boolean setSong(Song song);
    boolean start();
    boolean isPlaying();
    boolean pause();
}

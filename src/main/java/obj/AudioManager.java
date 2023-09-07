package obj;

import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;

public class AudioManager {
    private static AudioManager uniqueInstance = new AudioManager();
	private AudioManager(){}
	public static AudioManager getInstance() {
		return uniqueInstance;
	}

	private StreamPlayer streamPlayer = new StreamPlayer();
	public  boolean setSong(Song song){
		try{
			streamPlayer.stop();
			streamPlayer.open(song.getFile().toFile());
			return true;
		}catch (StreamPlayerException e){
			e.printStackTrace();
			return false;
		}
	}

	public boolean start(){
		try{
			if (streamPlayer.isPaused()) {
				System.out.println("执行：从暂停时播放");
				streamPlayer.resume();
			}else {
				System.out.println("执行：播放新歌曲");
				streamPlayer.play();
			}
			return true;
		}catch (StreamPlayerException e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean isPlaying(){
		return streamPlayer.isPlaying();
	}
	public boolean pause(){

		System.out.println("isPlaying" + streamPlayer.isPlaying());
		if (streamPlayer.isPlaying()) {
			streamPlayer.pause();
		}
		return true;
	}
}

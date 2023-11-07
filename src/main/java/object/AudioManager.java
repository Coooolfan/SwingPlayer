package object;

import Interface.AudioManagerInterface;
import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;

public class AudioManager implements AudioManagerInterface {
    private static AudioManager uniqueInstance = new AudioManager();
	private AudioManager(){}
	public static AudioManager getInstance() {
		return uniqueInstance;
	}
	private StreamPlayer streamPlayer = new StreamPlayer();

	/**
	 * 设置播放歌曲
	 * @param song Song 歌曲信息
	 * @return	boolean 是否成功
	 */
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

	/**
	 * 播放歌曲
	 * @return boolean 是否播放成功
	 */
	public boolean start(){
		try{
			if (streamPlayer.isPaused()) {
//				System.out.println("执行：从暂停时播放");
				streamPlayer.resume();
			}else {
//				System.out.println("执行：播放新歌曲");
				streamPlayer.play();
			}
			return true;
		}catch (StreamPlayerException e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 状态检测
	 * @return boolean 是否正在播放歌曲
	 */
	public boolean isPlaying(){
		return streamPlayer.isPlaying();
	}

	/**
	 * 暂停歌曲
	 * @return boolean 是否暂停成功
	 */
	public boolean pause(){
		if (streamPlayer.isPlaying()) {
			streamPlayer.pause();
		}
		return true;
	}
}

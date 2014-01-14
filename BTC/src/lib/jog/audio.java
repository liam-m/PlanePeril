package lib.jog;

import java.io.IOException;

import org.lwjgl.openal.AL;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

/**
 * <h1>jog.audio</h1>
 * <p>Provides a layer upon OpenGL methods. jog.graphics allows drawing basic shapes to the screen,
 * as well as images and limited font capabilities. jog.graphics (unlike OpenGL) has the graphical origin to be the window's
 * upper-left corner.</p>
 * @author IMP1
 */
public abstract class audio {
	
	/**
	 * <h1>jog.audio.Music</h1>
	 * <p>Essentially an object-orientated wrapper for the slick Music object.</p>
	 * @author IMP1
	 */
	public static class Music {
		
		private Audio _source;
		private boolean _looping;
		private boolean _isPaused;
		private float _volume;
		private float _pitch;
		private float _pausedPosition;
		
		/**
		 * Constructor for a music source.
		 * @param filepath the path to the audio file.
		 * @param stream whether to load the music as it's playing.
		 * @param looping whether to loop the music.
		 */
		private Music(String filepath, boolean stream, boolean looping) {
			_looping = looping;
			_volume = 1f;
			_pitch = 1f;
			try {
				if (stream) {
					_source = AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource(filepath));
				} else {
					_source = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(filepath));
				}
				_isPaused = false;
				_pausedPosition = 0;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Begins playback of the music.
		 */
		public void play() {
			if (_isPaused) {
				resume();
			} else {
				_source.playAsMusic(_pitch, _volume, _looping);
			}
		}
		
		/**
		 * Stops playback of the music.
		 */
		public void stop() {
			_source.stop();
		}
		
		/**
		 * Pauses playback of the music.
		 */
		public void pause() {
			_pausedPosition = tell();
			_source.stop();
			_isPaused = true;
		}
		
		/**
		 * Unpauses playback of the music.
		 */
		public void resume() {
			_source.playAsMusic(_pitch, _volume, _looping);
			seek(_pausedPosition);
			_isPaused = false;
		}

		/**
		 * Gets how far in, in seconds, the playback is of the music source. 
		 * @return seconds into the music.
		 */
		public float tell() {
			return _source.getPosition();
		}
		
		/**
		 * Sets the positions of the playback through the music source.
		 * @param position the location through the music.
		 */
		public void seek(float position) {
			_source.setPosition(position);
		}
		
		/**
		 * Sets the volume for the music to be played at.
		 * @param volume the volume at which to play the music.
		 */
		public void setVolume(float volume) {
			float position = tell();
			_source.stop();
			_source.playAsMusic(_pitch, volume, _looping);
			seek(position);
			_volume = volume;
		}
		
		/**
		 * Gets the current volume the music is being played at.
		 * @return the current volume.
		 */
		public float getVolume() {
			return _volume;
		}
		
	}
	
	public static class Sound {
		
		private Audio _source;
		private float _volume;
		private float _pitch;
		
		/**
		 * Constructor for a music source.
		 * @param filepath the path to the audio file.
		 */
		private Sound(String filepath) {
			_volume = 1f;
			_pitch = 1f;
			try {
				_source = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(filepath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Begins playback of the music.
		 */
		public void play() {
			_source.playAsSoundEffect(_pitch, _volume, false);
		}
		
		/**
		 * Stops playback of the sound effect.
		 */
		public void stop() {
			_source.stop();
		}
		
		/**
		 * Sets the volume for the music to be played at.
		 * @param volume the volume at which to play the music.
		 */
		public void setVolume(float volume) {
			_volume = volume;
		}
		
		/**
		 * Gets the current volume the music is being played at.
		 * @return the current volume.
		 */
		public float getVolume() {
			return _volume;
		}
	}
	
	public static Music newMusic(String filepath, boolean stream, boolean loop) {
		return new Music(filepath, stream, loop);
	}
	public static Music newMusic(String filepath) { return newMusic(filepath, true, true); }
	
	public static Sound newSoundEffect(String filepath) {
		return new Sound(filepath);
	}
	
	public static Audio newAudio(String filepath, boolean stream) throws IOException {
		if (stream) { 
			return AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource(filepath));
		} else {
			return AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(filepath));
		}
	}
	
	public static void update() {
		SoundStore.get().poll(0);
	}
	
	public static void dispose() {
		AL.destroy();
	}
	
}

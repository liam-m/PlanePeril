package lib.jog;

import org.lwjgl.openal.AL;
import org.newdawn.slick.SlickException;

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
		
		private org.newdawn.slick.Music _source;
		private boolean _looping;
		private boolean _isPaused;
		private float _volume;
		private float _pitch;
		
		/**
		 * Constructor for a music source.
		 * @param filepath the path to the image file.
		 * @param stream whether to load the music as it's playing.
		 * @param looping whether to loop the music.
		 */
		private Music(String filepath, boolean stream, boolean looping) {
			_looping = looping;
			_volume = 1f;
			_pitch = 1f;
			try {
				_source = new org.newdawn.slick.Music(filepath, stream);
				_isPaused = false;
			} catch (SlickException e) {
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
				if (_looping) {
					_source.loop(_pitch, _volume);
				} else {
					_source.play(_pitch, _volume);
				}
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
			_source.pause();
			_isPaused = true;
		}
		
		/**
		 * Unpauses playback of the music.
		 */
		public void resume() {
			_source.resume();
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
			_source.setVolume(volume);
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
	
	
	public static class SoundEffect {
		
		private org.newdawn.slick.Sound _source;
		private boolean _looping;
		private float _volume;
		private float _pitch;
		
		private SoundEffect(String filepath, boolean looping) {
			_looping = looping;
			_volume = 1f;
			_pitch = 1f;
			try {
				_source = new org.newdawn.slick.Sound(filepath);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		
		public void play() {
			if (_looping) {
				_source.loop(_pitch, _volume);
			} else {
				_source.play(_pitch, _volume);
			}
		}

		public void stop() {
			_source.stop();
		}

		public void setVolume(float volume) {
			_volume = volume;
		}
		
		public float getVolume() {
			return _volume;
		}
		
	}
	
	public static SoundEffect newSoundEffect(String filepath, boolean loop) {
		return new SoundEffect(filepath, loop);
	}
	public static SoundEffect newSoundEffect(String filepath) { return newSoundEffect(filepath, false); }
	
	
	public static Music newMusic(String filepath, boolean stream, boolean loop) {
		return new Music(filepath, stream, loop);
	}
	public static Music newMusic(String filepath) { return newMusic(filepath, true, true); }
	
	public static void dispose() {
		AL.destroy();
	}
	
}
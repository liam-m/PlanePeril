package lib.jog;

import org.lwjgl.openal.AL;
import org.newdawn.slick.SlickException;

public abstract class audio {
	
	public static class Music {
		
		private org.newdawn.slick.Music _source;
		private boolean _looping;
		private boolean _isPaused;
		private float _volume;
		private float _pitch;
		
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
		
		public void stop() {
			_source.stop();
		}
		
		public void pause() {
			_source.pause();
			_isPaused = true;
		}
		
		public void resume() {
			_source.resume();
			_isPaused = false;
		}
		
		public void fade(float duration, float endVolume, boolean stopAfterFade) {
			int dur = (int)(duration * 1000);
			_source.fade(dur, endVolume, stopAfterFade);
		}
		
		public float tell() {
			return _source.getPosition();
		}
		
		public void seek(float position) {
			_source.setPosition(position);
		}
		
		public void setVolume(float volume) {
			_source.setVolume(volume);
			_volume = volume;
		}
		
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
/*
 * Clock.java
 * Created on 21-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis;

/**
 * Clock provides timing for the game.
 * @author Bruce.Porteous
 *
 */
public class Clock {

	private long startTime;
	private long pausedTime;
	private long pausedStart;
	boolean paused;
	
	/**
	 * 
	 */
	public Clock() {
		super();
		reset();
	}

	public void reset(){
		startTime = System.currentTimeMillis();
		pausedTime = 0;
		paused = false;
	}
	
	public long getTime(){
		long now = System.currentTimeMillis();
		return now - (startTime + pausedTime);
	}
	
	/**
	 * @return Returns the paused.
	 */
	public boolean isPaused() {
		return paused;
	}
	
	/**
	 * @param paused The paused to set.
	 */
	public void setPaused(boolean paused) {
		long now = System.currentTimeMillis();
		if(this.paused){
			pausedTime += (now - pausedStart);
		} else {
			pausedStart = now;
		}
		this.paused = paused;
	}
}

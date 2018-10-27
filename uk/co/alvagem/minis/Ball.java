/*
 * Ball.java
 * Created on 17-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Ball
 * @author Bruce.Porteous
 *
 */
public class Ball {

	private float x,y;
	private float vx,vy;
	
	/** Height of ball above ground in m */
	private float z; // maybe
	
	/** Vertical speed, in ms, up is +ve. */
	private float vz;
	
	/** True if ball is being carried by a player */
	private boolean carried;
	
	/**
	 * 
	 */
	public Ball() {
		super();
		x = 0;
		y = 0;
		vx = 0;
		vy = 0;
		z = 0;
		vz = 0;
		carried = false;
	}

	/**
	 * @param g2d
	 * @param scale
	 */
	public void draw(Graphics2D g2d, float scale) {
		int ix = (int)(x * scale + 0.5);
		int iy = (int)(y * scale + 0.5);
		
		int size = (int)(0.3 * scale);
		
		g2d.setColor(Color.black);
		g2d.fillOval(ix,iy,size,size);
		
	}

	/**
	 * @param f
	 * @param g
	 */
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		
	}

	/**
	 * Gets the X position of the ball.
	 * @return
	 */
	public float getXPosition() {
		return x;
	}

	/**
	 * Gets the Y position of the ball.
	 * @return
	 */
	public float getYPosition() {
		return y;
	}

	/**
	 * @param speed
	 * @param dir
	 */
	public void pass(float speed, float dir) {
		vx = speed * (float)Math.cos(dir);
		vy = speed * (float)Math.sin(dir);
		z = 1.0f;
		vz = 1.0f; // up.
		carried = false;
	}
	
	public void pickUp(){
		vz = 0;
		vx = 0;
		vy = 0;
		carried = true;
	}
	
	public void move(Game game, float interval){
		if(!carried){
			x += (vx * interval);
			y += (vy * interval);
			final float G = 9.81f;
			vz -= G * interval; 
			z += (vz * interval);
			if(z <= 0){
				// Bounce up with reduced vertical
				z = 0.0f;
				vz = -vz * 0.7f; 
				
				float speed = (float)Math.sqrt(vx * vx + vy * vy);
				float dir = (float)Math.atan2(vy,vx);
				
				// Bounce up with reduced speed and perturbed direction
				speed *= 0.6f;
				dir *= (1.0 + 0.5 * game.getRandom().nextGaussian()); 
				vx = speed * (float)Math.cos(dir);
				vy = speed * (float)Math.sin(dir);
			}
		}
		
	}

}

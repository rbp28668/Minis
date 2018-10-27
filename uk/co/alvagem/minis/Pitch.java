/*
 * Pitch.java
 * Created on 17-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Pitch
 * @author Bruce.Porteous
 *
 */
public class Pitch {

	private static float WIDTH = 30;
	private static float LENGTH = 40;
	private static float IN_GOAL_LENGTH = 2;
	
	
	/**
	 * 
	 */
	public Pitch() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param g2d
	 */
	public void draw(Graphics2D g2d, float scale) {
		int width = (int)(scale * (LENGTH + 2 * IN_GOAL_LENGTH));
		int height = (int)(scale * WIDTH);

		g2d.setColor(Color.green);
		g2d.fillRect(0,0,width,height);
		
		g2d.setColor(Color.white);
		int x = (int)(scale * IN_GOAL_LENGTH);
		g2d.drawLine(x,0,x,height);
		x = (int)(scale * (LENGTH + IN_GOAL_LENGTH));
		g2d.drawLine(x,0,x,height);
		
	}

	public float getLength(){
		return LENGTH + 2 * IN_GOAL_LENGTH;
	}
	
	public float getWidth(){
		return WIDTH;
	}
	
	/**
	 * Determines whether the given position is within the
	 * goal areas at either end of the pitch.
	 * @param player
	 * @return true if within the goal area.
	 */
	public boolean isInGoal(Player player){
		float x = player.getXPosition();
		if(player.getTeam().getAttackingDirection() > 0){
			return (x > (LENGTH + IN_GOAL_LENGTH) && x < (LENGTH + 2 * IN_GOAL_LENGTH));
		} else {
			return (x >= 0.0f && x < IN_GOAL_LENGTH);
		}
	}

	/**
	 * Gets the position of one try line (lowest x coord).
	 * @return
	 */
	public float getLowTryLine() {
		return IN_GOAL_LENGTH;
	}

	/**
	 * Gets the position of the other try line (Highest x coord).
	 * @return
	 */
	public float getHighTryLine() {
		return LENGTH + IN_GOAL_LENGTH;
	}
}

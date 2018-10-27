/*
 * AvoidCollision.java
 * Created on 18-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis.strategy;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import uk.co.alvagem.minis.Game;
import uk.co.alvagem.minis.MathX;
import uk.co.alvagem.minis.Player;

/**
 * AvoidCollision
 * @author Bruce.Porteous
 *
 */
public class AvoidCollision implements PlayerStrategy {

	private float weight;
	private static final float CLOSE = 1.5f;

	/**
	 * 
	 */
	public AvoidCollision() {
		super();
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#assessWeight(uk.co.alvagem.minis.Player)
	 */
	public float assessWeight(Game game, Player player) {
		weight = 0.0f;
		
		// Get all the players on the pitch.
		Collection players = new LinkedList();
		players.addAll(game.getHome().getPlayers());
		players.addAll(game.getAway().getPlayers());
		
		float playerX = player.getXPosition();
		float playerY = player.getYPosition();

		for(Iterator iter = players.iterator(); iter.hasNext();){
			Player other = (Player)iter.next();
			
			if(other == player){
				continue;
			}
			// Ok to collide if trying to tackle!
			if(game.getBallCarrier() == other && other.getTeam() != player.getTeam()){
				continue;
			}
			
			float px = other.getXPosition();
			float py = other.getYPosition();
			
			float dx = px - playerX;
			float dy = py - playerY;
			
			// Invoke if within 0.5m
			float dist = (float)Math.sqrt(dx * dx + dy * dy);
			
			// Convert distance to function that goes to 1 at
			// zero seperation
			dist = (CLOSE - dist) / CLOSE;
			if(dist > weight){
				weight = dist;
			}
		}


		return weight;
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#apply(uk.co.alvagem.minis.Player)
	 */
	public void apply(Game game, Player player) {
		// Get all the players on the pitch.
		// Get all the players on the pitch.
		Collection players = new LinkedList();
		players.addAll(game.getHome().getPlayers());
		players.addAll(game.getAway().getPlayers());
		
		float playerX = player.getXPosition();
		float playerY = player.getYPosition();

		// Accumulate change in velocity
		float dvx = 0.0f;
		float dvy = 0.0f;
		float dvCount = 0;
		
		for(Iterator iter = players.iterator(); iter.hasNext();){
			Player other = (Player)iter.next();
			
			if(other == player){
				continue;
			}

			// Ok to collide if trying to tackle!
			if(game.getBallCarrier() == other && other.getTeam() != player.getTeam()){
				continue;
			}
			
			float px = other.getXPosition();
			float py = other.getYPosition();
			
			float dx = px - playerX;
			float dy = py - playerY;
			
			float dist = (float)Math.sqrt(dx * dx + dy * dy);
			
			if(dist < CLOSE){
				float w = (CLOSE - dist) / CLOSE;
				// Normalised vector from other player to this
				dx /= dist;
				dy /= dist;
				
				// Weight by closeness
				dx *= w;
				dy *= w; 
				
				// Accumulate from all close players
				// -ve sign to make players go away
				dvx -= dx;
				dvy -= dy;
				
				++dvCount;
			}
			
			if(dvCount > 0){
				// Reduce effect of multiple "forces"
				dvx /= dvCount;
				dvy /= dvCount;
			
				float vx = player.getXSpeed();
				float vy = player.getYSpeed();
				
				vx += dvx;
				vy += dvy;
				
				float dir = (float)Math.atan2(vy,vx);
				
				if(player == game.getBallCarrier()){
					float delta =MathX.signum(vy) * player.getTeam().getAttackingDirection();
					delta *= 0.1;
					dir -= delta;
				}
				player.setDirection(dir);
			}
		}

	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getName()
	 */
	public String getName() {
		return "Avoid collision";
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getWeight()
	 */
	public float getWeight() {
		return weight;
	}

}

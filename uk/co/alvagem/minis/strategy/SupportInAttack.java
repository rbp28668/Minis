/*
 * SupportInAttack.java
 * Created on 18-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis.strategy;

import uk.co.alvagem.minis.Game;
import uk.co.alvagem.minis.Player;

/**
 * SupportInAttack
 * @author Bruce.Porteous
 *
 */
public class SupportInAttack implements PlayerStrategy {

	private float weight;
	private static final float SIDE = 2.0f;
	private static final float BEHIND = 1.0f;
	
	/**
	 * 
	 */
	public SupportInAttack() {
		super();
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#assessWeight(uk.co.alvagem.minis.Player)
	 */
	public float assessWeight(Game game, Player player) {
		weight = 0.0f;
		if(game.getBallCarrier() != player && player.getTeam().hasPosession()){
			weight = 0.5f;
		}
		return weight;
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#apply(uk.co.alvagem.minis.Player)
	 */
	public void apply(Game game, Player player) {
		if(game.getBallCarrier() != null){
			
			float bcy = game.getBallCarrier().getYPosition();
			float px = player.getXPosition();
			float py = player.getYPosition();
			
			// Want to formate on a target player.  It's the
			// player between this player and the ball carrier
			// that's nearest to this player.  If there are no
			// players between this player and the ball carrier,
			// then it's this player.
			Player target = game.getBallCarrier();
			float minDist = Float.MAX_VALUE;
			
			for(Player teamMate : player.getTeam().getPlayers()) {
				
				// Ignore any players stopped by a tackle.
				if(teamMate.isStoppedByTackle(game)){
					continue;
				}
				
				float ty = teamMate.getYPosition();
				
				if(py > bcy){
					if(ty > bcy && ty < py){ // possible
						float dy = py - ty;	
						if(dy < minDist){
							minDist = dy;
							target = teamMate;
						}
					}
				} else { // py <= bcy
					if(ty > py && ty < bcy){ // possible
						float dy = ty - py;
						if(dy < minDist){
							minDist = dy;
							target = teamMate;
						}
					}
				}
			}
			
			// Ok so target should be the player to position
			// against.
			float tx = target.getXPosition();
			float ty = target.getYPosition();
			
			if(py > ty){
				ty += SIDE;
			} else {
				ty -= SIDE;
			}
			tx -= player.getTeam().getAttackingDirection() * BEHIND;
			float dx = tx - px;
			float dy = ty - py;
			
			float dir = (float)Math.atan2(dy,dx);
			player.setSpeed(Player.RUN);
			player.setDirection(dir);
			
		}

	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getName()
	 */
	public String getName() {
		return "Support in attack";
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getWeight()
	 */
	public float getWeight() {
		return weight;
	}

}

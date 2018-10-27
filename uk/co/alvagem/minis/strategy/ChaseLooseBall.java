/*
 * ChaseLooseBall.java
 * Created on 18-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis.strategy;

import uk.co.alvagem.minis.Game;
import uk.co.alvagem.minis.Player;

/**
 * ChaseLooseBall
 * @author Bruce.Porteous
 *
 */
public class ChaseLooseBall implements PlayerStrategy {

	private float weight;
	/**
	 * 
	 */
	public ChaseLooseBall() {
		super();
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#assessWeight(uk.co.alvagem.minis.Player)
	 */
	public float assessWeight(Game game, Player player) {
		weight = 0.0f;
		if(game.isInPlay()){
			if(game.getBallCarrier() == null){
				weight = 0.5f;
			}
		}
		return weight;
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#apply(uk.co.alvagem.minis.Player)
	 */
	public void apply(Game game, Player player) {
		// Set speed to run and work out direction of
		// the ball.
		
		float ballX = game.getBall().getXPosition();
		float ballY = game.getBall().getYPosition();
		
		float playerX = player.getXPosition();
		float playerY = player.getYPosition();
		
		float dx = ballX - playerX;
		float dy = ballY - playerY;
		
		float direction = (float)Math.atan2(dy,dx);
		player.setSpeed(Player.RUN);
		player.setDirection(direction);
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getName()
	 */
	public String getName() {
		return "Chase loose ball";
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getWeight()
	 */
	public float getWeight() {
		return weight;
	}

}

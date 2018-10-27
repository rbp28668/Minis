/*
 * PickupBall.java
 * Created on 19-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis.strategy;

import uk.co.alvagem.minis.Game;
import uk.co.alvagem.minis.Player;

/**
 * PickupBall
 * @author Bruce.Porteous
 *
 */
public class PickupBall implements PlayerStrategy {

	private float weight;
	/**
	 * 
	 */
	public PickupBall() {
		super();
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#assessWeight(uk.co.alvagem.minis.Game, uk.co.alvagem.minis.Player)
	 */
	public float assessWeight(Game game, Player player) {
		weight = 0.0f;
		
		if(game.getBallCarrier() == null && game.getPreviousCarrier() != player){
			float ballX = game.getBall().getXPosition();
			float ballY = game.getBall().getYPosition();
			
			float playerX = player.getXPosition();
			float playerY = player.getYPosition();
			
			float dx = ballX - playerX;
			float dy = ballY - playerY;
	
			// Invoke if within 0.5m
			if(dx * dx + dy * dy < 0.25f){
				weight = 0.8f;
			}
		}
		return weight;
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getWeight()
	 */
	public float getWeight() {
		return weight;
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#apply(uk.co.alvagem.minis.Game, uk.co.alvagem.minis.Player)
	 */
	public void apply(Game game, Player player) {
		if(game.getRandom().nextInt(1000) > 900){
			game.setBallCarrier(player);
		} // else fumbled!

	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getName()
	 */
	public String getName() {
		return "Pick up ball";
	}

}

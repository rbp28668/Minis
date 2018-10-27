/*
 * WanderAbout.java
 * Created on 19-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis.strategy;

import uk.co.alvagem.minis.Game;
import uk.co.alvagem.minis.Player;

/**
 * WanderAbout
 * @author Bruce.Porteous
 *
 */
public class WanderAbout implements PlayerStrategy {

	private static final float WEIGHT = 0.01f; 
	/**
	 * 
	 */
	public WanderAbout() {
		super();
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#assessWeight(uk.co.alvagem.minis.Player)
	 */
	public float assessWeight(Game game, Player player) {
		return WEIGHT;
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getWeight()
	 */
	public float getWeight() {
		return WEIGHT;
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#apply(uk.co.alvagem.minis.Player)
	 */
	public void apply(Game game, Player player) {
		if(game.getRandom().nextInt(1000) < 1){
			float dir = game.getRandom().nextFloat();
			dir *= (Math.PI * 2);
			player.setDirection(dir);
			player.setSpeed(Player.WALK);
			
		}

	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getName()
	 */
	public String getName() {
		return "Wander about";
	}

}

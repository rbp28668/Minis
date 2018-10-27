/*
 * StoppedByTackle.java
 * Created on 21-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis.strategy;

import uk.co.alvagem.minis.Game;
import uk.co.alvagem.minis.Player;

/**
 * StoppedByTackle
 * @author Bruce.Porteous
 *
 */
public class StoppedByTackle implements PlayerStrategy {

	private float weight;
	/**
	 * 
	 */
	public StoppedByTackle() {
		super();
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#assessWeight(uk.co.alvagem.minis.Game, uk.co.alvagem.minis.Player)
	 */
	public float assessWeight(Game game, Player player) {
		weight = 0.0f;
		if(player.isStoppedByTackle(game)){
			weight = 1.0f;
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
		player.setRawSpeed(0.0f,0.0f);

	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getName()
	 */
	public String getName() {
		return "Stopped by tackle";
	}

}

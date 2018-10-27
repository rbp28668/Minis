/*
 * AvoidTackle.java
 * Created on 18-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis.strategy;

import uk.co.alvagem.minis.Game;
import uk.co.alvagem.minis.Player;

/**
 * AvoidTackle
 * @author Bruce.Porteous
 *
 */
public class AvoidTackle implements PlayerStrategy {

	/**
	 * 
	 */
	public AvoidTackle() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#assessWeight(uk.co.alvagem.minis.Player)
	 */
	public float assessWeight(Game game, Player player) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#apply(uk.co.alvagem.minis.Player)
	 */
	public void apply(Game game, Player player) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getName()
	 */
	public String getName() {
		return "Avoid tackle";
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getWeight()
	 */
	public float getWeight() {
		// TODO Auto-generated method stub
		return 0;
	}

}

/*
 * ScoreTry.java
 * Created on 19-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis.strategy;

import uk.co.alvagem.minis.Game;
import uk.co.alvagem.minis.Player;

/**
 * ScoreTry
 * @author Bruce.Porteous
 *
 */
public class ScoreTry implements PlayerStrategy {

	private float weight;
	
	/**
	 * 
	 */
	public ScoreTry() {
		super();
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#assessWeight(uk.co.alvagem.minis.Game, uk.co.alvagem.minis.Player)
	 */
	public float assessWeight(Game game, Player player) {
		weight = 0.0f;
		if(game.isInPlay() && game.getBallCarrier() == player){
			if(game.getPitch().isInGoal(player)){
				weight = 1.0f;
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
		game.setBallCarrier(null);
		game.setInPlay(false);
		player.getTeam().tryScored(game);
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getName()
	 */
	public String getName() {
		return "Score try";
	}

}

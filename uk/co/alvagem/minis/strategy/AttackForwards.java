/*
 * AttackForwards.java
 * Created on 18-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis.strategy;

import uk.co.alvagem.minis.Game;
import uk.co.alvagem.minis.Player;

/**
 * AttackForwards
 * @author Bruce.Porteous
 *
 */
public class AttackForwards implements PlayerStrategy {

	private float weight;
	
	/**
	 * 
	 */
	public AttackForwards() {
		super();
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#assessWeight(uk.co.alvagem.minis.Player)
	 */
	public float assessWeight(Game game, Player player) {
		weight = 0.0f;
		if(game.isInPlay() && game.getBallCarrier() == player){
			weight = 0.5f;
		}
		return weight;
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#apply(uk.co.alvagem.minis.Player)
	 */
	public void apply(Game game, Player player) {
		float dirn = player.getTeam().getAttackingDirection();
		
		if(dirn > 0.0f){
			dirn = 0.0f;
		} else {
			dirn = (float)Math.PI;
		}
		player.setSpeed(Player.RUN);
		player.setDirection(dirn);
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getName()
	 */
	public String getName() {
		return "Attack forwards";
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getWeight()
	 */
	public float getWeight() {
		return weight;
	}

}

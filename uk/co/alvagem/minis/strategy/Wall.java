/*
 * Wall.java
 * Created on 19-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis.strategy;

import uk.co.alvagem.minis.Game;
import uk.co.alvagem.minis.Player;

/**
 * Wall - acts as a "brick wall" 2m around the pitch to reflect
 * wayward players back again.
 * @author Bruce.Porteous
 *
 */
public class Wall implements PlayerStrategy {

	private static final float SURROUND = 2.0f;
	private float weight = 0.0f;
	/**
	 * 
	 */
	public Wall() {
		super();
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#assessWeight(uk.co.alvagem.minis.Player)
	 */
	public float assessWeight(Game game, Player player) {
		float x = player.getXPosition();
		float y = player.getYPosition();
		
		if(x < (0.0f - SURROUND) || x > (game.getPitch().getLength() + SURROUND)
				|| y < (0.0f - SURROUND) || y > (game.getPitch().getWidth() + SURROUND)){
			weight =  1.0f;
		} else {
			weight = 0.0f;
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
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#apply(uk.co.alvagem.minis.Player)
	 */
	public void apply(Game game, Player player) {
		float x = player.getXPosition();
		float y = player.getYPosition();
		
		if(x < (0.0f - SURROUND)){
			player.setRawSpeed(Player.WALK,0.0f);
		} else if ( x > (game.getPitch().getLength() + SURROUND)){
			player.setRawSpeed(-Player.WALK,0.0f);
		}
		
		if(y < (0.0f - SURROUND)) {
			player.setRawSpeed(0.0f,Player.WALK);
		} else if (y > (game.getPitch().getWidth() + SURROUND)){
			player.setRawSpeed(0.0f,-Player.WALK);
		}
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getName()
	 */
	public String getName() {
		return "Wall";
	}

}

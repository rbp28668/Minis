/*
 * KeepDefensiveLine.java
 * Created on 18-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis.strategy;

import java.util.Iterator;

import uk.co.alvagem.minis.Game;
import uk.co.alvagem.minis.Player;

/**
 * KeepDefensiveLine
 * @author Bruce.Porteous
 *
 */
public class KeepDefensiveLine implements PlayerStrategy {

	private float weight;
	private static final float SEPERATION = 3.0f;
	/**
	 * 
	 */
	public KeepDefensiveLine() {
		super();
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#assessWeight(uk.co.alvagem.minis.Player)
	 */
	public float assessWeight(Game game, Player player) {
		weight = 0.0f;
		if(game.isInPlay() && !player.getTeam().hasPosession()){
			weight = 0.5f;
		}
		return weight;
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#apply(uk.co.alvagem.minis.Player)
	 */
	public void apply(Game game, Player player) {
		
		float px = player.getXPosition();
		
		int count = 0;
		float dx = 0;
		float vxSum = 0;
		for(Player teamMember : player.getTeam().getPlayers()) {
			float tx = teamMember.getXPosition();
			dx += (tx - px);
			
			vxSum += teamMember.getXSpeed();
			
			++count;
		}
		dx /= count;
		
		float speed = Player.JOG;
		if(vxSum > 0.0f){ // generally going left to right...
			if(dx > 0){
				speed = Player.RUN;
			}
		} else {
			if(dx < 0) {
				speed = Player.RUN;
			}
		}

		
		// Direction.  generally, if you're in defence, you're
		// trying to close down the ball carrier.
		Player ballCarrier = game.getBallCarrier();
		if(ballCarrier != null) {
			float ballX = ballCarrier.getXPosition();
			dx = ballX - px;
			float dir = (float)((dx > 0) ? 0.0 : Math.PI);

			// Update dir slightly to get spacing.
			// TODO
			player.setDirection(dir);
			player.setSpeed(speed);
		}
		
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getName()
	 */
	public String getName() {
		return "Keep defensive line";
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getWeight()
	 */
	public float getWeight() {
		return weight;
	}

}

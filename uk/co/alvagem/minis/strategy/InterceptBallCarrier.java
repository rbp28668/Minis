/*
 * InterceptBallCarrier.java
 * Created on 18-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis.strategy;

import uk.co.alvagem.minis.Game;
import uk.co.alvagem.minis.Player;

/**
 * InterceptBallCarrier
 * @author Bruce.Porteous
 *
 */
public class InterceptBallCarrier implements PlayerStrategy {

	private float weight;
	/**
	 * 
	 */
	public InterceptBallCarrier() {
		super();
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#assessWeight(uk.co.alvagem.minis.Player)
	 */
	public float assessWeight(Game game, Player player) {
		weight = 0.0f;
		if(game.isInPlay() && game.getBallCarrier() != null){
			if(!player.getTeam().hasPosession()){
				// Set speed to run and work out direction of
				// the ball.
				
				Player carrier = game.getBallCarrier();
				float cx = carrier.getXPosition();
				float cy = carrier.getYPosition();
				
				float playerX = player.getXPosition();
				float playerY = player.getYPosition();
				
				float dx = cx - playerX;
				float dy = cy - playerY;
	
				// Distance from this player to ball carrier
				float toPlayer = (float)Math.sqrt(dx * dx + dy * dy);
				
				// Distance from ball carrier to try line
				float toTry;
				if(carrier.getTeam().getAttackingDirection() > 0.0f){
					toTry = game.getPitch().getHighTryLine() - cx;
				} else {
					toTry = cx - game.getPitch().getLowTryLine();
				}
				
				// Only bother if we can intercept.
				if(toTry > toPlayer){
					weight = 0.6f;
				}

			}
		}
		return weight;
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#apply(uk.co.alvagem.minis.Player)
	 */
	public void apply(Game game, Player player) {
		Player carrier = game.getBallCarrier();
		if(carrier != null) {
			float cx = carrier.getXPosition();
			float cy = carrier.getYPosition();
			
			float playerX = player.getXPosition();
			float playerY = player.getYPosition();
			
			float dx = cx - playerX;
			float dy = cy - playerY;
			float direction = (float)Math.atan2(dy,dx);
			player.setSpeed(Player.RUN);
			player.setDirection(direction);
		}
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getName()
	 */
	public String getName() {
		return "Intercept ball carrier";
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getWeight()
	 */
	public float getWeight() {
		return weight;
	}

}

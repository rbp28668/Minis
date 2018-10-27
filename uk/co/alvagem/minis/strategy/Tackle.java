/*
 * Tackle.java
 * Created on 18-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis.strategy;

import uk.co.alvagem.minis.Game;
import uk.co.alvagem.minis.Player;

/**
 * Tackle
 * @author Bruce.Porteous
 *
 */
public class Tackle implements PlayerStrategy {

	private float weight;
	private static final float CLOSE = 0.4f;
	private static final float PASS_SPEED = 6.0f;
	
	/**
	 * Tackle
	 */
	public Tackle() {
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
				
				// Close enough - go for it!
				if(toPlayer < CLOSE){
					weight = 0.8f;
				}
			}
		}
		return weight;
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#apply(uk.co.alvagem.minis.Player)
	 */
	public void apply(Game game, Player player) {
		if(game.getRandom().nextInt(1000) > 800){
			game.getBallCarrier().tackled(game);
			player.makeTackle(game);
			passBall(game);
		}
	}

	/**
	 * @param game
	 */
	private void passBall(Game game) {
		game.getBallCarrier().getTeam().passToNearest(game,PASS_SPEED);
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getName()
	 */
	public String getName() {
		return "Tackle";
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getWeight()
	 */
	public float getWeight() {
		return weight;
	}

}

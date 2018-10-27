/*
 * StayOutOfTouch.java
 * Created on 18-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis.strategy;

import uk.co.alvagem.minis.Game;
import uk.co.alvagem.minis.Player;


/**
 * StayOutOfTouch
 * @author Bruce.Porteous
 *
 */
public class StayOutOfTouch implements PlayerStrategy {

	private float weight;
	private static final float CLOSE = 1.0f;
	private static final float TURN = 0.1f;
	
	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#apply(uk.co.alvagem.minis.Player)
	 */
	public void apply(Game game, Player player) {
		float y = player.getYPosition();
		float width = game.getPitch().getWidth();
		
		float vx = player.getXSpeed();
		float vy = player.getYSpeed();
		
		float turn  = 0.0f;
		if(y < width / 2) { // top touchline
			if(vx > 0) {
				turn = TURN;
			} else {
				turn = -TURN;
			}
		} else { // bottom touchline
			if(vx > 0) {
				turn = -TURN;
			} else {
				turn = TURN;
			}
		}
		float w = 0.0f;
		if(y < CLOSE) {
			w = (CLOSE - y)/CLOSE;
		} else if( y > (width - CLOSE)){
			w = (CLOSE - (width - y) )/CLOSE;
		}
		if(w > 1.0f) {
			w = 1.0f;
		}
		
		turn *= w;
		
		float dir = (float)Math.atan2(vy,vx);
		dir += turn;
		player.setDirection(dir);
	}
	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#assessWeight(uk.co.alvagem.minis.Player)
	 */
	public float assessWeight(Game game, Player player) {
		weight = 0.0f;
		float y = player.getYPosition();
		float width = game.getPitch().getWidth();
		
		if(y < CLOSE) {
			weight = (CLOSE - y)/CLOSE;
		} else if( y > (width - CLOSE)){
			weight = (CLOSE - (width - y) )/CLOSE;
		}
		
		if(weight > 1.0f) {
			weight = 1.0f;
		}
		return weight;
	}
	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getName()
	 */
	public String getName() {
		return "Stay out of touch";
	}
	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getWeight()
	 */
	public float getWeight() {
		return weight;
	}
}

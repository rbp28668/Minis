/*
 * Restart.java
 * Created on 18-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis.strategy;

import uk.co.alvagem.minis.Game;
import uk.co.alvagem.minis.Player;
import uk.co.alvagem.minis.Team;

/**
 * Restart
 * @author Bruce.Porteous
 *
 */
public class Restart implements PlayerStrategy {

	private float weight;
	private static int inPlace = 0;
	
	/**
	 * 
	 */
	public Restart() {
		super();
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#assessWeight(uk.co.alvagem.minis.Player)
	 */
	public float assessWeight(Game game, Player player) {
		weight = 0;
		if(!game.isInPlay()) {
			weight = 0.9f;
		}
		return weight;
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#apply(uk.co.alvagem.minis.Player)
	 */
	public void apply(Game game, Player player) {
		if(game.isInPlay()){
			return; // already restarted.
		}
		
		// Set speed to run and work out direction of
		// the ball.
		
		float length = game.getPitch().getLength();
		float width = game.getPitch().getWidth();
		
		float halfWay = length/2;
		
		boolean teamHasFreePass = player.getTeam().hasFreePass();
		
		float offset = (teamHasFreePass) ? 2.0f : 7.0f;
		
		// If this player's team has a free pass and this player
		// in number 4 then this player should make the initial pass
		boolean hasPass = false;
		if(player.getNumber() == 4 && teamHasFreePass){ // middle player starts
			 offset = 0;
			 hasPass = true;
		}
		
		float x = halfWay - player.getTeam().getAttackingDirection() * offset; 
		float y = player.getNumber() * width / ( 1 + player.getTeam().getPlayerCount());
		
		// If this player should make the initial pass and doesn't
		// have the ball then they need to go and get it!
		if(hasPass && game.getBallCarrier() == null) {
			x = game.getBall().getXPosition();
			y = game.getBall().getYPosition();
		}
		
		float playerX = player.getXPosition();
		float playerY = player.getYPosition();
		
		float dx = x - playerX;
		float dy = y - playerY;
		
		float direction = (float)Math.atan2(dy,dx);
		player.setSpeed(Player.JOG);
		player.setDirection(direction);

		// If close, then decrement the in place count.  Otherwise set it back to the number of
		// players on the field.  Then all players have to be in place before play restarts.
		if(dx * dx + dy * dy < 0.25f){
			if(hasPass){
				// Pick up the ball?
				if(game.getBallCarrier() == null) {
					game.setBallCarrier(player);
				} else {
					--inPlace;
				}
			} else {
				--inPlace;
			}
		} else {
			inPlace = game.getHome().getPlayerCount() + game.getAway().getPlayerCount();
		}
		
		if(inPlace <= 0 && hasPass) {
			Team startTeam = player.getTeam();
			startTeam.passToNearest(game,6.0f);
			startTeam.setHasFreePass(false);
			//game.getBall().setPosition(length/2, width/2);
			game.setInPlay(true);
		}
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getName()
	 */
	public String getName() {
		return "Restart";
	}

	/* (non-Javadoc)
	 * @see uk.co.alvagem.minis.strategy.PlayerStrategy#getWeight()
	 */
	public float getWeight() {
		return weight;
	}

}

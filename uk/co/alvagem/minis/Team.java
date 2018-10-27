/*
 * Team.java
 * Created on 17-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Team
 * @author Bruce.Porteous
 *
 */
public class Team {

	private final static int MAX_PLAYERS = 7;
	private Vector<Player> players = new Vector<>();
	private Color colour = Color.blue;
	private float direction = 1.0f; // direction of play for this team - 1.0 or -1.0.
	private int score = 0;
	private boolean posession = false;
	private boolean hasFreePass = false;
	private List<TeamListener> listeners = new LinkedList<>();
	
	/**
	 * 
	 */
	public Team() {
		super();
	}
	
	/**
	 * Adds a player to the team.  No more than MAX_PLAYERS
	 * can be in the team at any one time.
	 * @param player is the Player to add.
	 */
	public void addPlayer(Player player) {
		
		if(player == null){
			throw new NullPointerException("Can't add null Player to team");
		}
		
		if(players.size() >= MAX_PLAYERS){
			throw new IllegalStateException("Team is full");
		}
		
		players.add(player);
		
	}
	
	void playersThink(Game game){
		for(Player player : players) {
			player.think(game);
		}
	}

	void playersMove(Game game, float interval){
		for(Player player : players) { 
			player.move(game,interval);
		}
	}

	/**
	 * @param g2d
	 * @param scale
	 */
	public void draw(Graphics2D g2d, float scale) {
		for(Player player : players) {
			player.draw(g2d,colour, scale);
		}
		
	}


	/**
	 * @return Returns the colour.
	 */
	public Color getColour() {
		return colour;
	}
	/**
	 * @param colour The colour to set.
	 */
	public void setColour(Color colour) {
		this.colour = colour;
	}

	/**
	 * Sets the direction of play for this team. 
	 * @param positive is a flag to distinguish between the 2 directions of play.
	 */
	public void setDirection(boolean positive){
		direction = positive ? 1.0f : -1.0f;
	}
	
	/**
	 * Returns the direction 1 or -1 that a player in this team should be going in
	 * if they're attacking.
	 * @return direction value.
	 */
	public float getAttackingDirection(){
		return direction;
	}
	/**
	 * Arbitrarily lays out the team on the pitch before a match.
	 * @param pitch
	 * 
	 */
	public void setInitialPosition(Pitch pitch) {
		float length = pitch.getLength();
		float width = pitch.getWidth();
		
		float halfWay = length/2;
		
		float x = halfWay - direction * length / 4.0f; 
		
		float separation = width / (players.size() + 1);
		
		float y = 0.0f;
		for(Player player : players) {
			y += separation;
			player.teleportTo(x,y);
		}
	}

	/**
	 * Sets whether this team has posession.
	 * @param b
	 */
	public void setPosession(boolean b) {
		posession = b;
		
	}

	/**
	 * @return
	 */
	public boolean hasPosession() {
		return posession;
	}

	/**
	 * Notifies the team that they've scored a try.
	 * @param game
	 */
	public void tryScored(Game game) {
		score += 5;
		if(this == game.getHome()){
			game.getAway().setHasFreePass(true);
		} else {
			game.getHome().setHasFreePass(true);
		}
		signalTryScored();
	}
	
	/**
	 * Signals the score of a try to any listeners.
	 */
	private void signalTryScored() {
		for(TeamListener listener : listeners) {
			listener.tryScored();
		}
	}

	/**
	 * Gets the current score for this team.
	 * @return the current score.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Get the number of players in the team.
	 * @return number of players in the team.
	 */
	public int getPlayerCount() {
		return players.size();
	}

	/**
	 * Gets the players for this team.
	 * @return
	 */
	public Collection<Player> getPlayers() {
		return Collections.unmodifiableCollection(players);
	}

	/**
	 * @return Returns the hasFreePass.
	 */
	public boolean hasFreePass() {
		return hasFreePass;
	}
	/**
	 * @param hasFreePass The hasFreePass to set.
	 */
	public void setHasFreePass(boolean hasFreePass) {
		this.hasFreePass = hasFreePass;
	}
	
	/**
	 * @param game
	 */
	public void passToNearest(Game game, float speed) {
		
		Player player = game.getBallCarrier();
		
		float px = player.getXPosition();
		float py = player.getYPosition();
		
		// Which is the player's nearest team mate?
		// ... behind the player.
		float attack = getAttackingDirection();
		float minDist2 = Float.MAX_VALUE;
		Player nearest = null;
		for(Player teamMate : players) {
			if(teamMate != player){
				float tx = teamMate.getXPosition();
				float ty = teamMate.getYPosition();
				
				float dx = (px - tx);
				float dy = (py - ty);
				
				float dist2 = dx * dx + dy * dy;
				if(dist2 < minDist2 
						&& attack == MathX.signum(dx)){
					nearest = teamMate;
					minDist2 = dist2;
				}
			}
		}
		
		float dir;
		if(nearest != null) {
			// pass to nearest player
			float tx = nearest.getXPosition();
			float ty = nearest.getYPosition();
			
			float dx = tx - px;
			float dy = ty - py;
			
			dir = (float)Math.atan2(dy,dx);
		} else { // panic, no players on-side, lob it backwards
			dir = player.getTeam().getAttackingDirection();
			dir = (dir > 0) ? 0.0f : (float)Math.PI;
		}
		game.getBall().pass(speed,dir);
		game.setBallCarrier(null);
	}

	/**
	 * Adds a listener that should receive updates from this team.
	 * @param listener is the listener to add.
	 */
	public void addListener(TeamListener listener){
		listeners.add(listener);
	}
	
	/**
	 * Removes a given listener from this team.
	 * @param listener is the listener to remove.
	 */
	public void removeListener(TeamListener listener){
		listeners.remove(listener);
	}
}

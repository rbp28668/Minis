/*
 * Player.java
 * Created on 17-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import uk.co.alvagem.minis.strategy.AttackForwards;
import uk.co.alvagem.minis.strategy.AvoidCollision;
import uk.co.alvagem.minis.strategy.AvoidTackle;
import uk.co.alvagem.minis.strategy.ChaseLooseBall;
import uk.co.alvagem.minis.strategy.InterceptBallCarrier;
import uk.co.alvagem.minis.strategy.KeepDefensiveLine;
import uk.co.alvagem.minis.strategy.PickupBall;
import uk.co.alvagem.minis.strategy.PlayerStrategy;
import uk.co.alvagem.minis.strategy.Restart;
import uk.co.alvagem.minis.strategy.ScoreTry;
import uk.co.alvagem.minis.strategy.StayOutOfTouch;
import uk.co.alvagem.minis.strategy.StoppedByTackle;
import uk.co.alvagem.minis.strategy.SupportInAttack;
import uk.co.alvagem.minis.strategy.Tackle;
import uk.co.alvagem.minis.strategy.Wall;
import uk.co.alvagem.minis.strategy.WanderAbout;

/**
 * Player
 * @author Bruce.Porteous
 *
 */
public class Player {

	// Convenient speed constants.
	public static final float STOP = 0.0f;
	public static final float WALK = 1.0f;
	public static final float JOG = 2.0f;
	public static final float RUN = 3.0f;
	
	private static final long TACKLE_DURATION = 3000;
	
	private String name;
	private int number;
	private Team team;
	
	private float speed; // in metres per sec
	private float direction;	 // radians
	
	private float x,y; // where I am
	private float vx,vy; // derived velocity
	
	private boolean tackled;
	private long tackleTime;
	
	private List<PlayerStrategy> strategies = new LinkedList<>();
	private List<PlayerListener> listeners = new LinkedList<>();
	
	/**
	 * @param name
	 * @param number
	 * @param team
	 * 
	 */
	public Player(int number, String name, Team team) {
		super();
		this.name = name;
		this.number = number;
		this.team = team;
		x = 0;
		y = 0;
		vx = 1f;
		vy = 0;
		
		strategies.add(new AttackForwards());
		strategies.add(new AvoidCollision());
		strategies.add(new AvoidTackle());
		strategies.add(new ChaseLooseBall());
		strategies.add(new InterceptBallCarrier());
		strategies.add(new KeepDefensiveLine());
		strategies.add(new PickupBall());
		strategies.add(new Restart());
		strategies.add(new ScoreTry());
		strategies.add(new StayOutOfTouch());
		strategies.add(new StoppedByTackle());
		strategies.add(new SupportInAttack());
		strategies.add(new Tackle());
		strategies.add(new Wall());
		strategies.add(new WanderAbout());
	}
	
	/**
	 * Tries the diffeerent strategies and applies the one with the
	 * highest score.
	 * @param game
	 */
	public void think(Game game) {
		for(PlayerStrategy strategy : strategies) {
			strategy.assessWeight(game,this);
		}
		PlayerStrategy strategyToUse = getBestStrategy();
		strategyToUse.apply(game,this);
		//System.out.println(strategyToUse.getName());
		
		showStrategy(strategyToUse);
		
	}

	/**
	 * @return
	 */
	private PlayerStrategy getBestStrategy() {
		PlayerStrategy strategyToUse = null;
		float weight = -1.0f; // all real weights are > than this.
		for(PlayerStrategy ps: strategies) {
			if(ps.getWeight() > weight) {
				strategyToUse = ps;
				weight = ps.getWeight();
			}
		}
		return strategyToUse;
	}

	/**
	 * Shows the strategy being used by this player to any listeners.
	 * @param strategyToUse
	 */
	private void showStrategy(PlayerStrategy strategyToUse) {
		for(PlayerListener listener : listeners) {
			listener.usingStrategy(strategyToUse);
		}
	}

	/**
	 * Moves the player in the current direction.  
	 * @param game is the current game.
	 * @param interval is the time in seconds since the last move (to 
	 * calculate distance).
	 */
	public void move(Game game, float interval) {
		x += vx * interval;
		y += vy * interval;
		
		if(game.getBallCarrier() == this){
			game.getBall().setPosition(x,y);
		}
	}

	/**
	 * @param g2d
	 * @param colour
	 * @param scale
	 */
	public void draw(Graphics2D g2d, Color colour, float scale) {
		int ix = (int)(x * scale + 0.5);
		int iy = (int)(y * scale + 0.5);
		
		int size = (int)(0.5 * scale);
		
		g2d.setColor(colour);
		g2d.fillOval(ix,iy,size,size);
		
	}

	/**
	 * Places a player on a given position in the field.  
	 * @param x
	 * @param y
	 */
	public void teleportTo(float x, float y) {
		this.x = x;
		this.y = y;
		this.vx = 0;
		this.vy = 0;
	}

	/**
	 * @param dir
	 */
	public void setDirection(float dir) {
		this.direction = dir;
		updateRawVelocity();
		
	}

	/**
	 * @param speed
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
		updateRawVelocity();
	}

	/**
	 * Calculate the cartesian velocity components vx and vy from
	 * the polar velocity: speed and direction.
	 */
	private void updateRawVelocity(){
		vx = speed * (float) Math.cos(direction);
		vy = speed * (float) Math.sin(direction);
	}
	
	/**
	 * Gets the current x position of the player.
	 * @return
	 */
	public float getXPosition() {
		return x;
	}

	/**
	 * Gets the current y position of the player.
	 * @return
	 */
	public float getYPosition() {
		return y;
	}

	/**
	 * This sets the raw speed in the player.  Used for "odd" changes of
	 * velocity like when bouncing off walls or other players.
	 * @param vx
	 * @param vy
	 */
	public void setRawSpeed(float vx, float vy) {
		this.vx = vx;
		this.vy = vy;
	}


	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return Returns the number.
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * @return Returns the team.
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * @return
	 */
	public float getXSpeed() {
		return vx;
	}

	/**
	 * @return
	 */
	public float getYSpeed() {
		return vy;
	}
	
	/**
	 * Marks the player as having been tackled.
	 * @param game is the current game.
	 */
	public void tackled(Game game){
		tackled = true;
		tackleTime = game.getClock().getTime();
		vx = 0;
		vy = 0;
	}
	
	/**
	 * Determines if a player has just been tackled.
	 * @return true if the player has just been tackled.
	 */
	public boolean isTackled(){
		return tackled;
	}
	
	/**
	 * If a player tackles or is tackled they are out of the game while tags
	 * are sorted out.  This determines whether the player is stopped due
	 * to a tackle (whether given or received).
	 * @param game is the current game.
	 * @return true if the player is stopped.
	 */
	public boolean isStoppedByTackle(Game game){
		if(tackleTime > 0){
			long now = game.getClock().getTime();
			if( (now - tackleTime) > TACKLE_DURATION) {
				tackled = false;
				tackleTime = 0;
			}
		}
		return tackleTime > 0;
	}
	
	/**
	 * If the player makes a successful tackle then this should be
	 * called to mark the player out of the game while tags are
	 * sorted out.
	 * @param game is the current game.
	 */
	public void makeTackle(Game game){
		tackleTime = game.getClock().getTime();
		vx = 0;
		vy = 0;
	}
	
	/**
	 * Adds a listener that will monitor this player.
	 * @param listener is the listener to add.
	 */
	public void addListener(PlayerListener listener){
		listeners.add(listener);
	}
	
	/**
	 * Removes a listener from the list of listeners that is 
	 * monitoring this player.
	 * @param listener is the listener to remove.
	 */
	public void removeListener(PlayerListener listener){
		listeners.remove(listener);
	}
	
}

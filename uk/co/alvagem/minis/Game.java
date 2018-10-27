/*
 * Game.java
 * Created on 17-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

/**
 * Game
 * @author Bruce.Porteous
 *
 */
public class Game {

	private Pitch pitch = new Pitch();
	private Ball ball = new Ball();
	private Team home = new Team();
	private Team away = new Team();
	private Referree ref = new Referree();
	private Clock clock = new Clock();
	
	private GameDisplay display;
	private boolean inProgress;
	private Timer timer;
	private long moveTime;
	
	private Player ballCarrier = null;
	private Player previousCarrier = null;
	
	private Random random = new Random(42);
	
	private boolean inPlay; // game in play (c.f. stopped for restart).
	
	/**
	 * Creates a new game.
	 */
	public Game() {
		super();
		
		// Introduce the players to the teams
		home.addPlayer(new Player(1,"Fred",home));
		home.addPlayer(new Player(2,"Pete",home));
		home.addPlayer(new Player(3,"Jim",home));
		home.addPlayer(new Player(4,"Mark",home));
		home.addPlayer(new Player(5,"Alan",home));
		home.addPlayer(new Player(6,"Tom",home));
		home.addPlayer(new Player(7,"Ian",home));

		away.addPlayer(new Player(1,"Andy",away));
		away.addPlayer(new Player(2,"Bert",away));
		away.addPlayer(new Player(3,"Justin",away));
		away.addPlayer(new Player(4,"Joe",away));
		away.addPlayer(new Player(5,"Mike",away));
		away.addPlayer(new Player(6,"David",away));
		away.addPlayer(new Player(7,"Steve",away));
		
		// Set the team colours
		home.setColour(Color.blue);
		away.setColour(Color.red);
		
		// Make sure the teams are playing in opposite directions
		home.setDirection(true);
		away.setDirection(false);
		
		// Layout the players on their own sides.
		home.setInitialPosition(pitch);
		away.setInitialPosition(pitch);
		
		// put the ball in the centre of the pitch
		ball.setPosition(pitch.getLength()/2, pitch.getWidth()/2);
		
		display = new GameDisplay(this);
	}

	/**
	 * Main command line entry point.
	 * @param args are the command line arguments (not used).
	 */
	public static void main(String[] args){
		Game game = new Game();
		game.run();
	}

	/**
	 * Runs the game.
	 */
	public void run() {
		inProgress = true;
		inPlay = false;
		int delay = 10; //milliseconds
		moveTime = clock.getTime();

		if(random.nextBoolean()){
			home.setHasFreePass(true);
		}else {
			away.setHasFreePass(true);
		}

		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				step();
			}
		};
		timer = new Timer(delay, taskPerformer);
		timer.start();
	}

	/**
	 * Performs a single step of the game.  Does nothing if 
	 * the clock is paused.
	 */
	private void step(){
		if(!clock.isPaused()){
			long now = clock.getTime();
			float interval = (now - moveTime)/1000.0f;
			moveTime = now;
			
			home.playersThink(this);
			away.playersThink(this);
			home.playersMove(this,interval);
			away.playersMove(this,interval);
			ball.move(this,interval);
			ref.supervise(this);
			display.repaint();
		}
	}
	
	/**
	 * Terminates the game.
	 */
	public void terminate() {
		if(timer != null){
			inProgress = false;
			timer.stop();
			timer = null;
		}
	}

	/**
	 * Draws the current state of the game.
	 * @param g2d
	 * @param scale
	 */
	public void draw(Graphics2D g2d, float scale) {
		pitch.draw(g2d,scale);
		home.draw(g2d,scale);
		away.draw(g2d,scale);
		ball.draw(g2d,scale);
	}
	
	
	/**
	 * @return Returns the away.
	 */
	public Team getAway() {
		return away;
	}
	/**
	 * @return Returns the ball.
	 */
	public Ball getBall() {
		return ball;
	}
	/**
	 * @return Returns the home.
	 */
	public Team getHome() {
		return home;
	}
	/**
	 * @return Returns the pitch.
	 */
	public Pitch getPitch() {
		return pitch;
	}
	/**
	 * Gets whether a game is in play at the current time. A game
	 * is not in play before the first whistle, at half time, when
	 * the players are getting ready for a restart etc.
	 * @return Returns the inPlay.
	 */
	public boolean isInPlay() {
		return inPlay;
	}
	
	/**
	 * Sets whether the game is in play or not.
	 * @param inPlay is true if the game is in play.
	 */
	public void setInPlay(boolean inPlay) {
		this.inPlay = inPlay;
	}
	
	/**
	 * Determines whether the game is in progress or not.
	 * @return the inProgress
	 */
	public boolean isInProgress() {
		return inProgress;
	}

	/**
	 * Gets the random number generator for the game.
	 * @return Returns the random.
	 */
	public Random getRandom() {
		return random;
	}

	/**
	 * Gets the current ball carrier.
	 * @return the Player who's the current ball carrier
	 * or null if the ball is free.
	 */
	public Player getBallCarrier(){
		return ballCarrier;
	}
	
	/**
	 * Sets the curreent ball carrier. Set to null
	 * if no player is carrying the ball.
	 * @param player
	 */
	public void setBallCarrier(Player player) {
		if(ballCarrier != null){
			ballCarrier.getTeam().setPosession(false);
			previousCarrier = ballCarrier;
		}
		ballCarrier = player;
		if(ballCarrier != null){
			ballCarrier.getTeam().setPosession(true);
			ball.pickUp();
		}
		
	}
	
	/**
	 * @return
	 */
	public Player getPreviousCarrier() {
		return previousCarrier;
	}
	
	/**
	 * Gets the clock used for timing in the game.  Note that
	 * this should be used instead of System.currentTimeMillis()
	 * as this allows for pausing and can be reset to zero.
	 * @return Returns the clock.
	 */
	public Clock getClock() {
		return clock;
	}

	/**
	 * Resets the game to the start positions, 0 score, 0 time etc.
	 */
	public void reset() {
		timer.stop();
		inProgress = true;
		inPlay = false;
		clock.reset();
		moveTime = clock.getTime();

		ballCarrier = null;
		previousCarrier = null;

		home.setHasFreePass(false);
		home.setPosession(false);
		away.setHasFreePass(false);
		away.setPosession(false);

		// See who starts.
		if(random.nextBoolean()){
			home.setHasFreePass(true);
		}else {
			away.setHasFreePass(true);
		}
		
		// Layout the players on their own sides.
		home.setInitialPosition(pitch);
		away.setInitialPosition(pitch);
		
		// put the ball in the centre of the pitch
		ball.setPosition(pitch.getLength()/2, pitch.getWidth()/2);
		
		timer.start();
		
	}

}

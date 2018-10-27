/*
 * GameDisplay.java
 * Created on 17-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import uk.co.alvagem.minis.strategy.PlayerStrategy;

/**
 * GameDisplay
 * @author Bruce.Porteous
 *
 */
public class GameDisplay extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Game game;
	private PitchPanel p;
	private ControlPanel c;
	
	/**
	 * @throws java.awt.HeadlessException
	 */
	public GameDisplay(Game game) throws HeadlessException {
		super("Minis");
		this.game = game;
		
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				stopGame();
			}
		});

		p = new PitchPanel();
		getContentPane().add(p, BorderLayout.CENTER);
		
		c = new ControlPanel(game);
		getContentPane().add(c, BorderLayout.NORTH);

//		Dimension d = p.getPreferredSize();
//		setBounds(100,100,d.width,d.height);

		setBounds(100,100,300,300);
		pack();
		
		setVisible(true);
	}

	void stopGame(){
		game.terminate();
		game = null;
		dispose();
	}

    
    /**
	 * releases any references to external objects.
	 */
	public void dispose() {
		game = null;
		super.dispose();
    }

	/**
	 * @param nearest
	 */
	public void monitorPlayer(Player nearest) {
		c.monitorPlayer(nearest);
	}



	private class PitchPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private float scale = 20;
		
		PitchPanel(){
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					Player nearest = getPlayerAt(e);
					if(nearest != null){
						monitorPlayer(nearest);
					}
				}

			});
			
			setToolTipText("*");
		}
	    /**
		 * @param nearest
		 */
		protected void monitorPlayer(Player nearest) {
			GameDisplay.this.monitorPlayer(nearest);
			
		}
		/* (non-Javadoc)
		 * @see java.awt.Component#getPreferredSize()
		 */
		public Dimension getPreferredSize() {
	       	
			Pitch pitch = game.getPitch();
			
	    	float width = pitch.getLength() * scale;
	    	float height = pitch.getWidth() * scale;
			return new Dimension((int)width + 1, (int)height + 1); 
	    }
	    
	    /* (non-Javadoc)
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		protected void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D)g;
			game.draw(g2d,scale);
			
	    }

	    /* (non-Javadoc)
		 * @see javax.swing.JComponent#getToolTipText(java.awt.event.MouseEvent)
		 */
		public String getToolTipText(MouseEvent event) {
	    	String text = null;
	    	Player nearest = getPlayerAt(event);
	    	if(nearest != null){
	    		text = "Player " + nearest.getNumber() + " : " + nearest.getName();
	    	}
			return text;
	    }
	
		private Player getPlayerAt(MouseEvent event){
	    	int mx = event.getX();
	    	int my = event.getY();
	    	
	    	float x = mx / scale;
	    	float y = my / scale;
	    	
	    	List<Player> players = new LinkedList<>();
	    	players.addAll(game.getHome().getPlayers());
	    	players.addAll(game.getAway().getPlayers());
	    	
	    	float minDist = Float.MAX_VALUE;
	    	Player nearest = null;
	    	for(Player p : players) {
	    		float dx = x - p.getXPosition();
	    		float dy = y - p.getYPosition();
	    		float dist = dx * dx + dy * dy;
	    		if(dist < minDist){
	    			minDist = dist;
	    			nearest = p;
	    		}
	    	}
	    	
	    	minDist = (float)Math.sqrt(minDist);
	    	minDist *= scale;
	    	
	    	if(minDist > 10){ // within 10 pixels?
	    		nearest = null;
	    	}
	    	return nearest;
		}
	}
	
	private class ControlPanel extends JPanel implements PlayerListener, TeamListener {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JButton newGame; 
		JButton pause;
		JTextField score;
		JTextField playerInfo;
		Player monitor = null;
		Game game;
		
		ControlPanel(Game game){
			this.game = game;
			
			Box box = Box.createHorizontalBox();
			add(box);
			
			newGame = new JButton("New Game");
			newGame.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					newGame();
				}
			});
			pause = new JButton("Pause");
			pause.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					doPause();
				}
			});

			box.add(newGame);
			box.add(pause);
			box.add(new JLabel("Score"));
			score = new JTextField(10);
			box.add(score);
			box.add(new JLabel("Player: "));
			playerInfo = new JTextField(40);
			box.add(playerInfo);
			
			game.getHome().addListener(this);
			game.getAway().addListener(this);
		}
		
		private void newGame(){
			game.reset();
		}
		
		private void doPause(){
			if(game.getClock().isPaused()){
				game.getClock().setPaused(false);
				pause.setText("Pause");
			} else {
				
				game.getClock().setPaused(true);
				pause.setText("Resume");
			}
		}

		/**
		 * Monitors a given player.
		 * @param player is the player to monitor.
		 */
		public void monitorPlayer(Player player){
			if(monitor != null) {
				monitor.removeListener(this);
			}
			monitor = player;
			monitor.addListener(this);
		}
		
		/* (non-Javadoc)
		 * @see uk.co.alvagem.minis.PlayerListener#usingStrategy(uk.co.alvagem.minis.strategy.PlayerStrategy)
		 */
		public void usingStrategy(PlayerStrategy strat) {
			playerInfo.setText(monitor.getName() + ": " + strat.getName() + " (" + strat.getWeight() + ")");
			
		}
		
		/* (non-Javadoc)
		 * @see uk.co.alvagem.minis.TeamListener#tryScored()
		 */
		public void tryScored() {
			String text = game.getHome().getScore() 
				+ " : "
				+ game.getAway().getScore();
			score.setText(text);
			
		}
		
	}

	
}

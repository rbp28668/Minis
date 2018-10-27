/*
 * PlayerListener.java
 * Created on 25-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis;

import uk.co.alvagem.minis.strategy.PlayerStrategy;

/**
 * PlayerListener
 * @author Bruce.Porteous
 *
 */
public interface PlayerListener {
	public void usingStrategy(PlayerStrategy strat);
}

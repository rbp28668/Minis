/*
 * WeightComparator.java
 * Created on 19-Oct-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis.strategy;

import java.util.Comparator;

/**
 * WeightComparator
 * @author Bruce.Porteous
 *
 */
public class WeightComparator implements Comparator<PlayerStrategy> {

	/**
	 * 
	 */
	public WeightComparator() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(PlayerStrategy ps0, PlayerStrategy ps1) {
		float w0 = ps0.getWeight();
		float w1 = ps1.getWeight();
		
		System.out.println(w0 + "," + w1);
		if(w1 > w0) {
			return 1;
		}
		
		if(w0 < w1){
			return -1;
		}
		
		return 0;
	}

}

/*
 * MathX.java
 * Created on 05-Dec-2005
 * By Bruce.Porteous
 *
 */
package uk.co.alvagem.minis;

/**
 * MathX - math extensions - primarily for JRE 1.4 compatibility.
 * @author Bruce.Porteous
 *
 */
public class MathX {
	public static float signum(float arg){
		if(arg < 0){
			return -1.0f;
		}
		if(arg > 0){
			return 1.0f;
		}
		return 0.0f;
	}
}

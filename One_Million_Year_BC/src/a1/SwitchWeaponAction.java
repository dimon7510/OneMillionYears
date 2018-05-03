package a1;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.input.action.Action;

public class SwitchWeaponAction extends AbstractInputAction {

	private MillionYears game;
	
	public SwitchWeaponAction(MillionYears g)
	{
		game = g;
	}
	public void performAction(float arg0, Event arg1) {
		int activeWeapon = (((Player)game.gameColl.localPlayer).getActiveWeapon()+1)%3;
		((Player)game.gameColl.localPlayer).setActiveWeapon(activeWeapon);
		 System.out.println(((Player)game.gameColl.localPlayer).getActiveWeapon());

	}
}

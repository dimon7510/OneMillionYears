package a1;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.input.action.Action;
import ray.rage.scene.SkeletalEntity;

public class SwitchWeaponAction extends AbstractInputAction {

	private MillionYears game;
	
	public SwitchWeaponAction(MillionYears g)
	{
		game = g;
	}
	public void performAction(float arg0, Event arg1) {
			//changing weapon
		int activeWeapon = (((Player)game.gameColl.localPlayer).getActiveWeapon()+1)%3;
		((Player)game.gameColl.localPlayer).setActiveWeapon(activeWeapon);
		 System.out.println(((Player)game.gameColl.localPlayer).getActiveWeapon());
		 
		 	//if active weapon is hand - delete stone and spear from hand
		 if (activeWeapon==0)
		 {
			 game.gameColl.localPlayerEntity.stopAnimation( );
			 game.gameColl.localPlayerEntity.playAnimation( "WalkAnimation", 1.0f, SkeletalEntity.EndType.LOOP, 0);
				 game.spearWeaponEnt.setVisible(false);
				 game.stoneWeaponEnt.setVisible(false);
		 }
		 
		 	//if weapon is stone change to WalkWeaponAction and show stone in hand if has
		 if (activeWeapon==1)
		 {
			 game.spearWeaponEnt.setVisible(false);
			 game.gameColl.localPlayerEntity.stopAnimation( );
			 game.gameColl.localPlayerEntity.playAnimation( "WalkWeaponAnimation", 1.0f, SkeletalEntity.EndType.LOOP, 0);
			 if ( ((Player)game.gameColl.localPlayer).getStones()>0)
			 {
				 game.stoneWeaponEnt.setVisible(true);
			 }
		 }
			 
		//if weapon is spear change to WalkWeaponAction and show speare in hand if has
		 if (activeWeapon==2)
		 {
			 game.stoneWeaponEnt.setVisible(false);
			 game.gameColl.localPlayerEntity.stopAnimation( );
			 game.gameColl.localPlayerEntity.playAnimation( "WalkWeaponAnimation", 1.0f, SkeletalEntity.EndType.LOOP, 0);
			 if ( ((Player)game.gameColl.localPlayer).getSpears()>0)
			 {
				 game.spearWeaponEnt.setVisible(true);
			 }
		 }
	}
}

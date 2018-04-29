package a1;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.SkeletalEntity;

public class StopMovementAction extends AbstractInputAction{
	private MillionYears game;
	   

	   //constructor for game 
	   public StopMovementAction(MillionYears g)
	   { game = g;
	   }
	     
	   public void performAction(float time, Event e)
	   { 
	      SkeletalEntity localPlayerSEnt=  game.gameColl.localPlayerEntity;
	      System.out.println("stop walk");
	      	//if player is  walking - stop walking
	      if ( ((Player)game.gameColl.localPlayer).isWalking()) 
	      {
	    	  ((Player)game.gameColl.localPlayer).setWalking(false);
	    	  localPlayerSEnt.stopAnimation();
	      }
	   }
}

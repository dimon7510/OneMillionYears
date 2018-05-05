package a1;

import myGameEngine.ProtocolClient;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.input.action.Action;
import ray.rage.scene.SkeletalEntity;
import ray.rml.Vector3f;

public class MoveRightAction extends AbstractInputAction {

	   private MillionYears game;
	   private float localTimeElapsed;
	   private float localSpeed;
	   private ProtocolClient clientProtocol;

	   //constructor for game and timeInterval
	   public MoveRightAction(MillionYears g, ProtocolClient cp)
	   { 
		   game = g;
		   localTimeElapsed=0.0f;
		   localSpeed = 0.0f;
		   clientProtocol = cp;
	   }
	     
	   public void performAction(float time, Event e)
	   {   
	      localTimeElapsed = time/1000.0f;
	      localSpeed = ( (Player)game.gameColl.localPlayer).getSpeed()*0.5f;
	    
	      if ( ( (Player)game.gameColl.localPlayer).isAlive()&& game.GameStart )
	      {
	    	  game.gameColl.localPlayerNode.moveLeft(localTimeElapsed*localSpeed); 
	    	  clientProtocol.sendMoveMessage(game.gameColl.localPlayerNode.getWorldPosition());
	    	  game.updateVerticalPosition();
	    	  
	    	//if player is not walking - start walking
		      if (! ((Player)game.gameColl.localPlayer).isWalking()) 
		      {
		    	  //animating player
		          SkeletalEntity localPlayerSEnt=  game.gameColl.localPlayerEntity;
		    	  ((Player)game.gameColl.localPlayer).setWalking(true);
		    	  localPlayerSEnt.stopAnimation();
		    	  	//if without weapon - just walk
		    	  System.out.println("should walk");
		    	  if (((Player)game.gameColl.localPlayer).getActiveWeapon() == 0)
		    		  localPlayerSEnt.playAnimation( "WalkAnimation", 1.0f, SkeletalEntity.EndType.LOOP, 0);
		    	  	//else walk with weapon
		    	  else  localPlayerSEnt.playAnimation( "WalkWeaponAnimation", 1.0f, SkeletalEntity.EndType.LOOP, 0);	  
		      }
	      }
	    	 
	   } 
}

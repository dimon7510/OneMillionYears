package a1;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.input.action.Action;
import ray.rage.scene.SkeletalEntity;

public class FireAction extends AbstractInputAction{
	  	private MillionYears game;
	   private float localTimeElapsed;
	   private float localSpeed;
	   
	   public FireAction (MillionYears g)
	   { game = g;
	   localTimeElapsed = 0.0f;
	   localSpeed = 0.0f;
	   }


	public void performAction(float time, Event e) {
		  localTimeElapsed = time/1000.0f;
		  System.out.println("should throw");
	      localSpeed = ( (Player)game.gameColl.localPlayer).getSpeed()*0.5f;
	      
	      
	      if ( ( (Player)game.gameColl.localPlayer).isAlive()&& MillionYears.GameStart )
	      {
	    	  ((Player)game.gameColl.localPlayer).setThrowing(true);
	    	  SkeletalEntity localPlayerSEnt=  game.gameColl.localPlayerEntity;
	    	  if (! ((Player)game.gameColl.localPlayer).isWalking()) 
	          {
	              //animating (throw) if not walking
	    		  System.out.println("stand throw");
        		  localPlayerSEnt.playAnimation( "StandThrowAnimation", 1.0f, SkeletalEntity.EndType.STOP, 0);
	        	  	 	  
	          }
	    	  else 
	    	  {
	    		//animating (throw) if walking
	    		  System.out.println("walk throw");
        		  localPlayerSEnt.playAnimation( "WalkThrowAnimation", 1.0f, SkeletalEntity.EndType.STOP, 0);
        		  localPlayerSEnt.playAnimation( "WalkAnimation", 1.0f, SkeletalEntity.EndType.LOOP, 0);
	    	  }
	      }
	      

	}

}

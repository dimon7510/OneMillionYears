package a1;

import myGameEngine.ProtocolClient;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.input.action.Action;
import ray.rage.scene.SkeletalEntity;

public class MoveForwardBackwardActionJoy extends AbstractInputAction
{
   private MillionYears game;
   float localTimeElapsed;
   float localSpeed;
   private ProtocolClient clientProtocol;
   //constructor for game and timeInterval and localSpeed
   public MoveForwardBackwardActionJoy(MillionYears g, ProtocolClient cp)
   { 
	   game = g;
	   localTimeElapsed=0.0f;
	   localSpeed =0.0f;
	   clientProtocol = cp;
   }
       
   public void performAction(float time, Event e)
   { 
      localTimeElapsed = time/1000.0f;
      localSpeed = ( (Player)game.gameColl.localPlayer).getSpeed();
      //if player is alive
      if ( ( (Player)game.gameColl.localPlayer).isAlive() && MillionYears.GameStart)
      {
    	  	//move forward by Joystick
    	  if (e.getValue() < -0.3)
          {   
    		  game.gameColl.localPlayerNode.moveForward(localTimeElapsed*localSpeed);
        	  game.updateVerticalPosition();  
          } 
    	  	//move backward by Joystick
          if (e.getValue() > 0.3)
          {
             game.gameColl.localPlayerNode.moveBackward(localTimeElapsed*localSpeed);
             game.updateVerticalPosition();  
          } 
         
          	//if player is not walking - start walking
          if (! ((Player)game.gameColl.localPlayer).isWalking()) 
          {
              //animating player
              SkeletalEntity localPlayerSEnt=  game.gameColl.localPlayerEntity;
        	  ((Player)game.gameColl.localPlayer).setWalking(true);
        	  localPlayerSEnt.stopAnimation();
        	  	//if without weapon - just walk
        	  if (((Player)game.gameColl.localPlayer).getActiveWeapon() == 0)
        	  {
        		  System.out.println("should walk");
        		  localPlayerSEnt.playAnimation( "WalkAnimation", 1.0f, SkeletalEntity.EndType.LOOP, 0);
        	  }
        	  	//else walk with weapon
        	  else  localPlayerSEnt.playAnimation( "WalkWeaponAnimation", 1.0f, SkeletalEntity.EndType.LOOP, 0);	  
          }
          clientProtocol.sendMoveMessage(game.gameColl.localPlayerNode.getWorldPosition());
      }
      
   }                 
}

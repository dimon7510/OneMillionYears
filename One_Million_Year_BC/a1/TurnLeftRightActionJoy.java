package a1;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.input.action.Action;
import ray.rml.Angle;
import ray.rml.Degreef;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class TurnLeftRightActionJoy extends AbstractInputAction{

	   private MillionYears game;
	   float localTimeElapsed;
	   //constructor for game and timeInterval
	   public TurnLeftRightActionJoy(MillionYears g)
	   { game = g;
	   localTimeElapsed = 0.0f;
	   }
	       
	   public void performAction(float time, Event e)
	   {  
		  Angle rotAmt = Degreef.createFrom( 0.0f);
	      localTimeElapsed = time/50.0f;
	      float rotAmtFloat = 0.0f;
	      
	      if (e.getValue() < -0.3)
	      {
	    	  	//turning dolphin
			  rotAmt = Degreef.createFrom(localTimeElapsed);
			  rotAmtFloat = localTimeElapsed;
	      }
	      
	      if (e.getValue() > 0.3)
	      {
	         rotAmt = Degreef.createFrom(-localTimeElapsed);
	         rotAmtFloat = -localTimeElapsed;
	      }
	      //turn dolphin at rotAmount
	      game.gameColl.localPlayerNode.yaw(rotAmt);
		  	//turning camera at same degree
		  game.orbitController.setCameraAzimuth(game.orbitController.getCameraAzimuth() + rotAmtFloat);
		  game.orbitController.setCameraAzimuth(game.orbitController.getCameraAzimuth() % 360);	
	  }    
}

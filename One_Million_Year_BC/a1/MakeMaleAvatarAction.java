package a1;


import java.io.IOException;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.Engine;
import ray.rage.rendersystem.states.TextureState;
import ray.rage.scene.SceneManager;

import ray.rage.asset.texture.Texture;
import ray.rage.rendersystem.Renderable.Primitive;
import ray.rage.rendersystem.states.RenderState;
import ray.rage.scene.ManualObject;
import ray.rage.scene.ManualObjectSection;
import ray.rage.scene.SceneNode;
import ray.rage.scene.SkeletalEntity;

public class MakeMaleAvatarAction extends AbstractInputAction {
	private MillionYears game;
	private Engine eng;
	
	 public MakeMaleAvatarAction(MillionYears g, Engine e)
	   { game = g;
	   eng=e;
	   }

	@Override
	public void performAction(float time, Event e) {
		// if game not started initialize mark started
		if (!MillionYears.GameStart)
		{
			System.out.println("male created");
			try {
				ttt();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
	}

	public void ttt () throws IOException
	{
		MillionYears.GameStart = true;
		SceneManager sm = eng.getSceneManager();
			//delete lobby avatars
		sm.destroySceneNode( game.lobbyPlayerManNode);
		sm.destroySceneNode( game.lobbyPlayerFemaleNode);
		sm.destroyEntity( game.lobbyPlayerManEntity.getName());
		sm.destroyEntity( game.lobbyPlayerFemaleEntity.getName());
		
			//create Male avatar
		//Making Local Player
		((Player)game.gameColl.localPlayer).setMale(true);
	    			    	
	    	//rage animated Player Entity
		
	    game.gameColl.localPlayerEntity = sm.createSkeletalEntity("playerSkEntity2", "man4.rkm", "man4.rks");
	  
	    	//create texture for plater entity
    	Texture playerText = sm.getTextureManager().getAssetByPath("man2.jpg");   
    	TextureState tstate2 = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);   
    	tstate2.setTexture(playerText);   
    	game.gameColl.localPlayerEntity.setRenderState(tstate2); 	
    	game.gameColl.localPlayerEntity.setPrimitive(Primitive.TRIANGLES);
    	
	    //making local Player Node
    	game.gameColl.localPlayerNode.detachObject(game.gameColl.localPlayerEntity0);
    	game.gameColl.localPlayerNode.attachObject(game.gameColl.localPlayerEntity);
    	sm.destroyEntity(game.gameColl.localPlayerEntity0);
	   	    
	    	//loading Animation of LocalPlayer male
	    game.gameColl.localPlayerEntity.loadAnimation("WalkAnimation", "man4-walking.rka");
	    game.gameColl.localPlayerEntity.loadAnimation("StandPunchAnimation", "man4-standingPunch.rka");
	    game.gameColl.localPlayerEntity.loadAnimation("StandThrowAnimation", "man4-standingThrow.rka");
	    game.gameColl.localPlayerEntity.loadAnimation("WalkPunchAnimation", "man4-walkingPunch.rka");
	    game.gameColl.localPlayerEntity.loadAnimation("WalkWeaponAnimation", "man4-walkingWeapon.rka");
	    game.gameColl.localPlayerEntity.loadAnimation("WalkThrowAnimation", "man4-walkingThrow.rka");
		game.setupNetworking();
		game.setIsConnected(true);
	}
}

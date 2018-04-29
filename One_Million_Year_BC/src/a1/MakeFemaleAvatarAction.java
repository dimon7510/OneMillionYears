package a1;

import java.io.IOException;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.input.action.Action;
import ray.rage.Engine;
import ray.rage.asset.texture.Texture;
import ray.rage.rendersystem.Renderable.Primitive;
import ray.rage.rendersystem.states.RenderState;
import ray.rage.rendersystem.states.TextureState;
import ray.rage.scene.SceneManager;

public class MakeFemaleAvatarAction extends AbstractInputAction {
	private MillionYears game;
	private Engine eng;
	
	 public MakeFemaleAvatarAction(MillionYears g, Engine e)
	   { game = g;
	   eng=e;
	   }
	@Override
	public void performAction(float arg0, Event arg1) {
			// if game not started initialize mark started
			if (!MillionYears.GameStart)
			{
				try {
					System.out.println("male created");
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
			((Player)game.gameColl.localPlayer).setMale(false);
		    			    	
		    	//rage animated Player Entity
		    game.gameColl.localPlayerEntity = sm.createSkeletalEntity("playerSkEntity2", "female4.rkm", "female4.rks");
		    	//create texture for plater entity
	    	Texture playerText = sm.getTextureManager().getAssetByPath("female4.jpg");   
	    	TextureState tstate2 = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);   
	    	tstate2.setTexture(playerText);   
	    	game.gameColl.localPlayerEntity.setRenderState(tstate2); 	
	    	game.gameColl.localPlayerEntity.setPrimitive(Primitive.TRIANGLES);
	    	
	    	 //making local Player Node
	    	game.gameColl.localPlayerNode.detachObject(game.gameColl.localPlayerEntity0);
	    	game.gameColl.localPlayerNode.attachObject(game.gameColl.localPlayerEntity);
	    	sm.destroyEntity(game.gameColl.localPlayerEntity0);
		       
		    	//loading Animation of LocalPlayer male
		    game.gameColl.localPlayerEntity.loadAnimation("WalkAnimation", "female-walking.rka");
		    game.gameColl.localPlayerEntity.loadAnimation("StandPunchAnimation", "female-standingPunch.rka");
		    game.gameColl.localPlayerEntity.loadAnimation("StandThrowAnimation", "female-standingThrow.rka");
		    game.gameColl.localPlayerEntity.loadAnimation("WalkPunchAnimation", "female-walkingPunch.rka");
		    game.gameColl.localPlayerEntity.loadAnimation("WalkWeaponAnimation", "female-walkingWeapon.rka");
		    game.gameColl.localPlayerEntity.loadAnimation("WalkThrowAnimation", "female-walkingThrow.rka");
			
		}

}

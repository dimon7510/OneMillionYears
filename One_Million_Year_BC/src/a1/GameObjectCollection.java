package a1;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Vector;

import ray.rage.Engine;
import ray.rage.asset.texture.Texture;
import ray.rage.asset.texture.TextureManager;
import ray.rage.rendersystem.RenderSystem;
import ray.rage.rendersystem.Renderable.DataSource;
import ray.rage.rendersystem.Renderable.Primitive;
import ray.rage.rendersystem.shader.GpuShaderProgram;
import ray.rage.rendersystem.states.FrontFaceState;
import ray.rage.rendersystem.states.RenderState;
import ray.rage.rendersystem.states.TextureState;
import ray.rage.scene.Entity;
import ray.rage.scene.ManualObject;
import ray.rage.scene.ManualObjectSection;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rage.scene.SkeletalEntity;
import ray.rage.util.BufferUtil;
import ray.rml.Angle;
import ray.rml.Degreef;

import static ray.rage.scene.SkeletalEntity.EndType.*;

public class GameObjectCollection {
	
	public static int AmountOfStones;
	public static int AmountOfSpears;
	public static int AmountOfPlants;
	public static int AmountOfRocks;
	
	public static float aimScale;
	public static float aimHeight;
	
	public static int RockScale;
	public static String StoneFile = "stone2.obj";
	public static String SpearFile = "cone.obj";
	public static String PlantFile = "earth.obj";
	public static String RockFile = "rock1.obj";
	public static String PlayerFile = "localPlayer";
	public static String PlayerRageFile = "man4.rkm";
	public static String PlayerRageFileFemale = "female4.rkm";
	public static String AimFile = "sphere.obj";
	
	
	///Declaring stone and Arrays of stone entities and stone SceneNodes
	protected Vector<GameObject> stoneVector  = new Vector<GameObject>();
	protected Vector<Entity> stoneEntityVector = new Vector<Entity>();
	protected  Vector<SceneNode> stoneNodeVector = new Vector<SceneNode>();
	   
	//declaring spear and arrays of Spear entities and array of Spear SceneNodes(in our case 2 dolphins)
	protected Vector<GameObject> spearVector = new Vector<GameObject>();
	protected Vector<Entity>  spearEntityVector = new Vector<Entity>();
	protected Vector<SceneNode> spearNodeVector = new Vector<SceneNode>();
	   
	///Declaring Plant and arrays of plant entities and Plant SceneNodes
	protected GameObject plantArray [] = new Plant [AmountOfPlants] ;
	protected Entity [] plantEntityArray = new Entity [AmountOfPlants];
	protected SceneNode [] plantNodeArray = new SceneNode [AmountOfPlants];
	
	///Declaring Rocks and arrays of Rock entities and Rock SceneNodes
	protected GameObject rockArray [] = new Rock [AmountOfRocks] ;
	protected Entity [] rockEntityArray = new Entity [AmountOfRocks];
	protected SceneNode [] rockNodeArray = new SceneNode [AmountOfRocks];
	
	//Declaring parent Nodes for Stones, Spears and Plants, and Rocks
	protected SceneNode spearParentNode, stoneParentNode, plantParentNode, rockParentNode;
	
	//Declaring Local Player
	protected GameObject localPlayer;
	protected Entity localPlayerEntity0;
	protected SkeletalEntity localPlayerEntity;
	protected SceneNode localPlayerNode;
	//declaring aim for local palyer
	protected Entity playerAimEntity;
	protected SceneNode playerAimNode;
	
	//declaring vector of ghost players;
	protected Vector<GameObject> ghostPlayerVector = new Vector<GameObject>();
	protected Vector<Entity> ghostPlayerEntityVector = new Vector<Entity>();
	protected Vector<SceneNode> ghostPlayerNodeVector = new Vector<SceneNode>();
	

	
	public GameObjectCollection (Engine eng, SceneManager sm, boolean male) throws IOException 
	{
		 //Making Arrays Stones------------------------------
			//making parent node for Stones
		stoneParentNode = sm.getRootSceneNode().createChildSceneNode( "stoneParentNode");
		
        for (int i =0; i<AmountOfStones; i++)
        {
        		//making array of stones
        	stoneVector.add(new Stone (StoneFile));
            	//Create entity array for stones 
        	stoneEntityVector.add(sm.createEntity("myStoneEnt"+i,StoneFile ));
        	stoneEntityVector.lastElement().setPrimitive(Primitive.TRIANGLES);
            	//Create Node array for Stones
        	stoneNodeVector.add(stoneParentNode.createChildSceneNode(stoneEntityVector.lastElement().getName() + "Node"+i));
        	stoneNodeVector.lastElement().attachObject(stoneEntityVector.lastElement());
            	//Set locations and size for stone Nodes
        	stoneNodeVector.lastElement().setLocalPosition(stoneVector.lastElement().getLocation() );
        	float stoneScale = ((Stone) stoneVector.lastElement()).getSize();
        	stoneNodeVector.lastElement().scale(stoneScale, stoneScale, stoneScale );
        }
        
        //Making arrays of Spears----------------------------------------
        	//making parent node for Spears
        spearParentNode = sm.getRootSceneNode().createChildSceneNode( "spearParentNode");
        for (int i=0; i<AmountOfSpears;i++)
        {
             	//making array of spears
        	spearVector.add( new Spear(SpearFile));
        		//making Entity array for Spears
        	spearEntityVector.add(sm.createEntity("mySpearEnt"+i,SpearFile ));
        	spearEntityVector.lastElement().setPrimitive(Primitive.TRIANGLES);
        		//Making Node Array for Spears
        	spearNodeVector.add(spearParentNode.createChildSceneNode(spearEntityVector.lastElement().getName() + "Node"+i));
        	spearNodeVector.lastElement().attachObject(spearEntityVector.lastElement());
        		//Set locations for spears Nodes
        	spearNodeVector.lastElement().setLocalPosition(spearVector.lastElement().getLocation() );
        }
        
        /*
        //Making arrays of Plants----------------------------------------
    		//making parent node for Plants
	    plantParentNode = sm.getRootSceneNode().createChildSceneNode( "plantParentNode");
	    for (int i=0; i<AmountOfPlants;i++)
	    {
	         	//making array of spears
	    	plantArray[i] = new Plant(PlantFile);
	    		//making Entity array for Plants
	    	plantEntityArray[i] = sm.createEntity("myPlantEnt"+i,PlantFile );
	    	plantEntityArray[i].setPrimitive(Primitive.TRIANGLES);
	    		//Making Node Array for Plants
	    	plantNodeArray[i] = plantParentNode.createChildSceneNode(plantEntityArray[i].getName() + "Node"+i);
	    	plantNodeArray[i].attachObject(plantEntityArray[i]);
	    		//Set locations and different scales for Plant Nodes
	    	plantNodeArray[i].setLocalPosition(plantArray[i].getLocation() );
	    	float scaleFactor = 1.0f/(float)( (Plant)plantArray[i]).getSize() ;
	    	plantNodeArray[i].scale(scaleFactor,scaleFactor,scaleFactor);	
	    }
          */
        
	    //Making arrays of Rocks----------------------------------------
			//making parent node for Rocks
	    rockParentNode = sm.getRootSceneNode().createChildSceneNode( "rockParentNode");
	    for (int i=0; i<AmountOfRocks;i++)
	    {
	         	//making array of rocks
	    	rockArray[i] = new Rock(RockFile);
	    		//making Entity array for Rocks
	    	rockEntityArray[i] = sm.createEntity("myRockEnt"+i,RockFile );
	    	rockEntityArray[i].setPrimitive(Primitive.TRIANGLES);
	    		//Making Node Array for Rocks
	    	rockNodeArray[i] = rockParentNode.createChildSceneNode(rockEntityArray[i].getName() + "Node"+i);
	    	rockNodeArray[i].attachObject(rockEntityArray[i]);
	    		//Set locations and different scales for  Nodes
	    	rockNodeArray[i].setLocalPosition(rockArray[i].getLocation() );
	    	float scaleFactor = (float)( (Rock)rockArray[i]).getSize() ;
	    	rockNodeArray[i].scale(scaleFactor,scaleFactor,scaleFactor);	
	    }
	      
	    
        //Making Local Player
	    localPlayer = new Player(PlayerFile, male, 0);
	    
	 
	    	//making Local Player Entity
	    localPlayerEntity0 = sm.createEntity("player", "man4.obj");
	    localPlayerEntity0.setVisible(false);
	    	/*
	    	//rage animated Player Entity
	    localPlayerEntity = sm.createSkeletalEntity("playerSkEntity", PlayerRageFile, "man4.rks");
	    	//create texture for plater entity
    	Texture playerText = sm.getTextureManager().getAssetByPath("man2.jpg");   
    	TextureState tstate2 = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);   
    	tstate2.setTexture(playerText);   
    	localPlayerEntity.setRenderState(tstate2); 	
	    localPlayerEntity.setPrimitive(Primitive.TRIANGLES);
	    */
	    //making local Player Node
	    localPlayerNode = sm.getRootSceneNode().createChildSceneNode(localPlayerEntity0.getName() + "Node");
	    localPlayerNode.attachObject(localPlayerEntity0);
	    	//set location for local player and size 
	    localPlayerNode.setLocalPosition(localPlayer.getLocation() );
	    float scaleFactor = (float) ((Player) localPlayer).getSize() ;
	    localPlayerNode.scale(scaleFactor, scaleFactor, scaleFactor);	
	    //localPlayerNode.pitch((Angle)Degreef.createFrom(90.0f));
	    /*
	    	//loading Animation of LocalPlayer male
	    localPlayerEntity.loadAnimation("manWalkAnimation", "man4-walking.rka");
	    localPlayerEntity.loadAnimation("manStandPunchAnimation", "man4-standingPunch.rka");
	    localPlayerEntity.loadAnimation("manStandThrowAnimation", "man4-standingThrow.rka");
	    localPlayerEntity.loadAnimation("manWalkPunchAnimation", "man4-walkingPunch.rka");
	    localPlayerEntity.loadAnimation("manWalkWeaponAnimation", "man4-walkingWeapon.rka");
	    localPlayerEntity.loadAnimation("manWalkThrowAnimation", "man4-walkingThrow.rka");
	    	//loading Animation of LocalPlayer female
	    localPlayerEntity.loadAnimation("femaleWalkAnimation", "female-walking.rka");
	    localPlayerEntity.loadAnimation("femaleStandPunchAnimation", "female-standingPunch.rka");
	    localPlayerEntity.loadAnimation("femaleStandThrowAnimation", "female-standingThrow.rka");
	    localPlayerEntity.loadAnimation("femaleWalkPunchAnimation", "female-walkingPunch.rka");
	    localPlayerEntity.loadAnimation("femaleWalkWeaponAnimation", "female-walkingWeapon.rka");
	    localPlayerEntity.loadAnimation("femaleWalkThrowAnimation", "female-walkingThrow.rka");
	    */
	    	//making aim entity
	    playerAimEntity = sm.createEntity("myPlayerAimEnt",AimFile );
	    playerAimNode = localPlayerNode.createChildSceneNode("myAimNode");
	    playerAimNode.attachObject(playerAimEntity);
	    playerAimNode.scale(aimScale, aimScale, aimScale);
	    playerAimNode.setLocalPosition(-0.8f,aimHeight,0.0f);
	    
          	
	}
	   

}

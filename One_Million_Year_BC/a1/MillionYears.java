package a1;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JOptionPane;
import javax.script.Invocable;
import java.io.*;
import java.util.*;
import java.util.List;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import net.java.games.input.AbstractComponent;
import net.java.games.input.Component;
import net.java.games.input.Event;

import myGameEngine.Camera3Pcontroller;
import myGameEngine.GhostAvatar;
import myGameEngine.GhostNPC;
import myGameEngine.ProtocolClient;
import ray.rage.asset.texture.*;
import ray.rage.util.*;
import java.awt.geom.*;

import ray.audio.AudioManagerFactory;
import ray.audio.AudioResource;
import ray.audio.AudioResourceType;
import ray.audio.IAudioManager;
import ray.audio.Sound;
import ray.audio.SoundType;
import ray.input.GenericInputManager;
import ray.input.InputManager;
import ray.input.action.Action;
import ray.networking.IGameConnection.ProtocolType;
import ray.rage.Engine;
import ray.rage.asset.texture.Texture;
import ray.rage.game.Game;
import ray.rage.game.VariableFrameRateGame;
import ray.rage.rendersystem.RenderSystem;
import ray.rage.rendersystem.RenderWindow;
import ray.rage.rendersystem.Renderable.DataSource;
import ray.rage.rendersystem.Renderable.Primitive;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.rage.rendersystem.shader.GpuShaderProgram;
import ray.rage.rendersystem.states.*;
import ray.rage.scene.Camera;
import ray.rage.scene.Entity;
import ray.rage.scene.Light;
import ray.rage.scene.ManualObject;
import ray.rage.scene.ManualObjectSection;
import ray.rage.scene.Node;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rage.scene.SkyBox;
import ray.rage.scene.Tessellation;
import ray.rage.scene.Camera.Frustum.Projection;
import ray.rage.scene.controllers.RotationController;
import ray.rage.util.BufferUtil;
import ray.rml.Degreef;
import ray.rml.Vector3;
import ray.rml.Vector3f;
import static ray.rage.scene.SkeletalEntity.EndType.*;

public class MillionYears extends VariableFrameRateGame  
{
	// to minimize variable allocation in update()
		GL4RenderSystem rs;
	    float updateTime;
		float elapsTime = 0.0f; 	
		String elapsTimeStr, dispStr;
	 	int elapsTimeSec;
	 	float halfSec = 0.5f;
	   
	   //game static Logic variables
	   public static float HeightOfWorld ;
	   public static int DimentionOfWorld;
	   public static final float CameraElevation = 5.0f;
	   public static boolean GameStart = false;
	   
	   //game logic variables
	   private boolean male = true;
	   
	   //skyBox variables
	   private static final String SKYBOX_NAME = "SkyBox";
	   private boolean skyBoxVisible = true;
	   
	   //gameCollection variable
	   public GameObjectCollection gameColl;
	   
	   //declaring scene nodes
	   protected SceneNode rootNode, skyBoxNode;
	   protected SceneNode lobbyPlayerFemaleNode, lobbyPlayerManNode;
	   
	   //declaring Entities
	   protected Entity lobbyPlayerManEntity, lobbyPlayerFemaleEntity;
	 
	   
	   //declaring camera node
	   private Camera myCamera;
	   private SceneNode cameraNode;
	   public Camera3Pcontroller orbitController;
	   
	   //Declaring input manager and actions 
	   private InputManager im;
	   private Action quitGameAction, stopMovementAction; 
	   private Action moveForwardAction, moveForwardBackwardActionJoy, moveBackwardAction;
	   private Action moveLeftAction, moveLeftRightActionJoy, moveRightAction; 
	   private Action turnLeftAction, turnRightAction, turnLeftRightActionJoy;
	   private Action fireAction, switchWeaponAction;
	   private Action makeMaleAvatarAction, makeFemaleAvatarAction;
	   
	   
	   //Game's networking variables
	   private String serverAddress;
	   private int serverPort;
	   private ProtocolType serverProtocol;
	   private ProtocolClient clientProtocol;
	   private boolean isConnected;
	   
	   //Sound
	   private IAudioManager audioMng;
	   private Sound walkingSound;
	   
	   //gameCollection variable
	   public Vector<UUID> gameObjectsToRemove;
	   
	   //Declaring Javascript variables
	   protected ScriptEngine jsEngine;
	   protected File scriptFile;
	   
		public MillionYears(String serverAddr, int sPort) 
	    {
	        super();
	        this.serverAddress = serverAddr;
	        this.serverPort = sPort;
	        this.serverProtocol = ProtocolType.UDP;
	    }
		
		public static void main(String[] args) {
	    	 Game game = new MillionYears(args[0], Integer.parseInt(args[1]));
	        try {
	            game.startup();
	            game.run();
	        } catch (Exception e) {
	            e.printStackTrace(System.err);
	        } finally {
	            game.shutdown();
	            game.exit();
	        }
	    }
		
		@Override
		protected void setupWindow(RenderSystem rs, GraphicsEnvironment ge) {
			rs.createRenderWindow(new DisplayMode(1000, 700, 24, 60), false);
		}

		
	//NETWORKING FUNCTIONS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~// Ghost NPC //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
	public void addGhostNPCtoGameWorld(GhostNPC npc) throws IOException
	{
		SceneManager sceneManager = this.getEngine().getSceneManager();
		if (npc != null)
		{
			Entity ghostE = sceneManager.createEntity(npc.getID().toString(), "cube.obj");
			ghostE.setPrimitive(Primitive.TRIANGLES);
			SceneNode ghostN = sceneManager.getRootSceneNode().createChildSceneNode(npc.getID().toString() + "Node");
			ghostN.attachObject(ghostE);
			ghostN.setLocalPosition(npc.getPosition());
			ghostN.setLocalScale(0.5f, 0.5f, 0.5f);
		}
	}
	public void moveGhostNPCAroundGameWorld(GhostNPC npc, Vector3 pos)
	{
		npc.getSceneNode().setLocalPosition(pos);
	}
		
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~// Ghost Avatar //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
	public void addGhostAvatarToGameWorld(GhostAvatar avatar) throws IOException
	{
		SceneManager sceneManager = this.getEngine().getSceneManager();
    	if (avatar != null)
    	{
    		Entity ghostE = sceneManager.createEntity(avatar.getID().toString(), "man2.obj");
    		avatar.setEntity(ghostE);
    		ghostE.setPrimitive(Primitive.TRIANGLES);
    		SceneNode ghostN = sceneManager.getRootSceneNode().createChildSceneNode(avatar.getID().toString() + "Node");
    		avatar.setNode(ghostN);
    		ghostN.attachObject(ghostE);
    		ghostN.setLocalPosition(avatar.getPosition());
    		//ghostN.setLocalScale(1f, 1f, 1f);
    		avatar.setNode(ghostN);
    		avatar.setEntity(ghostE);
    		avatar.setPosition(ghostN.getLocalPosition());
    	}
    }
    
    public void moveGhostAvatarAroundGameWorld(GhostAvatar ghostA, Vector3 pos)
    {
    	ghostA.getSceneNode().setLocalPosition(pos);
    }
    
    public void removeGhostAvatarFromGameWorld(GhostAvatar ghostID)
    {
    	if (ghostID != null)
    	{
    		gameObjectsToRemove.add(ghostID.getID());
    	}
    }
    
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~// SetUp Network //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    public void setupNetworking()
    {
    	gameObjectsToRemove = new Vector<UUID>();
    	isConnected = false;
    	try
    	{
    		clientProtocol = new ProtocolClient(InetAddress.getByName(serverAddress), 
    				serverPort, serverProtocol, this);
    	}
    	catch(UnknownHostException e)
    	{
    		e.printStackTrace();
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    	if (clientProtocol == null)
    	{
    		System.out.println("Missing protocol host");
    	}
    	else
    	{
    		this.setupInputs(this.getEngine());
    		clientProtocol.sendJoinMessage();
    	}
    }
    
    public void processNetworking(float elapsTime)
    {
    	SceneManager sceneManager = this.getEngine().getSceneManager();
    	if (clientProtocol != null)
    	{
    		clientProtocol.processPackets();
    	}
    	Iterator<UUID> iterator = gameObjectsToRemove.iterator();
    	while(iterator.hasNext())
    	{
    		sceneManager.destroySceneNode(iterator.next().toString());
    	}
    	gameObjectsToRemove.clear();
    }
	
	//Camera setup Function
	    @Override
	    protected void setupCameras(SceneManager sm, RenderWindow rw) {
	        rootNode = sm.getRootSceneNode();
	        myCamera = sm.createCamera("MainCamera", Projection.PERSPECTIVE);
	        rw.getViewport(0).setCamera(myCamera);
			
			getCamera().setRt((Vector3f)Vector3f.createFrom(1.0f, 0.0f, 0.0f));
			getCamera().setUp((Vector3f)Vector3f.createFrom(0.0f, 1.0f, 0.0f));
			getCamera().setFd((Vector3f)Vector3f.createFrom(0.0f, 0.0f, -1.0f));
			getCamera().setPo((Vector3f)Vector3f.createFrom(0.0f, 0.0f, CameraElevation));
	     
	      cameraNode = rootNode.createChildSceneNode(myCamera.getName() + "Node");
	      cameraNode.attachObject(myCamera);
	      myCamera.getFrustum().setFarClipDistance(1000.0f);
	      getCamera().setMode('n');
	    }
	    
	  //Orbit camera setup Function--------------------------------------------------------------
	    protected void setupOrbitCameras(Engine eng, SceneManager sm) {
	    	
	    	//creating and attaching orbit camera moved by joystick or keyboard
	    	SceneNode localPlayerNode = gameColl.playerAimNode;
	    	String localPlayerControl;
	    	if (im.getFirstGamepadName()!=null)
	    		localPlayerControl = im.getFirstGamepadName();
	    	else localPlayerControl = im.getKeyboardName();
	    	orbitController = new Camera3Pcontroller(myCamera,cameraNode,localPlayerNode,localPlayerControl, im);	    		
	    }
	    
	//Scene setup Function----------------------------------------------------------------
	    @Override
	    protected void setupScene(Engine eng, SceneManager sm) throws IOException 
	    {	
	    	isConnected = false;
	    	
	    	 //Making SkyBox---------------
		    Configuration conf = eng.getConfiguration();
		    TextureManager tm = getEngine().getTextureManager();
		    tm.setBaseDirectoryPath(conf.valueOf("assets.skyboxes.path"));
		    Texture front = tm.getAssetByPath("front.jpg");
		    Texture back = tm.getAssetByPath("back.jpg");
		    Texture left = tm.getAssetByPath("left.jpg");
		    Texture right = tm.getAssetByPath("right.jpg");
		    Texture top = tm.getAssetByPath("top.jpg");
		    Texture bottom = tm.getAssetByPath("bottom.jpg");
		    tm.setBaseDirectoryPath(conf.valueOf("assets.textures.path"));
		    // cubemap textures are flipped upside-down.
		    // All textures must have the same dimensions, so any image’s
		    // heights will work since they are all the same height
		    AffineTransform xform = new AffineTransform();
		    xform.translate(0, front.getImage().getHeight());
		    xform.scale(1d, -1d);
		    front.transform(xform);
		    back.transform(xform);
		    left.transform(xform);
		    right.transform(xform);
		    top.transform(xform);
		    bottom.transform(xform);
		    SkyBox sb = sm.createSkyBox(SKYBOX_NAME);
		    sb.setTexture(front, SkyBox.Face.FRONT);
		    sb.setTexture(back, SkyBox.Face.BACK);
		    sb.setTexture(left, SkyBox.Face.LEFT);
		    sb.setTexture(right, SkyBox.Face.RIGHT);
		    sb.setTexture(top, SkyBox.Face.TOP);
		    sb.setTexture(bottom, SkyBox.Face.BOTTOM);
		    sm.setActiveSkyBox(sb);
		    
		    //Making Terrain-----
		    Tessellation tessE = sm.createTessellation("tessE", 7);
			// subdivisions per patch: min=0, try up to 32
			//tessE.setSubdivisions(8f);
			SceneNode tessNode =sm.getRootSceneNode().createChildSceneNode("tessN");
			tessNode.attachObject(tessE);
			// to move it, note that X and Z must BOTH be positive OR negative
			tessNode.translate(Vector3f.createFrom(0.0f, 0.0f, 0.0f));
			// tessN.yaw(Degreef.createFrom(37.2f));
			tessNode.scale(150, 20, 150);
			tessE.setHeightMap(this.getEngine(), "heightMapContrast.jpg");
			tessE.setTexture(this.getEngine(), "bottomText.jpg");
		    
			
	   //Setup light
	        sm.getAmbientLight().setIntensity(new Color(.1f, .1f, .1f));
			Light plight = sm.createLight("testLamp1", Light.Type.POINT);
			plight.setAmbient(new Color(0.7f, 0.7f, 0.7f));
	        plight.setDiffuse(new Color(1.0f, 1.0f, 1.0f));
			plight.setSpecular(new Color(1.0f, 1.0f, 1.0f));
	        plight.setRange(200f);
			
			SceneNode plightNode = sm.getRootSceneNode().createChildSceneNode("plightNode");
	        plightNode.attachObject(plight);
	        plightNode.translate(0.0f, 20.0f, 0.0f);

	        RotationController rc = new RotationController(Vector3f.createUnitVectorY(), .01f);
	        //rc.addNode(earthNode);
	        sm.addController(rc);
	        
		    
	    	//setup Javascript -----------------------------------
	    		// prepare script engine
	    	ScriptEngineManager factory = new ScriptEngineManager();
	    	List <ScriptEngineFactory> list = factory.getEngineFactories();
	    	jsEngine = factory.getEngineByName("js");
	    		//print out script engines
	    	System.out.println("Script Engine Factories found:");
	    	for (ScriptEngineFactory f : list)
	    	{ System.out.println(" Name = " + f.getEngineName() + " language = " + f.getLanguageName() + " extensions = " + f.getExtensions()); }
	    		//initialize scriptFile and run the script and get values for initial world setup
	    	scriptFile = new File("InitParams.js");
	    	this.runScript(jsEngine, scriptFile);
	    	DimentionOfWorld = ((int)(jsEngine.get("dimentionOfWorld"))) ;
	    	HeightOfWorld = ((Double)(jsEngine.get("heightOfObjects"))).floatValue() ;
	    	GameObjectCollection.AmountOfStones = ((int)(jsEngine.get("stones")));
	    	GameObjectCollection.AmountOfSpears = ((int)(jsEngine.get("spears")));
	    	GameObjectCollection.AmountOfPlants = ((int)(jsEngine.get("plants")));
	    	GameObjectCollection.AmountOfRocks = ((int)(jsEngine.get("rocks")));
	    	GameObjectCollection.aimScale = ((Double)(jsEngine.get("aimSize"))).floatValue() ;
	    	GameObjectCollection.aimHeight = ((Double)(jsEngine.get("aimHeight"))).floatValue() ;
	    	
	    	
	    	Plant.MinPlantSize = ((int)(jsEngine.get("minPlantSize")));
	    	Plant.MaxPlantSize = ((int)(jsEngine.get("maxPlantSize")));
	    	Rock.MinRockSize = ((int)(jsEngine.get("minRockSize")));
	    	Rock.MaxRockSize = ((int)(jsEngine.get("maxRockSize")));
	    	Stone.size = ((Double)(jsEngine.get("stoneSize"))).floatValue() ;
	    	Spear.size = ((Double)(jsEngine.get("spearSize"))).floatValue() ;
	    	Player.Life = ((int)(jsEngine.get("life")));
	    	Player.MaleStrength = ((int)(jsEngine.get("maleStrength")));
	    	Player.FemaleStrength = ((int)(jsEngine.get("femaleStrength")));
	    	Player.MaleSpeed = ((Double)(jsEngine.get("maleSpeed"))).floatValue() ;
	    	Player.FemaleSpeed = ((Double)(jsEngine.get("femaleSpeed"))).floatValue() ;
	    	Player.size = ((Double)(jsEngine.get("playerSize"))).floatValue();
	    	
	    	//Initializing GameWorld (Collection of all obejcts and players)------------
	    	 gameColl = new GameObjectCollection(eng, sm, male);
	    	
	    	//setup lobby avatars
	    	sm = eng.getSceneManager();
			//create male and female Avatar entities  for Lobby
	    	lobbyPlayerManEntity = sm.createEntity("lobbyMan", "man4.obj");
	    	lobbyPlayerManEntity.setPrimitive(Primitive.TRIANGLES);
	    	lobbyPlayerFemaleEntity = sm.createEntity("lobbyFemale", "female4.obj");
	    	lobbyPlayerFemaleEntity.setPrimitive(Primitive.TRIANGLES);
	    	//making male and female lobby Player Node
			lobbyPlayerManNode = sm.getRootSceneNode().createChildSceneNode(lobbyPlayerManEntity.getName() + "Node");
			lobbyPlayerManNode.attachObject(lobbyPlayerManEntity);
			lobbyPlayerFemaleNode = sm.getRootSceneNode().createChildSceneNode(lobbyPlayerFemaleEntity.getName() + "Node");
			lobbyPlayerFemaleNode.attachObject(lobbyPlayerFemaleEntity);
		    	//set location for lobby male and female nodes player and size 
			float scale = 1.5f*((Player) this.gameColl.localPlayer).getSize();
			lobbyPlayerManNode.setLocalPosition(this.gameColl.localPlayer.getLocation().add(0.7f, 0.0f, 0.0f));
			lobbyPlayerManNode.scale(scale, scale, scale);	
			lobbyPlayerManNode.yaw(Degreef.createFrom(180.0f));
			lobbyPlayerFemaleNode.setLocalPosition(this.gameColl.localPlayer.getLocation().add(-0.7f, 0.0f, 0.0f));
			lobbyPlayerFemaleNode.scale(scale, scale, scale);
			lobbyPlayerFemaleNode.yaw(Degreef.createFrom(180.0f));
			
			
	        setupInputs(eng);
	        setupOrbitCameras(eng, sm);
	        
	    	
	    }
	    
	//Update Function
	    @Override
	    protected void update(Engine engine) {
			
			updateTime = engine.getElapsedTimeMillis();
			elapsTime += updateTime;
			elapsTimeSec = Math.round(elapsTime/1000.0f);
			elapsTimeStr = Integer.toString(elapsTimeSec);
			
			// check every half second and stop moving if moving buttons or axis are released
			if (elapsTime>halfSec)
			{
				halfSec+=0.5f;
				checkStopMoving();
			}
			
			if (isConnected == true)
			{
				processNetworking(elapsTime);
			}
				
			//check if player have chosen avatar then start game
			if (GameStart)
			{
			
				// build and set HUD
				rs = (GL4RenderSystem) engine.getRenderSystem();
				int windowHeight = (int) (rs.getCanvas().getHeight() * 1.05);
				int windowWidth = rs.getCanvas().getWidth();
				((Player)gameColl.localPlayer).setDispStr();
				rs.setHUD( elapsTimeStr + ((Player)gameColl.localPlayer).getDispStr(), windowWidth / 100, windowHeight / 70);
				   
				
			      
				//game over if Player is not dead then process inputs--------------------
				if( ((Player)gameColl.localPlayer).isAlive())
				{
					im.update(updateTime);
					
					//Check collision-----------------------
					collisionDetection(engine);
					
					//Animation update
					gameColl.localPlayerEntity.update();
				}
				else { ((Player)gameColl.localPlayer).setStatusStr("  GAME OVER - you died.");}
				      
				//update camera  position
				if (orbitController != null)
				{
					orbitController.updateCameraPosition();
				}
				      
					
			} 
			else
			{	rs = (GL4RenderSystem) engine.getRenderSystem();
				int windowHeight = (int) (rs.getCanvas().getHeight() * 1.05);
				int windowWidth = rs.getCanvas().getWidth();
				rs.setHUD( elapsTimeStr + "Chose Avatar: M-key (male)/F-key (female) on KeyBoard. Y-button (male)/X-button (female) on GamePad", windowWidth / 100, windowHeight / 70);
			} 
			
			//update input 
			im.update(updateTime);
		}

	    
	    //Function checks if Moving Buttons are released and stops moving animation
	    protected void checkStopMoving()
	    {
	    	if ( MillionYears.GameStart && ((Player)this.gameColl.localPlayer).isWalking() )
	    	{
	    			//checking if all movable buttons are released
	    		if ( Math.abs(im.getGamepadController(1).getComponent(net.java.games.input.Component.Identifier.Axis.X).getPollData()) < 0.1f
	    				&& Math.abs(im.getGamepadController(1).getComponent(net.java.games.input.Component.Identifier.Axis.Y).getPollData()) < 0.1f
	    				&& im.getKeyboardController().getComponent(net.java.games.input.Component.Identifier.Key.A).getPollData()==0.0f
	    				&& im.getKeyboardController().getComponent(net.java.games.input.Component.Identifier.Key.W).getPollData()==0.0f
	    				&& im.getKeyboardController().getComponent(net.java.games.input.Component.Identifier.Key.D).getPollData()==0.0f
	    				&& im.getKeyboardController().getComponent(net.java.games.input.Component.Identifier.Key.S).getPollData()==0.0f)
	    		{
	    			this.gameColl.localPlayerEntity.stopAnimation();
	    			((Player)this.gameColl.localPlayer).setWalking(false);
	    		}
	    	}	
	    }
	



	//SetupInputs Function   
	    protected void setupInputs(Engine eng)
	   { 
	      im = new GenericInputManager();
	      String kbName = im.getKeyboardName();
	      String gpName = im.getFirstGamepadName();
	      
	      // build some action objects for doing things in response to user input
	      quitGameAction = new QuitGameAction(this);
	      moveForwardAction = new MoveForwardAction(this, clientProtocol);
	      moveForwardBackwardActionJoy = new MoveForwardBackwardActionJoy(this, clientProtocol);
	      moveBackwardAction = new MoveBackwardAction(this, clientProtocol);
	      moveLeftAction = new MoveLeftAction(this, clientProtocol);
	      moveRightAction = new MoveRightAction(this, clientProtocol);
	      moveLeftRightActionJoy = new MoveLeftRightActionJoy(this, clientProtocol);
	      turnLeftAction = new TurnLeftAction(this);
	      turnRightAction = new TurnRightAction(this);
	      turnLeftRightActionJoy = new TurnLeftRightActionJoy(this);
	      stopMovementAction = new StopMovementAction(this);
	      fireAction = new FireAction(this);
	      switchWeaponAction = new SwitchWeaponAction(this);
	      makeMaleAvatarAction = new MakeMaleAvatarAction(this, eng);
	      makeFemaleAvatarAction = new MakeFemaleAvatarAction(this, eng);
	    
	      
	      // attach the action objects to keyboard and gamepad components
	         //attach quit game action
	      im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.Z,
	      quitGameAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	      
	         //attach MoveForward action
	      im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.W,
	      moveForwardAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	       
	      
	         //attach MoveBackward action
	      im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.S,
	      moveBackwardAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	      
	         //attach MoveLeft action
	      im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.A,
	      moveLeftAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	     
	         //attach MoveRight action
	      im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.D,
	      moveRightAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	      
	         //attach TurnLeft action "yaw"
	      im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.LEFT,
	      turnLeftAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

	         //attach TurnRight action "yaw"
	      im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.RIGHT,
	      turnRightAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	      
	      //attach MakeMaleAvatar Action action from keyboard
	      im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.M,
	      makeMaleAvatarAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	      
	      //attach MakeFemaleAvatar Action action from keyboard
	      im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.F,
	      makeFemaleAvatarAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	      
	      //attach SwitchWeapon Action action from keyboard
	      im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.RETURN,
	      switchWeaponAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	      
	      //attach Fire Action action from keyboard
	      im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.SPACE,
	      fireAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	      
/*
	         //attach TurnUp action "pitch"
	      im.associateAction(kbName,
	      net.java.games.input.Component.Identifier.Key.UP,
	      turnUpAction,
	      InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	      
	         //attach TurnDown action "pitch"
	      im.associateAction(kbName,
	      net.java.games.input.Component.Identifier.Key.DOWN,
	      turnDownAction,
	      InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

	         //attach Toggle action 
	      im.associateAction(kbName,
	      net.java.games.input.Component.Identifier.Key.SPACE,
	      toggleAction,
	      InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
*/
	      
	      //check if first controller is pluged in and if yes enable controller actions
	      if(im.getFirstGamepadName()!=null)
	      {
	    	  
	            //attach quitGame  action on button of joystic
	         im.associateAction(gpName, net.java.games.input.Component.Identifier.Button._7,
	         quitGameAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	         
	            //attach MoveForwardBackwardActionJoy
	         im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.Y,
	         moveForwardBackwardActionJoy, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	   
	            //attach MoveLeftRightActionJoy
	         im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.X,
	         moveLeftRightActionJoy, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	         
	            //attach TurnLeftRightActionJoy
	         im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.RX,
	         turnLeftRightActionJoy, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	         
	         	//attach MakeMaleAvatar Action action from gamepad
		     im.associateAction(gpName, net.java.games.input.Component.Identifier.Button._3,
		     makeMaleAvatarAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		      
		      	//attach MakeFemaleAvatar Action action from gamepad
		     im.associateAction(gpName,net.java.games.input.Component.Identifier.Button._2,
		     makeFemaleAvatarAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		     
		 		//attach MakeFemaleAvatar Action action from gamepad
		     im.associateAction(gpName,net.java.games.input.Component.Identifier.Button._2,
		     makeFemaleAvatarAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		     
		 		//attach Switch Action
		     im.associateAction(gpName,net.java.games.input.Component.Identifier.Button._5,
		     switchWeaponAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	      
		     //attach Fire Action
		     im.associateAction(gpName,net.java.games.input.Component.Identifier.Button._0,
		     fireAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	      }
	      
	   }

	    //Function executes JavaScript-----------------------------------------
	    private void runScript(ScriptEngine engine, File scriptFileName)
	    {
		    try
		    { FileReader fileReader = new FileReader(scriptFileName);
		    engine.eval(fileReader); //execute the script statements in the file
		    fileReader.close();
		    }
		    catch (FileNotFoundException e1)
		    { System.out.println(scriptFileName + " not found " + e1); }
		    catch (IOException e2)
		    { System.out.println("IO problem with " + scriptFileName + e2); }
		    catch (ScriptException e3)
		    { System.out.println("ScriptException in " + scriptFileName + e3); }
		    catch (NullPointerException e4)
		    { System.out.println ("Null ptr exception in " + scriptFileName + e4); }
	    }   
	

	//Function returns distanse between camera and node----------------
	static public float distance (Camera camera, Node node)
	{
	   Vector3f cameraPos = camera.getPo();
	   Vector3f nodePos = (Vector3f)node.getWorldPosition();
	   Vector3 differense = cameraPos.sub(nodePos);
	   
	   return (float)differense.lengthSquared();
	}

	//Function returns distanse between 2 nodes----------------------
	static float distance (Node node1, Node node2)
	{
	   Vector3f node1Pos = (Vector3f)node1.getWorldPosition();
	   Vector3f node2Pos = (Vector3f)node2.getWorldPosition();
	   Vector3 differense = node1Pos.sub(node2Pos);
	   
	   return (float)differense.lengthSquared();
	}

	//Function returns distanse between camera and Vector---------------
	static float distance (Camera camera, Vector3 vector)
	{
	   Vector3f cameraPos = camera.getPo();
	   Vector3 differense = cameraPos.sub(vector);
	   
	   return (float)differense.lengthSquared();
	}

	   
	   void collisionDetection(Engine myEngine)
	   {
	      
	         Iterator <GameObject> stoneIter = this.gameColl.stoneVector.iterator();
	         Iterator <Entity> stoneEntityIter = this.gameColl.stoneEntityVector.iterator();
	         Iterator <SceneNode> stoneNodeIter = this.gameColl.stoneNodeVector.iterator();
	         Stone stoneCurrent;
	         Entity stoneEntityCurrent;
	         SceneNode stoneNodeCurrent;
	         
	         Iterator <GameObject> spearIter = this.gameColl.spearVector.iterator();
	         Iterator <Entity> spearEntityIter = this.gameColl.spearEntityVector.iterator();
	         Iterator <SceneNode> spearNodeIter = this.gameColl.spearNodeVector.iterator();
	         Spear spearCurrent;
	         Entity spearEntityCurrent;
	         SceneNode spearNodeCurrent;

	   		while ( stoneEntityIter.hasNext() ) 
	   		{
	   			stoneCurrent = (Stone)stoneIter.next();
	   			stoneEntityCurrent= stoneEntityIter.next();
	   			stoneNodeCurrent= stoneNodeIter.next();
	    
	            if(MillionYears.distance(this.gameColl.localPlayerNode,stoneNodeCurrent)<0.5f)
	            {
	               ((Player)this.gameColl.localPlayer).setStones( ((Player)this.gameColl.localPlayer).getStones()+1);
	               stoneIter.remove();
	               stoneEntityIter.remove();
	               myEngine.getSceneManager().destroySceneNode( stoneNodeCurrent);
	               stoneNodeIter.remove();
	               System.out.println("Stone picked");
	               return;
	            }
	   		}
	   		
	   		while ( spearEntityIter.hasNext() ) 
	   		{
	   			spearCurrent = (Spear)spearIter.next();
	   			spearEntityCurrent= spearEntityIter.next();
	   			spearNodeCurrent= spearNodeIter.next();
	    
	            if(MillionYears.distance(this.gameColl.localPlayerNode,spearNodeCurrent)<0.5f)
	            {
	               ((Player)this.gameColl.localPlayer).setSpears( ((Player)this.gameColl.localPlayer).getSpears()+1);
	               spearIter.remove();
	               spearEntityIter.remove();
	               myEngine.getSceneManager().destroySceneNode( spearNodeCurrent);
	               spearNodeIter.remove();
	               System.out.println("spear picked");
	               return;
	            }
	   		}
				
	         return;
	   }


	//Update Vertical position of Player according to Height mam-----------------------
	protected void updateVerticalPosition()
	{ 
		SceneNode tessNode = this.getEngine().getSceneManager().getSceneNode("tessN");
		Tessellation tessE = ((Tessellation) tessNode.getAttachedObject("tessE"));
		// Figure out Avatar's position relative to plane
		Vector3 worldAvatarPosition = gameColl.localPlayerNode.getWorldPosition();
		Vector3 localAvatarPosition = gameColl.localPlayerNode.getLocalPosition();
		// use avatar World coordinates to get coordinates for height
		Vector3 newAvatarPosition = Vector3f.createFrom(localAvatarPosition.x(), tessE.getWorldHeight(worldAvatarPosition.x(), worldAvatarPosition.z()), localAvatarPosition.z() );
		// use avatar Local coordinates to set position, including height
		gameColl.localPlayerNode.setLocalPosition(newAvatarPosition);
	}
	
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~// Sound //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
	public void setEarParameters(SceneManager sceneManager)
	{
		SceneNode player = sceneManager.getSceneNode((this.gameColl.localPlayerNode).toString());
		Vector3 avDir = player.getWorldForwardAxis();
		audioMng.getEar().setLocation(player.getWorldPosition());
		audioMng.getEar().setOrientation(avDir, Vector3f.createFrom(0, 1, 0));
	}
	public void initAudio(SceneManager sceneManager)
	{
		AudioResource resource1;
		audioMng = AudioManagerFactory.createAudioManager("ray.audio.joal.JOALAudioManager");
		if (audioMng.initialize())
		{
			System.out.println("Audio Manager has failed to Initialize");
			return;
		}
		resource1 = audioMng.createAudioResource("walking.wav", AudioResourceType.AUDIO_SAMPLE);
		walkingSound = new Sound(resource1, SoundType.SOUND_EFFECT, 100, true);
		walkingSound.initialize(audioMng);
		walkingSound.setMaxDistance(10.0f);
		walkingSound.setMinDistance(0.5f);
		walkingSound.setRollOff(5.0f);
		SceneNode player = sceneManager.getSceneNode((this.gameColl.localPlayerNode).toString());
		walkingSound.setLocation(player.getWorldPosition());
		setEarParameters(sceneManager);
		
		walkingSound.play();
	}
	

//----Getters and Setters---------------------------------
	public Camera getCamera() 
	{
		return myCamera;
	}

	public Vector3 getPlayerPosition()
	{
		return this.gameColl.localPlayerNode.getWorldPosition();
	}

	public void setCamera(Camera camera) 
	{
		this.myCamera = camera;
	}


	public SceneNode getCameraNode() 
	{
		return cameraNode;
	}


	public void setCameraNode(SceneNode cameraNode) 
	{
		this.cameraNode = cameraNode;
	}

	public void setIsConnected(boolean b)
	{
		isConnected = b;
	}
}

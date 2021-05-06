/**
 * @author Hung Truong
 * @author Ezekiel Sung
 * @version 1.0
 * @date 12.18.2020
 * Desert Falls
*/


package a3;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import myGameEngine.*;
import ray.rage.*;
import ray.rage.game.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.scene.*;
import ray.rage.scene.Camera.Frustum.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.rage.rendersystem.states.*;
import ray.rage.asset.texture.*;
import ray.input.*;
import ray.input.action.*;
import ray.rage.util.BufferUtil;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import ray.rage.asset.texture.Texture;
import ray.rage.rendersystem.shader.*;
import java.util.Random;
import java.util.ArrayList;
import net.java.games.input.Controller;
import ray.rage.asset.material.Material;
import ray.rage.rendersystem.states.*;
import ray.rage.asset.texture.*;
import ray.rage.util.*;
import java.awt.geom.*;
import java.util.UUID;
import ray.networking.server.GameConnectionServer;
import ray.networking.server.IClientInfo;
import ray.networking.IGameConnection.ProtocolType;
import ray.networking.*;
import java.net.InetAddress;
import ray.networking.server.UDPClientInfo;
import java.util.*;
import java.net.UnknownHostException;
import ray.input.action.AbstractInputAction;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.Invocable;
import java.util.List;
import ray.physics.PhysicsEngine;
import ray.physics.PhysicsObject;
import ray.physics.PhysicsEngineFactory;
import ray.audio.*;
import com.jogamp.openal.ALFactory;
import static ray.rage.scene.SkeletalEntity.EndType.*;


public class MyGame extends VariableFrameRateGame implements MouseListener, MouseMotionListener {
	
	public SceneNode avatarN,avatar2N,cloudN,camelN,palmN,rockN,fireN,tessN;
	private Camera camera;
	public SceneManager sm;
	private InputManager im;
	private static final String SKYBOX_NAME = "SkyBox";
	private GL4RenderSystem rs;
	float elapsTime = 0.0f;
	private Camera3Pcontroller orbitController1;
	private String serverAddress, sName = "a3/CreateLights.js", avatar_Node = "myAvatarNode", 
			choiceS,displayStr, avatar_Name = "myAvatar";
	private int serverPort,toggleLights=0, elapsTimeSec,ghostCounter=0,palmCounter=1, 
			rockCounter=1,ghostNPC=-1,numBullet=0,startGhost=-3 ,ghostChoice, npcHealth = 2,choice,finalTime,
			bulletCounter=0,numDead=0;
	private ProtocolType serverProtocol;
	private ProtocolClient protClient;
	private DisplaySettingsDialog display;
	private boolean isClientConnected,skyBoxVisible = true, running = false, isRecentering,
					npcCreated= false,bossDead=false,timeStored=false, flag,set=false,
					dead = false,fullScreen=true,created=false;
	private Vector<UUID> gameObjectsToRemove;
	private ScriptEngine jsEngine;
	private ScriptEngine engine;
	protected ColorAction colorAction;
	protected File scriptFile3,scriptFile;
	private SceneNode ball1Node,gndNode,groundNode,ball2Node,bulletN,manNode,alienNode, palmNG,npcN,bossN;
	private final static String GROUND_E = "Ground";
	private final static String GROUND_N = "GroundNode";
	private PhysicsEngine physicsEng;
	private PhysicsObject ball1PhysObj, ball2PhysObj, gndPlaneP,rockPhysObj,avatarPhyObj;
	
	private Entity ball1Entity,ManE, alienE, bossE;
	private int potiID, health, round;
	private Robot robot;
	private float prevMouseX, prevMouseY, curMouseX, curMouseY;
	private RotationController rc;
	private IAudioManager audioMgr;
	private Sound bgSound, camelSound,hitSound,pewSound;
	private SkeletalEntity avatarSE,avatarFE,avatarE;
	private Engine en;
	private SkeletalEntity npcE;
	private int healthArr[] = new int[10];
	private int detatchedArr[] = new int[10];
	
	/** Creates a game with specific server address and port
	 * @param serverAddress The server address the game client wants to connect to
	 * @param sPort The port the  game client wants to use
	*/

    public MyGame(String serverAddr, int sPort) {
        super();
		this.serverAddress = serverAddr;
		this.serverPort = sPort;
		this.serverProtocol = ProtocolType.UDP;
		health = 1000;
		round = 1;


    }

	 public static void main(String[] args) {
		 
		Scanner sc = new Scanner(System.in); 
		System.out.print("Enter Server Address: ");
		String serverIP = sc.nextLine();
		
		 
        Game game = new MyGame(serverIP, Integer.parseInt(args[0]));
        try {
            game.startup();
            game.run();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {

            game.exit();
        }
    }
	//Connecting the client to the server
	private void setupNetworking(){
		gameObjectsToRemove = new Vector<UUID>();
		isClientConnected = false;
		try{
			protClient = new ProtocolClient(InetAddress.
			getByName(serverAddress), serverPort, serverProtocol, this);
		}catch (UnknownHostException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		if (protClient == null){
			System.out.println("missing protocol host");
		}else{

			protClient.sendJoinMessage();
		}
	}


	@Override
	protected void setupWindow(RenderSystem rs, GraphicsEnvironment ge) {
		
		GraphicsDevice gd= ge.getDefaultScreenDevice();
		display = new DisplaySettingsDialog(gd);
		
		display.showIt();
		
		fullScreen = display.isFullScreenModeSelected();
		
		if (fullScreen==false){
			rs.createRenderWindow(new DisplayMode(1000, 700, 24, 60),false);
		} else if (fullScreen==true){
				rs.createRenderWindow(true);
				rs.createRenderWindow(new DisplayMode(1000, 700, 24, 60),true);
			}
		
	


	}
	


	@Override
    protected void setupCameras(SceneManager sm, RenderWindow rw) {

		this.sm = sm;

		SceneNode rootNode = sm.getRootSceneNode();
		Camera camera = sm.createCamera("MainCamera", Projection.PERSPECTIVE);
		rw.getViewport(0).setCamera(camera);
		SceneNode cameraN =
		rootNode.createChildSceneNode("MainCameraNode");
		cameraN.attachObject(camera);
		camera.setRt((Vector3f)Vector3f.createFrom(1.0f, 0.0f, 0.0f));
		camera.setUp((Vector3f)Vector3f.createFrom(0.0f, 1.0f, 0.0f));
		camera.setFd((Vector3f)Vector3f.createFrom(0.0f, 0.0f, -1.0f));

		camera.setMode('n');
		camera.getFrustum().setFarClipDistance(1000.0f);


    }
	
	/** Setting up orbit camera 
	 * @param eng The game engine
	 * @param sm The game's scene manager
	*/
	protected void setupOrbitCamera(Engine eng, SceneManager sm){

		SceneNode avatarN = sm.getSceneNode(avatar_Node);
		SceneNode cameraN = sm.getSceneNode("MainCameraNode");
		Camera camera = sm.getCamera("MainCamera");
		String kbName = im.getKeyboardName();
		orbitController1 = new Camera3Pcontroller(camera, cameraN, avatarN, kbName, im);
	}
	
	/** Setting up the game world objects, object controllers,
			lights, skybox, and javascript.
	 * @param eng The game engine
	 * @param sm The game's scene manager
	*/
	@Override
	protected void setupScene(Engine eng, SceneManager sm) throws IOException {
		 im = new GenericInputManager();
		this.sm=sm;
		sm=this.sm;
		en = eng;
		
		rc = new RotationController(Vector3f.createUnitVectorY(), .02f);

		//creating potion
		ball1Entity = sm.createEntity("ball1", "Poti.obj");
		ball1Node = sm.getRootSceneNode().createChildSceneNode("Ball1Node");
		ball1Node.attachObject(ball1Entity);
		ball1Node.setLocalScale(.007f,.007f,.007f);
		ball1Node.setLocalPosition(-31.540f, 2.5f, -12.79016f);
		
		sm.addController(rc);
		
		//spawning Clash to the character selection screen
		ManE=sm.createEntity ("preMan","man.obj");
		manNode = sm.getRootSceneNode().createChildSceneNode("preManNode");
		manNode.attachObject(ManE);
		manNode.setLocalScale(.05f,.05f,.05f);
		manNode.moveForward(1f);
		manNode.moveLeft(.25f);
		manNode.moveDown(.2f);
		
		//Spawning Chon to the character selection screen
		alienE=sm.createEntity ("preAlien","alien.obj");
		alienNode = sm.getRootSceneNode().createChildSceneNode("preAlienNode");
		alienNode.attachObject(alienE);
		alienNode.setLocalScale(.05f,.05f,.05f);
		alienNode.moveForward(1f);
		alienNode.moveRight(.25f);
		alienNode.moveDown(.2f);
		
		rc.addNode(alienNode);
		rc.addNode(manNode);
		

		
		

		// Ground plane
		Entity groundEntity = sm.createEntity(GROUND_E, "cube.obj");
		groundNode = sm.getRootSceneNode().createChildSceneNode(GROUND_N);
		groundNode.attachObject(groundEntity);
		groundNode.setLocalPosition(10.567f, -.2f, -6.1755f);


		//creating skeletal entity and mesh for Chon
		avatarE=sm.createSkeletalEntity("myAvatar","alien2.rkm","alien2.rks");
		Texture tex = sm.getTextureManager().getAssetByPath("alien.jpeg");
		TextureState tstate = (TextureState)sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		tstate.setTexture(tex);
		avatarE.setRenderState(tstate); 
		
		//creating skeletal entity and mesh for Clash 
		avatarFE=sm.createSkeletalEntity("myAvatarF","try5.rkm","try5.rks");
		tex = sm.getTextureManager().getAssetByPath("man.jpg");
		tstate = (TextureState)sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		tstate.setTexture(tex);
		avatarFE.setRenderState(tstate); 
		avatarFE.setPrimitive(Primitive.TRIANGLES);
		setupNetworking();

		


		// set up light
        sm.getAmbientLight().setIntensity(new Color(.05f, .05f, .05f));
        Light plight = sm.createLight("testLamp1", Light.Type.POINT);
        plight.setAmbient(new Color(.05f, .05f, .05f));
        plight.setDiffuse(new Color(0.3f, 0.3f, 0.3f));
        plight.setSpecular(new Color(0.50f, 0.50f, 0.50f));
        plight.setRange(10f);
        SceneNode plightNode =
        sm.getRootSceneNode().createChildSceneNode("plightNode");
        plightNode.attachObject(plight);
        plightNode.setLocalPosition(1.0f, 1.0f, 5.0f);

		//set up a second light
		Light plight2 = sm.createLight("testLamp3", Light.Type.POINT);
		plight2.setAmbient(new Color(.0f, .0f, .0f));
		plight2.setDiffuse(new Color(.0f, .0f, .0f));
		plight2.setSpecular(new Color(0.0f, 0.0f, 0.0f));
		plight2.setRange(5);
        SceneNode plightNode2 =
        sm.getRootSceneNode().createChildSceneNode("plightNode2");
        plightNode2.attachObject(plight2);

		// set up sky box
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

		//set up light script
		ScriptEngineManager factory = new ScriptEngineManager();
		java.util.List<ScriptEngineFactory> list = factory.getEngineFactories();
		jsEngine = factory.getEngineByName("js");
		scriptFile3 = new File("UpdateLightColor.js");
		this.runScript(scriptFile3, jsEngine);

		initPhysicsSystem();
		createRagePhysicsWorld();
		initAudio(sm);
		addLightsWithScripts();
	}

	
	/** Setting game audios; SFX, BG sound
	 * @param sm The game's scene manager
	*/
	public void initAudio(SceneManager sm){ 
		AudioResource resource1, resource2,resource3,resource4;
		audioMgr = AudioManagerFactory.createAudioManager("ray.audio.joal.JOALAudioManager");
		if (!audioMgr.initialize()){ 
			System.out.println("Audio Manager failed to initialize!");
			return;
		}
		resource1 = audioMgr.createAudioResource("camel3.wav",AudioResourceType.AUDIO_SAMPLE);
		resource2 = audioMgr.createAudioResource("bg4.wav",AudioResourceType.AUDIO_SAMPLE);
		resource3 = audioMgr.createAudioResource("hit.wav",AudioResourceType.AUDIO_SAMPLE);
		resource4 = audioMgr.createAudioResource("pew.wav",AudioResourceType.AUDIO_SAMPLE);
		pewSound = new Sound(resource4, SoundType.SOUND_EFFECT, 20, true);
		hitSound = new Sound(resource3, SoundType.SOUND_EFFECT, 20, true);
		bgSound = new Sound(resource2, SoundType.SOUND_EFFECT, 20, true);
		bgSound.initialize(audioMgr);
		pewSound.initialize(audioMgr);
		hitSound.initialize(audioMgr);
		bgSound.play();
		
		
		camelSound = new Sound(resource1, SoundType.SOUND_EFFECT, 80, true);
		camelSound.initialize(audioMgr);
		camelSound.setMaxDistance(5.0f);
		camelSound.setMinDistance(0.5f);
		camelSound.setRollOff(5f);



	}

	/** Setting ear parameters for 3D sound
	 * @param sm The game's scene manager
	*/
	public void setEarParameters(SceneManager sm){ 
		SceneNode dolphinNode = sm.getSceneNode(avatar_Node);
		Vector3 avDir = dolphinNode.getWorldForwardAxis();
		// note - should get the camera's forward direction
		// - avatar direction plus azimuth
		audioMgr.getEar().setLocation(dolphinNode.getWorldPosition());
		audioMgr.getEar().setOrientation(avDir, Vector3f.createFrom(0,1,0));
}
	
	/** Method to play the game avatar's bow animation **/
	private void doBow(){
		avatarSE = (SkeletalEntity)getEngine().getSceneManager().getEntity(avatar_Name);
		avatarSE.stopAnimation();
		avatarSE.playAnimation("bowAnimation", 1f, STOP, 0);
	}
	/** Method to play the game avatar's walking animation **/
	private void doAlienWalk() {
		avatarSE = (SkeletalEntity)getEngine().getSceneManager().getEntity(avatar_Name);
		avatarSE.stopAnimation();
		avatarSE.playAnimation("alienWalkAnimation", 1f, LOOP, 0);
	}
	/** Method to stop the game avatar's walking animation **/
	private void stopAlienWalk() {
		avatarSE.pauseAnimation();
	}
	

	/** Running the script using the .js file
	 * @param scriptFile3 The javascript file name
	 * @param eng The game engine
	*/ 
	private void runScript(File scriptFile3, ScriptEngine engine){
		try{
			FileReader fileReader = new FileReader(scriptFile3);
			engine.eval(fileReader);
			fileReader.close();
		}catch (FileNotFoundException e1){
			System.out.println(scriptFile3 + " not found " + e1);
		}catch (IOException e2){
			System.out.println("IO problem with " + scriptFile3 + e2);
		}catch (ScriptException e3){
			System.out.println("Script Exception in " + scriptFile3 + e3);
		}catch (NullPointerException e4){
			System.out.println ("Null ptr exception reading " + scriptFile3 + e4);
		}
	}

	/** Creating the monsters in the first three rooms
	 * @param eng The game engine
	 * @param sm The game's scene manager
	*/
	public void createCamel(Engine eng, SceneManager sm) throws IOException{
		Entity camelE=sm.createEntity("myCamel","dragon3.obj");
		 camelE.setPrimitive(Primitive.TRIANGLES);
		 camelN= sm.getRootSceneNode().createChildSceneNode(camelE.getName()+"Node");
		 camelN.attachObject(camelE);
		 camelN.setLocalScale(.6f,.6f,.6f);
		 camelN.moveForward(5.0f);
		 camelN.moveUp(0.5f);
	}
	
	/** Choice 1: Player chose "Chon" as their game avatar **/
	public void charSelectTron()throws IOException{
		avatarN= sm.getRootSceneNode().createChildSceneNode(avatarE.getName()+"Node");
		avatarN.attachObject(avatarE);
		setupOrbitCamera(en,sm);
		set=true;

		avatarE.loadAnimation("bowAnimation", "bow.rka");
		avatarE.loadAnimation("alienWalkAnimation","alienWalk.rka");
		avatarN.yaw(Degreef.createFrom(45.f));
		avatarN.setLocalScale(.1f,.1f,.1f);
		avatarN.setLocalPosition(-6.99f,0.0f,-3.18f);
		setupInputs(sm);
		setEarParameters(sm);
		
		
		choice=1;
		choiceS="1";
		ManE.setVisible(false);
		alienE.setVisible(false);
		createWorld();

	}
	
	/** Choice 2: Player chose "Clash" as their game avatar **/
	public void charSelectFlash()throws IOException{
		
		avatarN=sm.getRootSceneNode().createChildSceneNode(avatarFE.getName()+"Node");
		avatarN.setLocalScale(.1f,.1f,.1f);
		 avatarN.setLocalPosition(-6.99f,0.0f,-3.18f);
		avatarN.attachObject(avatarFE);
		
		avatar_Node = "myAvatarFNode";
		avatar_Name = "myAvatarF";
		setupOrbitCamera(en,sm);
		set=true;
		
		avatarN.yaw(Degreef.createFrom(45.f));
		avatarFE = (SkeletalEntity)getEngine().getSceneManager().getEntity(avatar_Name);
		avatarFE.loadAnimation("bowAnimation", "try5.rka");
		avatarFE.loadAnimation("alienWalkAnimation","walkingtry5.rka");
		setEarParameters(sm);
		setupInputs(sm);
		
		
		ManE.setVisible(false);
		alienE.setVisible(false);
		choice=2;
		choiceS="2";
		createWorld();
		
	}
	
	/** Once the player chooses an avatar, create game platforms
			and display objects
	*/
	public void createWorld()throws IOException{
		Tessellation tessE = sm.createTessellation("tessE", 6);
		tessE.setSubdivisions(8f);
		tessN =sm.getRootSceneNode().createChildSceneNode("TessN");
		tessN.attachObject(tessE);
		tessN.scale(100, 200, 100);
		tessE.setHeightMap(this.getEngine(), "ter.jpg");
		tessE.setTexture(this.getEngine(), "bottom.jpg");
		
		createCloud(en,sm);

		palmNG = sm.getRootSceneNode().createChildSceneNode("myPalmNodeG");
		
		createPalm(en,sm,palmCounter,23.626f,0f,-20.8080f);
		createPalm(en,sm,palmCounter,31.027f,0f,29.1725f);
		createPalm(en,sm,palmCounter,-21.418f,0f,24.124617f);
		createPalm(en,sm,palmCounter,-42.53333f,0f,-19.7155f);
			 
		
			 

		  
		 protClient.sendCreateMessage(avatarN.getLocalPosition(),choiceS);
		 
		 created=true;
	}
	
	/** Creating palm trees and add to game world
	 * @param eng The game engine
	 * @param sm The game's scene manager
	*/
	public void createPalm(Engine eng,SceneManager sm, int num,float x, float y, float z) throws IOException{
		Entity palmE=sm.createEntity("myPalm"+num,"palm.obj");
		palmE.setPrimitive(Primitive.TRIANGLES);
		palmN= palmNG.createChildSceneNode(palmE.getName()+"Node");
		palmN.attachObject(palmE);
		palmN.setLocalScale(.1f,.1f,.1f);
		palmN.setLocalPosition(x,y,z);
		palmCounter++;
	}

	/** Creating bullets and spawn it where ever the player is
	 * @param num The number of the bullet, increment each time
	*/
	public void createBullet(int num) throws IOException{
		Entity bulletE=sm.createEntity("myBullet"+num,"sphere.obj");
		bulletE.setPrimitive(Primitive.TRIANGLES);
		bulletN= sm.getRootSceneNode().createChildSceneNode(bulletE.getName()+"Node");
		bulletN.attachObject(bulletE);
		bulletN.setLocalScale(.09f,.09f,.09f);
		SceneNode avatarN = getEngine().getSceneManager().getSceneNode(avatar_Node);
		bulletN.setLocalPosition(avatarN.getLocalPosition());

		SceneNode cameraN = sm.getSceneNode("MainCameraNode");
	
		bulletN.setLocalRotation(avatarN.getWorldRotation());

		bulletN.moveUp(.50f);

		pewSound.play(50,false);
		numBullet++;
	}
	
	/** Creating the NPC that can be interacted with for health regeneration
	 * @param eng The game engine
	 * @param sm The game's scene manager
	*/
	public void createCloud(Engine eng, SceneManager sm)throws IOException{
		Entity cloudE=sm.createEntity("myCloud","avatar3.obj");
		cloudE.setPrimitive(Primitive.TRIANGLES);
		cloudN= sm.getRootSceneNode().createChildSceneNode(cloudE.getName()+"Node");
		cloudN.attachObject(cloudE);
		cloudN.setLocalScale(.3f,.3f,.3f);
		cloudN.lookAt(-25.f,0.0f,-11.89f);

		cloudN.setLocalPosition(-32f, 0.0f, -11.1755f);


	}

	/** Initializing the physics engine system **/
	private void initPhysicsSystem(){
		String engine = "ray.physics.JBullet.JBulletPhysicsEngine";
		float[] gravity = {0, -3f, 0};
		physicsEng = PhysicsEngineFactory.createPhysicsEngine(engine);
		physicsEng.initSystem();
		physicsEng.setGravity(gravity);
	}
	
	/** Creating the physics world **/
	private void createRagePhysicsWorld(){
		float mass = 1.0f;
		float up[] = {0,1,0};
		double[] temptf;
		float[] tempsize = {7f,3.5f,3f};
		float[] avatarSize={1f,2.5f,1.0f};
		
		//set up potion as a physic object
		temptf = toDoubleArray(ball1Node.getLocalTransform().toFloatArray());
		potiID = physicsEng.nextUID();
		ball1PhysObj = physicsEng.addSphereObject(potiID, mass, temptf, .6f);

		
		ball1PhysObj.setBounciness(0f);
		ball1Node.setPhysicsObject(ball1PhysObj);



		temptf = toDoubleArray(groundNode.getLocalTransform().toFloatArray());
		gndPlaneP = physicsEng.addStaticPlaneObject(physicsEng.nextUID(),
		temptf, up, 0.0f);

		gndPlaneP.setBounciness(1.0f);
		groundNode.scale(3f, .05f, 3f);
		groundNode.setLocalPosition(10.567f, -.2f, -6.1755f);
		groundNode.setPhysicsObject(gndPlaneP);
	}

	/** Imports blue light js file and turn it on if called**/
	public class ColorAction extends AbstractInputAction{
		private SceneManager sm;
		private ScriptEngine jsEngine;
		private File scriptFile3;
		public ColorAction(SceneManager s, ScriptEngine j, File file) {
			sm = s;
			jsEngine = j;
			scriptFile3 = file;

		} // constructor

		public void performAction(float time, net.java.games.input.Event e){
			//cast the engine so it supports invoking functions
			Invocable invocableEngine = (Invocable) jsEngine ;
			//get the light to be updated
			Light lgt = sm.getLight("testLamp1");
			// invoke the script function
			try{
				invocableEngine.invokeFunction("updateAmbientColor", lgt);
			}catch (ScriptException e1){
				System.out.println("ScriptException in " + scriptFile3 + e1);
			}catch (NoSuchMethodException e2){
				System.out.println("No such method in " + scriptFile3 + e2);
			}catch (NullPointerException e3){
				System.out.println ("Null ptr exception reading " + scriptFile3 + e3);
			}
		}
	}

	/** Setting up keyboard and game pad inputs 
	 *@ param sm The game's scene manager
	*/
	protected void setupInputs(SceneManager sm){

		SceneNode avatarN = getEngine().
			getSceneManager().getSceneNode(avatar_Node);
		String kbName = im.getKeyboardName();
		String gpName = im.getFirstGamepadName();

		MoveForwardAction moveForwardAction = new MoveForwardAction(avatarN,this,protClient);
		
		MoveBackwardAction moveBackwardAction = new MoveBackwardAction(avatarN,this,protClient);
		
		MoveLeftAction moveLeftAction = new MoveLeftAction(avatarN,this,protClient);
		
		MoveRightAction moveRightAction= new MoveRightAction(avatarN,this,protClient);
		
		LookUpAction lookUpAction = new LookUpAction(orbitController1);
		
		LookDownAction lookDownAction = new LookDownAction(orbitController1);
		
		OrbitLeftAction orbitLeftAction = new OrbitLeftAction(orbitController1);
		
		OrbitRightAction orbitRightAction = new OrbitRightAction(orbitController1);
	
		MoveYawLeftAction yawLeft = new MoveYawLeftAction(avatarN,orbitController1);
		
		RotateLeftAction rotateLeftAction = new RotateLeftAction(avatarN, orbitController1,protClient);
		
		RotateRightAction rotateRightAction = new RotateRightAction(avatarN, orbitController1,protClient);
		
		QuitGameAction quitGameAction = new QuitGameAction(this);
		
		colorAction = new ColorAction(sm, jsEngine, scriptFile3);
		
		OrbitAroundAction orbitAroundAction = new OrbitAroundAction(orbitController1);
		
		OrbitElevationAction orbitElevationAction = new OrbitElevationAction(orbitController1);
		
		XStickAction xStickAction = new XStickAction(avatarN, this, protClient);
		
		YStickAction yStickAction = new YStickAction(avatarN, this, protClient);
		
		RXStickAction rxStickAction = new RXStickAction(avatarN,orbitController1 );
		
		ShootAction shootAction = new ShootAction(this);
		
		InteractAction interactAction = new InteractAction(this);
		
		PickUpAction pickUpAction = new PickUpAction(this);



		ArrayList controllers = im.getControllers();
		for (int i =0;i<controllers.size();i++){
			Controller c = (Controller)controllers.get(i);
			if (c.getType()==Controller.Type.MOUSE){
			}
			//setting up keyboard actions
			if (c.getType()==Controller.Type.KEYBOARD){

				im.associateAction(c,net.java.games.input.Component.Identifier.Key.ESCAPE,
				quitGameAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
				
				im.associateAction(c,net.java.games.input.Component.Identifier.Key.W,
				moveForwardAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				im.associateAction(c,net.java.games.input.Component.Identifier.Key.A,
				moveLeftAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				im.associateAction(c,net.java.games.input.Component.Identifier.Key.S,
				moveBackwardAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				im.associateAction(c,net.java.games.input.Component.Identifier.Key.D,
				moveRightAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

				im.associateAction(c,net.java.games.input.Component.Identifier.Key.UP,
				lookDownAction,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				im.associateAction(c,net.java.games.input.Component.Identifier.Key.DOWN,
				lookUpAction,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);


				im.associateAction(c,net.java.games.input.Component.Identifier.Key.LEFT,
				rotateLeftAction,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				im.associateAction(c,net.java.games.input.Component.Identifier.Key.RIGHT,
				rotateRightAction,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

				im.associateAction(c,net.java.games.input.Component.Identifier.Key.Q,
				orbitRightAction,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);	
				
				im.associateAction(c, net.java.games.input.Component.Identifier.Key.F,	
				interactAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
				
				im.associateAction(c, net.java.games.input.Component.Identifier.Key.SPACE,
				shootAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
						
				
			
			}
			//setting up game pad button actions
			else if ((c.getType() == Controller.Type.GAMEPAD || c.getType() == Controller.Type.STICK)){ 
					
				im.associateAction( c, net.java.games.input.Component.Identifier.Axis.RX,
				rxStickAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				im.associateAction( c, net.java.games.input.Component.Identifier.Axis.RY,
				orbitElevationAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				im.associateAction( c, net.java.games.input.Component.Identifier.Axis.X,
				xStickAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				im.associateAction( c, net.java.games.input.Component.Identifier.Axis.Y,
				yStickAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				im.associateAction(c, net.java.games.input.Component.Identifier.Button._5,
				shootAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
				
				im.associateAction(c, net.java.games.input.Component.Identifier.Button._0,
				interactAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
				
				im.associateAction(c, net.java.games.input.Component.Identifier.Button._2,
				pickUpAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
					
			}   
		}
	}

	
	/** Updating game world 
	 *@ param engine The game engine 
	*/
	@Override
    protected void update(Engine engine) {
		Engine en= engine;
		// build and set HUD
		rs = (GL4RenderSystem) engine.getRenderSystem();
		elapsTime += engine.getElapsedTimeMillis();
		elapsTimeSec = Math.round(elapsTime/1000.0f);
		
		//character selection screen displays only before 
		//is chosen.
		if (set==false){
		rs.setHUD("Press 1: Chon",rs.getCanvas().getWidth()/5 ,rs.getCanvas().getHeight()/3 - 100);
		rs.setHUD2("Press 2: Clash",rs.getCanvas().getWidth() - rs.getCanvas().getWidth()/3,rs.getCanvas().getHeight()/3 - 100);
		} 
		//setting up game HUD, displaying player's health
		else if (!bossDead) {
			rs.setHUD("Health: "+health,15, 15);
			rs.setHUD2("",0,0);
		} 
		//if boss is dead, display completion time
		else if (bossDead){
			rs.setHUD("Time completed: "+finalTime/60+":"+finalTime%60);
		}
	
		im.update(elapsTime);
		
		//updating orbit camera controller, only if the game has started
		if (set==true){
			orbitController1.updateCameraPosition();
		}
		processNetworking(elapsTime);
		
		//controlling how far the bullet and move
		if(flag && bulletCounter < 50 ) {
			SceneNode bulletN = getEngine().getSceneManager().getSceneNode("myBullet"+(numBullet-1)+"Node");
			bulletN.moveForward(2.0f);
			bulletCounter++;
			
			//if the NPCs has been created and not dead, detect collision
			if (npcCreated==true){
				for(int i = startGhost; i <= ghostNPC; i++) {
					npcN = sm.getSceneNode("myNPC"+i+"Node");
					if (i!=9){
						npcE = (SkeletalEntity)engine.getSceneManager().getEntity("myNPC"+i);
					}
					
					//collision detected between bullet and NPCs
					if(getDistance(bulletN, npcN) < 1) {
						
						//decrease health if bullet hit NPC
						if (healthArr[i]>0){
							hitSound.play(50,false);
							healthArr[i]--;
						}
						
						//remove NPC health is 0 = dead
						if(healthArr[i] == 0 && detatchedArr[i]==0) {
							numDead++;
							if(i!=9) {
								npcN.detachObject(npcE);
							}else{
								npcN.detachObject(bossE);
							}
							
							//removing NPC and sound
							detatchedArr[i]=1;
							camelSound.stop();
							if (numDead==3){
							npcCreated = false;
							}
						}
					}
				}
			}
		}
		
		//if bullet moved pass the limit, remove it
		if (bulletCounter>=50){
			SceneNode bulletN = getEngine().getSceneManager().getSceneNode("myBullet"+(numBullet-1)+"Node");
			bulletN.detachAllObjects();
			flag=false;
			bulletCounter=0;
		}
		
		//detecting collision between player and NPCs
		if(npcCreated) {
			for(int i = startGhost; i <= ghostNPC; i++){
				npcN = sm.getSceneNode("myNPC"+i+"Node");
				
				//if NPC collided with player
				if(getDistancePoti(avatarN,npcN) < 0.5 && health>0) {
					//decrease player health if its greather than 0
					if (healthArr[i]>0){
					health--;
					}
					//mark the player as dead if health is 0
					if(health <1) {
						dead = true;
						camelSound.stop();
						
					}
				
				}
			}
			
		}
		float time = engine.getElapsedTimeMillis();
		if (running){
			Matrix4 mat;
			physicsEng.update(time);
			
			//applying physic to all physic objects
			for (SceneNode s : engine.getSceneManager().getSceneNodes()){
				if (s.getPhysicsObject() != null && !s.getName().equals(avatar_Node) /*&& !s.getName().equals("myRockNode")*/){
					mat = Matrix4f.createFrom(toFloatArray(s.getPhysicsObject().getTransform()));
					s.setLocalPosition(mat.value(0,3),mat.value(1,3),mat.value(2,3));	
				}
				if (s.getPhysicsObject()!= null && s.getName().equals(avatar_Node)){
					Matrix4 avatarLoc=s.getLocalTransform();
					float[] temp = avatarLoc.toFloatArray();
					double[] locArr = toDoubleArray(temp);
					avatarPhyObj.setTransform(locArr);
				}
			}
		}
		
		//checking to see if all monsters and boss are killed,
		//mark completed if finished
		if ( numDead==10 &&!bossDead){
			bossDead=true;
			finalTime= elapsTimeSec;
		}
		//update animation
		SkeletalEntity avatarSE = (SkeletalEntity)engine.getSceneManager().getEntity(avatar_Name);
		if(created){
			pickUp();
		}
		//if player chose Chon, update Chon's animations
		if (choice==1){
			avatarSE.update();	
		}
		//if player chose Clash, update Clash's animations
		else if (choice==2){	
			avatarFE.update();
		}
		

		try{
			//update NPCs' animations
			for (int i=startGhost;i<ghostCounter;i++){
				SkeletalEntity npcSE = (SkeletalEntity)engine.getSceneManager().getEntity("myNPC"+i);
				npcSE.update();
			}
		}catch(Exception ee) {
			System.out.println("error updating NPCs' animations");
		}
		
		//setting sound for monster sound
		if (npcN!=null){
			SceneManager sm = engine.getSceneManager();
			SceneNode robotN = sm.getSceneNode("myNPC0Node");
			camelSound.setLocation(robotN.getWorldPosition());	
		}
		
		if (set==true){	
			setEarParameters(sm);
		}
		
		//detecting where player is vs the palm trees to calculate 
		//which round the player is currently in
		if (created){
			SceneNode tree1 = sm.getSceneNode("myPalm1Node");
			SceneNode tree2 = sm.getSceneNode("myPalm2Node");
			SceneNode tree3 = sm.getSceneNode("myPalm3Node");
			SceneNode tree4 = sm.getSceneNode("myPalm4Node");
		
			if (created && round==1 && (getDistance(avatarN,tree1) <4 ) ){
				incrementRound();
			} else if (created && round==2 && (getDistance(avatarN,tree2) <4 )){
				incrementRound();
			} else if (created && round==3 &&(getDistance(avatarN,tree3) <4 )){
				incrementRound();
			} else if (created && round==4 &&(getDistance(avatarN,tree4) <4)){
				incrementRound();
			}
		}

	}

	/** Geting the start ghost number
	 *@ return startGhost
	*/
	public int getStartGhost(){
		return startGhost;
	}

	/** Finding out if the player is dead or not
	 *@ return dead
	*/
	public boolean getDead() {
		return dead;
	}

	/** Converting double array to float array
	 *@ param arr Double array to be converted
	*/
	private float[] toFloatArray(double[] arr){
		if (arr == null) return null;
		int n = arr.length;
		float[] ret = new float[n];
		for (int i = 0; i < n; i++){
			ret[i] = (float)arr[i];
		}
		return ret;
		}

	/** Converting float array to double
	 *@ param arr Float array to be converted
	*/
	private double[] toDoubleArray(float[] arr){
		if (arr == null) return null;
		int n = arr.length;
		double[] ret = new double[n];

		for (int i = 0; i < n; i++){
			ret[i] = (double)arr[i];
		}
		return ret;
	}
	
	/** Processing packets received by the client from the server
	 *@ param elapsTime The game running time
	*/
	protected void processNetworking(float elapsTime){
		// Process packets received by the client from the server
		if (protClient != null)
			protClient.processPackets();

	}
	
	/** Getting the player's position
	 *@ return avatarN local position
	*/
	public Vector3 getPlayerPosition(){
		return avatarN.getWorldPosition();
	}
	
	/** Adding ghost avatar to game world when another player join
	 * @param avatar The other player's avatar
	 * @param position The other player's position
	 * @param choice The other player's choice of avatar
	*/
	public void addGhostAvatarToGameWorld(GhostAvatar avatar, Vector3 position,String choice) throws IOException{
		if (avatar != null){
			//if the player's choice is 1,
			//use Chon model for ghost avatar
			if (choice.equals("1")){
				Entity avatar2E=sm.createEntity("myAvatar"+ghostCounter,"alien.obj");
				avatar2E.setPrimitive(Primitive.TRIANGLES);
				avatar2N= sm.getRootSceneNode().createChildSceneNode(avatar2E.getName()+"Node");
				avatar2N.attachObject(avatar2E);
				avatar2N.setLocalScale(.1f,.1f,.1f);
				avatar2N.setLocalPosition(position);
				ghostCounter++;
				ghostChoice = 1;
			}
			
			//if the player's choice is 2,
			//use Clash model for ghost avatar
			if (choice.equals("2")){
				Entity avatar2E=sm.createEntity("myAvatar"+ghostCounter,"man.obj");
				avatar2E.setPrimitive(Primitive.TRIANGLES);
				avatar2N= sm.getRootSceneNode().createChildSceneNode(avatar2E.getName()+"Node");
				avatar2N.attachObject(avatar2E);
				avatar2N.setLocalScale(.1f,.1f,.1f);
				avatar2N.setLocalPosition(position);
				ghostCounter++;
				ghostChoice = 2;
			}
		}
	}
	
	/** Getting the player's choice of avatar
	 *@ return String choiceS
	*/
	public String getChoiceString(){
		return choiceS;
	}

	/** Removing the ghost avatar of the other player
	 *		that quits the game
	 *@ param avatar The other player's ghost avatar 
	*/
	public void removeGhostAvatarFromGameWorld(GhostAvatar avatar){
		if(avatar != null) {
			gameObjectsToRemove.add(avatar.getID());
				Entity avatar2E = sm.getEntity("myAvatar" + (ghostCounter-1));
				avatar2N.detachObject(avatar2E);
				avatar2E.setVisible(false);
			
		}
	}
	

	private abstract class SendCloseConnectionPacketAction extends AbstractInputAction {
		// for leaving the game... need to attach to an input device
		//@Override
		public void performAction(float time, Event evt){
			if(protClient != null && isClientConnected == true){
				protClient.sendByeMessage();
			}
		}
	}
	
	/** Adding lights to game world with script **/
	public void addLightsWithScripts(){
		ScriptEngineManager factory = new ScriptEngineManager();
		List<ScriptEngineFactory> list = factory.getEngineFactories();
		scriptFile = new File(sName);
		jsEngine.put("toggleLights",toggleLights);
		jsEngine.put("sm", sm);
		this.runScript(scriptFile,jsEngine);
		if (toggleLights==0){
			toggleLights=1;
		} else {
			  toggleLights=0;
		}


	}
	
	/** Kill the lights in game world with script **/
	public void killLightsWithScripts(){
		ScriptEngineManager factory = new ScriptEngineManager();
		List<ScriptEngineFactory> list = factory.getEngineFactories();
		scriptFile = new File("a3/KillLights.js");
		jsEngine.put("sm", sm);
		jsEngine.put("sm", sm);
		this.runScript(scriptFile,jsEngine);
	}


	/** Setting up key inputs 
	 *@ param e The key that the player pressed
	*/
	@Override
    public void keyPressed(KeyEvent e) {

       switch (e.getKeyCode()) {
			//Pressing '1' in character selection screen
			//will Start Chon in the game
			case KeyEvent.VK_1:
				try{
					if (set==false){
						charSelectTron();
						choiceS = "1";
					}	
					}catch(Exception eu){}
				break;
				
			//Pressing '2' in character selection screen
			//will Start Clash in the game
			case KeyEvent.VK_2:
				try{
					if (set==false){
						charSelectFlash();
						choiceS = "2";
					}		
				}catch(Exception eu){}
				break;
				
			//pressing 'L' will toggle the light
		    case KeyEvent.VK_L:
				addLightsWithScripts();
				break;
				
			//pressing 'B' will play bow animation
			case KeyEvent.VK_B:
				doBow();
				break;
				
			//when player walk with 'W', play walking animation
			case KeyEvent.VK_W:
				if (dead==false){
					doAlienWalk();
				}
				break;
			
			//when player press 'F', interact with NPC
			case KeyEvent.VK_F:
				interact();
				break;

        }
        super.keyPressed(e);
    }
	
	
	/** Setting up action when a a key is released
	 *@ param e The key that the player released
	*/
	@Override
	public void keyReleased(KeyEvent e){
		switch(e.getKeyCode()){
			case KeyEvent.VK_W:
				stopAlienWalk();
				break;
		}
		super.keyReleased(e);
		
	}
	
	/** Getting the distance between two nodes
	 *@ param node1 The node 1 of 2 that is needed for checking distance
	 *@ param node2 The node 2 of 2 that is needed for checking distance
	 *@return distance between the 2 nodes as a float
	*/
	public float getDistance(SceneNode node1, SceneNode node2){
		//getting and storing the position of node 1
		float node1X = node1.getLocalPosition().x();
		float node1Y = node1.getLocalPosition().y();
		float node1Z = node1.getLocalPosition().z();
		
		//getting and storing the position of node 2
		float node2X = node2.getLocalPosition().x();
		float node2Y = node2.getLocalPosition().y();
		float node2Z = node2.getLocalPosition().z();
		return (float) (Math.sqrt((double)+(node1X-node2X)*(node1X-node2X)+(node1Y-node2Y)*(node1Y-node2Y)+(node1Z-node2Z)*(node1Z-node2Z)));
	}
	
	/** getting the distance between the potion and player
	 *@ param node1 Getting the player's position
	 *@ param node2 Getting the potion's position 
	 *		when it is on the ground
	*/
	public float getDistancePoti(SceneNode node1, SceneNode node2){
		//getting and storing position of player
		float node1X = node1.getLocalPosition().x();
		float node1Y = node1.getLocalPosition().y();
		float node1Z = node1.getLocalPosition().z();
		
		//getting and storing position of potion
		float node2X = node2.getLocalPosition().x();
		float node2Y = 0f;
		float node2Z = node2.getLocalPosition().z();
		return (float) (Math.sqrt((double)+(node1X-node2X)*(node1X-node2X)+(node1Y-node2Y)*(node1Y-node2Y)+(node1Z-node2Z)*(node1Z-node2Z)));
		
	}

	/** Updating the vertical position of the player, making
	 *		it look like the player is walking up the terrain
	*/
	public void updateVerticalPosition(){
		SceneNode avatarN =this.getEngine().getSceneManager().
		getSceneNode(avatar_Node);
		SceneNode tessN =this.getEngine().getSceneManager().
		getSceneNode("TessN");
		Tessellation tessE = ((Tessellation) tessN.getAttachedObject("tessE"));

		// Figure out Avatar's position relative to plane
		Vector3 worldAvatarPosition = avatarN.getWorldPosition();
		Vector3 localAvatarPosition = avatarN.getLocalPosition();
		// use avatar World coordinates to get coordinates for height
		Vector3 newAvatarPosition = Vector3f.createFrom(localAvatarPosition.x(),tessE.getWorldHeight(worldAvatarPosition.x(),worldAvatarPosition.z()),localAvatarPosition.z());
		// use avatar Local coordinates to set position, including height
		avatarN.setLocalPosition(newAvatarPosition);
		
	}
	
	/** Setting the player's connection to game server 
	 *@ param bool Connection boolean
	*/
	public void setIsConnected(boolean bool) {
		isClientConnected = bool;
	}
	
	/** Getting the game round
	 *@ return round
	*/
	public int getRound() {
		return round;
	}
	
	/** Adding ghost NPC avatar to game world
	 *@ param npc The Ghost NPC 
	*/
	public void addGhostNPCtoGameWorld(GhostNPC npc) throws IOException {
		Vector3 position = npc.getPosition();
		ghostNPC++;
		
		//Setting up entity and node for the ghost NPC
		npcE=sm.createSkeletalEntity("myNPC"+ghostNPC,"dragon3.rkm","dragon3.rks");
		Texture tex = sm.getTextureManager().getAssetByPath("dragon.jpg");
		TextureState tstate = (TextureState)sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		tstate.setTexture(tex);
		npcE.setRenderState(tstate); 
		npcE.setPrimitive(Primitive.TRIANGLES);
		npcN= sm.getRootSceneNode().createChildSceneNode(npcE.getName()+"Node");
		npcN.attachObject(npcE);
		npcN.setLocalScale(.3f,.3f,.3f);
		npcN.setLocalPosition(position);
		npcN.lookAt(avatarN);
		
		//loading NPC's animation
		npcE.loadAnimation("npcWalk","dragon3walking.rka");	
		npcE.playAnimation("npcWalk", 1f, LOOP, 0);
	
		//setting sound for NPC		
		SceneNode robotN = sm.getSceneNode("myNPC"+ghostNPC+"Node");
		camelSound.setLocation(robotN.getWorldPosition());
		setEarParameters(sm);
		camelSound.play();
		
		npcCreated = true;
		//setting health of NPC depending on what round,
		//	to increase game difficulty 
		healthArr[ghostNPC] = 2*round;
		detatchedArr[ghostNPC] = 0;
		
	}
	
	/** Adding boss NPC avatar to game world
	 *@ param npc The Ghost NPC 
	*/
	public void addBossNPCtoGameWorld(GhostNPC npc)throws IOException{
			Vector3 position = npc.getPosition();
			ghostNPC++;
			bossE=sm.createEntity("myNPC"+ghostNPC,"boss.obj");
			bossE.setPrimitive(Primitive.TRIANGLES);
			bossN= sm.getRootSceneNode().createChildSceneNode(bossE.getName()+"Node");
			bossN.attachObject(bossE);
			bossN.setLocalPosition(position);
			healthArr[ghostNPC]=10;
		
		
	}
	
	/** Getting the ghost NPC number
	 *@ return ghostNPC The number assigned to ghost NPC
	*/
	public int getGhostNum(){
		return ghostNPC;
	}
	
	/** Checking if the set has been set or not
	 *@ return set boolean
	*/
	public boolean getSet(){
		return set;
	}
	
	/** Checking to see if NPC were created or not
	 *@ return npcCreated boolean
	*/
	public boolean getNpcCreated(){
		return npcCreated;
	}
	
	/** Creates a bullet */
	public void shoot(){
		try{
			if (!flag){
				createBullet(numBullet);
				flag = true;
			}
		}catch(Exception eu) {
			
		}
		
	}
	
	/** Interact with NPC when called, drop potion
	 *  	 if player close enough to NPC
	*/
	public void interact(){

		//checking to see if the player is close enough to NPC
		if(getDistancePoti(avatarN,ball1Node)<8){
			running=true;
			rc.addNode(ball1Node);
			doBow();	

		}
	}	

	/** Increment round when called, and tell server
	 *		to spawn more monsters
	*/
	private void incrementRound(){
		startGhost=startGhost+3;
		round++;
		protClient.sendCreateNPCMessage();
	}
	
	/** Pick up potion when called **/
	public void pickUp(){
		// If player is close enough to pick up
		if (getDistance(ball1Node,avatarN)<1){
			//pick up = remove potion and increase health to max
			try{
				physicsEng.removeObject(potiID); 
				ball1Node.detachObject(ball1Entity);
				health = 100;
			}catch(Exception eu){}		
		} 		
	}
	/** Quit game when called, and let the server know
	 * 		that the player has exitted the game
	*/
	public void quitGame(){
		protClient.sendByeMessage();
		this.exit();
		
	}
}

package myGameEngine;

import a3.MyGame;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import ray.rage.scene.Camera;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;
import ray.rml.Vector3f;
import ray.input.InputManager;
import ray.input.action.Action;

public class Camera3Pcontroller {
	
	private Camera camera;
	private SceneNode cameraN;
	private SceneNode target;
	public float cameraAzimuth;
	public float cameraElevation;
	public float radius;
	private Vector3 targetPos;
	private Vector3 worldUpVec;
	
	public Camera3Pcontroller(Camera cam, SceneNode camN, SceneNode targ, String controllerName, InputManager im){
		camera=cam;
		cameraN=camN;
		target=targ;
		cameraAzimuth = 225.0f;
		cameraElevation=15.0f;
		radius=5.0f;
		worldUpVec = Vector3f.createFrom(0.0f,1.0f,0.0f);
		//setupInput(im,controllerName);
		updateCameraPosition();
		
	}
	
	public void updateCameraPosition(){
		double theta = Math.toRadians(cameraAzimuth);
		double phi = Math.toRadians(cameraElevation);
		double x = radius * Math.cos(phi) * Math.sin(theta);
		double y = radius * Math.sin(phi);
		double z = radius * Math.cos(phi) * Math.cos(theta);
		cameraN.setLocalPosition(Vector3f.createFrom((float)x, (float)y, (float)z).add(target.getWorldPosition()));
		//Vector3 newLocation = Vector3f.createFrom(targe
		cameraN.lookAt(target, worldUpVec);
		
		
	}
	public float getCameraAzimuth() {
		return cameraAzimuth;
	}
	public void setCameraAzimuth(float azimuth) {
		cameraAzimuth = azimuth;
	}
	public float getCameraElevation() {
		return cameraElevation;
	}
	public void setCameraElevation(float elevation) {
		cameraElevation = elevation;
	}
	public float getCameraRadias() {
		return radius;
	}
	public void setCameraRadias(float r) {
		radius = r;
	}
	
	private void setupInput (InputManager im, String cn) {
		//Action orbitAAction = new OrbitAroundAction();
		//im.associateAction(cn,net.java.games.input.Component.Identifier.Axis.RX, orbitAAction,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		//Action orbitLeft = new CameraOrbitLeft(this);
			
		//im.associateAction(cn, net.java.games.input.Component.Identifier.Key.LEFT,orbitLeft,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	}
	

	
	
}
package myGameEngine;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import a3.*;

public class LookDownAction extends AbstractInputAction
{ 
	Camera3Pcontroller c3Pc;
	float cameraElevation;
	
	public LookDownAction(Camera3Pcontroller c) {
		c3Pc = c;
	}
	
	public void performAction(float time, net.java.games.input.Event evt)
	{ 
		cameraElevation = c3Pc.getCameraElevation();
		if(cameraElevation <= 89.0f){
			
			cameraElevation += 0.5f;
				
		}else{
			cameraElevation = 89.0f;
		}
		cameraElevation = cameraElevation % 360;
		c3Pc.setCameraElevation(cameraElevation);
		c3Pc.updateCameraPosition();
	} 
}
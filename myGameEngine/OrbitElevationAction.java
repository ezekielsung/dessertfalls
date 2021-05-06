package myGameEngine;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import a3.*;

public class OrbitElevationAction extends AbstractInputAction
{ 
	Camera3Pcontroller c3Pc;
	float cameraElevation;
	
	public OrbitElevationAction(Camera3Pcontroller c) {
		c3Pc = c;
		
	}
	
	public void performAction(float time, net.java.games.input.Event evt)
	{ 
		cameraElevation = c3Pc.getCameraElevation();
		float rotAmount;
		if (evt.getValue() < -0.2) {
			if(cameraElevation >= 1.0f) 
				rotAmount=-0.5f;
			else {
				cameraElevation = 1.0f;
				rotAmount = 0.0f;
			}
		}
		else
		{ 
			if (evt.getValue() > 0.2){
				if(cameraElevation <= 89.0f)
					rotAmount=0.5f;
				else{
					cameraElevation = 89.0f;
					rotAmount = 0.0f;
				}
			}
				 
			else
				rotAmount=0.0f; 
		}
		cameraElevation += rotAmount;
		cameraElevation = cameraElevation % 360;
		c3Pc.setCameraElevation(cameraElevation);
		c3Pc.updateCameraPosition();
	} 
}
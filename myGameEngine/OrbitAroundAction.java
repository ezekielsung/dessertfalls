package myGameEngine;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import a3.*;

public class OrbitAroundAction extends AbstractInputAction
{ 
	Camera3Pcontroller c3Pc;
	float cameraAzimuth;
	
	public OrbitAroundAction(Camera3Pcontroller c) {
		c3Pc = c;
		
	}
	
	public void performAction(float time, net.java.games.input.Event evt)
	{ 
		cameraAzimuth = c3Pc.getCameraAzimuth();
		float rotAmount;
		if (evt.getValue() < -0.2) {
			rotAmount=-0.75f; 
		}
		else
		{
			if (evt.getValue() > 0.2)
				rotAmount=0.75f; 
			else
				rotAmount=0.0f; 
		}
		cameraAzimuth += rotAmount;
		cameraAzimuth = cameraAzimuth % 360;
		c3Pc.setCameraAzimuth(cameraAzimuth);
		c3Pc.updateCameraPosition();
	} 
}
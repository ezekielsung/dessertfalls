package myGameEngine;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import a3.*;
public class RotateLeftAction extends AbstractInputAction
{
	private Node avN;
	private Camera3Pcontroller c3Pc;
	float cameraAzimuth;
	private ProtocolClient protClient;
	
	public RotateLeftAction(Node n, Camera3Pcontroller c,ProtocolClient p)
	{ 
		avN = n;
		c3Pc = c;
		protClient=p;
		
	}
	public void performAction(float time, Event e)
	{ 
		cameraAzimuth = c3Pc.getCameraAzimuth();
		Angle rotAmt = Degreef.createFrom(1.3f);
		avN.yaw(rotAmt);
		cameraAzimuth += 1.3f;
		cameraAzimuth = cameraAzimuth % 360;
		c3Pc.setCameraAzimuth(cameraAzimuth);
		c3Pc.updateCameraPosition();
		protClient.sendRotationMessage(1.3f);
	}
}  

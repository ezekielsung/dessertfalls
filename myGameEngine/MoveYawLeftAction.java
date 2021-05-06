package myGameEngine;

import a3.MyGame;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import ray.rml.Angle;

public class MoveYawLeftAction extends AbstractInputAction
{
private Node avN;
private Camera3Pcontroller c;

public MoveYawLeftAction(Node n,Camera3Pcontroller rot)
{ avN=n;
c=rot;
}

public void performAction(float time, Event e)
{ 
		avN.yaw(Degreef.createFrom(1.0f));
		float rotAmt =1.0f;
		c.cameraAzimuth += rotAmt;
		c.cameraAzimuth = c.cameraAzimuth%360;
		c.updateCameraPosition();
}

}

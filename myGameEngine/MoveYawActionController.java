package myGameEngine;
import a1.MyGame;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import ray.rml.Angle;
public class MoveYawActionController extends AbstractInputAction
{
private Camera camera;
private MyGame mygame;
private Angle rotAmt;

public MoveYawActionController(Camera c, MyGame g)
{ camera = c;
mygame=g;
}

public void performAction(float time, Event e)
{ 
	Vector3 u=camera.getRt();
	Vector3 v=camera.getUp();
	Vector3 n=camera.getFd();

if (e.getValue()<0){
	
	if (mygame.toggleDolphin==true){
	rotAmt = Degreef.createFrom(1.0f);
	mygame.dolphinN.yaw(rotAmt);

} else {
	rotAmt = Degreef.createFrom(1.0f);

	Vector3 uShift =(u.rotate(rotAmt,v)).normalize();
	Vector3 nShift =(n.rotate(rotAmt,v)).normalize();
	
	camera.setRt((Vector3f)uShift);
	camera.setFd((Vector3f)nShift);
	
}
	
} else {
	
	if (mygame.toggleDolphin==true){
	rotAmt = Degreef.createFrom(-1.0f);
	mygame.dolphinN.yaw(rotAmt);

}else {
	rotAmt = Degreef.createFrom(-1.0f);

	Vector3 uShift =(u.rotate(rotAmt,v)).normalize();
	Vector3 nShift =(n.rotate(rotAmt,v)).normalize();
	
	camera.setRt((Vector3f)uShift);
	camera.setFd((Vector3f)nShift);
}
	
}

}

}

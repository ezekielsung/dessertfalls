package myGameEngine;
import a1.MyGame;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import ray.rml.Angle;
public class MovePitchActionController extends AbstractInputAction
{
private Camera camera;
private MyGame mygame;
private Angle rotAmt;

public MovePitchActionController(Camera c, MyGame g)
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
	rotAmt = Degreef.createFrom(-1.0f);
	mygame.dolphinN.pitch(rotAmt);

} else {
	rotAmt = Degreef.createFrom(1.0f);

	Vector3 vShift =(v.rotate(rotAmt,u)).normalize();
	Vector3 nShift =(n.rotate(rotAmt,u)).normalize();
	
	camera.setUp((Vector3f)vShift);
	camera.setFd((Vector3f)nShift);
	
}
	
} else {
	
	if (mygame.toggleDolphin==true){
	rotAmt = Degreef.createFrom(1.0f);
	mygame.dolphinN.pitch(rotAmt);

}else {
	rotAmt = Degreef.createFrom(-1.0f);

	Vector3 vShift =(v.rotate(rotAmt,u)).normalize();
	Vector3 nShift =(n.rotate(rotAmt,u)).normalize();
	
	camera.setUp((Vector3f)vShift);
	camera.setFd((Vector3f)nShift);
}
	
}

}

}

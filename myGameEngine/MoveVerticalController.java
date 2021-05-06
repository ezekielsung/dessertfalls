package myGameEngine;
import a1.MyGame;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
public class MoveVerticalController extends AbstractInputAction
{
private Camera camera;
private MyGame mygame;
public MoveVerticalController(Camera c,MyGame g)
{ camera = c;
mygame=g;
}
public void performAction(float time, Event e)
{ 

if (mygame.toggleDolphin!=true){
	
	Vector3f v = camera.getFd();
	Vector3f p = camera.getPo();
	Vector3f p1 =(Vector3f) Vector3f.createFrom(0.01f*v.x(), 0.01f*v.y(), 0.01f*v.z());
	Vector3f p2;
	if (e.getValue()>0.0){
		p2 = (Vector3f) p.sub((Vector3)p1);
	} else {
		p2 = (Vector3f) p.add((Vector3)p1);
	}
	camera.setPo((Vector3f)Vector3f.createFrom(p2.x(),p2.y(),p2.z()));
}else {
	
	if (e.getValue()<0.0){
		mygame.dolphinN.moveForward(0.03f);
	} else {
		mygame.dolphinN.moveBackward(0.03f);
	}
	
	
}
}
}
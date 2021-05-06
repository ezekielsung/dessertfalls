package myGameEngine;

import a3.MyGame;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import ray.rml.Angle;

public class MovePitchDownAction extends AbstractInputAction
{
private Camera camera;
private MyGame mygame;
private Angle rotAmt;

public MovePitchDownAction(Camera c, MyGame g)
{ camera = c;
mygame=g;
}

public void performAction(float time, Event e)
{ 

	rotAmt = Degreef.createFrom(1.0f);
	mygame.avatarN.pitch(rotAmt);

}



}
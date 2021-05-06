package myGameEngine;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import a3.*;

public class InteractAction extends AbstractInputAction
{ 
	private MyGame myGame;
	public InteractAction(MyGame g) {
		myGame = g;
		
	}
	
	public void performAction(float time, net.java.games.input.Event evt)
	{ 
		
			myGame.interact();
		
	} 
}

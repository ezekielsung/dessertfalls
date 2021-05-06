package myGameEngine;

import a3.MyGame;
import ray.input.action.AbstractInputAction;
import ray.rage.game.*;
import net.java.games.input.Event;
public class QuitGameAction extends AbstractInputAction
{
	private MyGame game;
	public QuitGameAction(MyGame g){
		game = g;
	}
	public void performAction(float time, Event event){ 
		game.quitGame();
	}
}
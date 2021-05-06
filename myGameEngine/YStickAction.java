package myGameEngine;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import a3.*;

public class YStickAction extends AbstractInputAction
{ 
	Node avN;
	private ProtocolClient protClient;
	private int round;
	private MyGame myGame;
	
	public YStickAction(Node n, MyGame g, ProtocolClient p) {
		protClient = p;
		myGame = g;
		avN = n;
		
	}
	
	public void performAction(float time, net.java.games.input.Event evt)
	{ 
		
		
		if(!myGame.getDead()) {
			round = myGame.getRound();
			
			Vector3 loc = avN.getLocalPosition();
			if (evt.getValue() < -0.2) {
				avN.moveForward(0.13f);
				loc = avN.getLocalPosition();
				if(loc.y() > 1.5){
					avN.moveForward(0.13f);
				}
				myGame.updateVerticalPosition();
				
				if(round%5 == 1) {
					if(loc.x() < -4.5 && loc.z() < -15.393) {
						avN.moveForward(0.13f);
					}
				}else if(round%5 == 2) {
					if(loc.x() > 34.4449 && loc.z() > -15.8371) {
						avN.moveForward(0.13f);
					}
				}else if(round%5 == 3) {
					if(loc.x() < 8.9299 && loc.z() < 39.5956) {
						avN.moveForward(0.13f);
					}
				}else if(round%5 == 4) {
					if(loc.x() > -42.4501 && loc.z() < 10.5722) {
						avN.moveForward(00.13f);
					}
				}else if(round%5 == 0) {
					if(loc.x() > -12.0751 && loc.z() < -29.0945) {
						avN.moveForward(0.13f);
					}
				}
			}else{ 
				if (evt.getValue() > 0.2){
					avN.moveBackward(0.13f);
					loc = avN.getLocalPosition();
					if(loc.y() > 1.5){
						avN.moveBackward(0.13f);
					}
					myGame.updateVerticalPosition();
					
					if(round%5 == 1) {
						if(loc.x() < -4.5 && loc.z() < -15.393) {
							avN.moveBackward(0.13f);
						}
					}else if(round%5 == 2) {
						if(loc.x() > 34.4449 && loc.z() > -15.8371) {
							avN.moveBackward(0.13f);
						}
					}else if(round%5 == 3) {
						if(loc.x() < 8.9299 && loc.z() < 39.5956) {
							avN.moveBackward(0.13f);
						}
					}else if(round%5 == 4) {
						if(loc.x() > -42.4501 && loc.z() < 10.5722) {
							avN.moveBackward(00.13f);
						}
					}else if(round%5 == 0) {
						if(loc.x() > -12.0751 && loc.z() < -29.0945) {
							avN.moveBackward(0.13f);
						}
					}
				}
				else{}
			}
			protClient.sendMoveMessage(avN.getWorldPosition());
		}
		
		
		
	} 
}

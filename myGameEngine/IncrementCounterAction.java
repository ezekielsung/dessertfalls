package myGameEngine;

import a1.MyGame;
import ray.input.action.AbstractInputAction;
import ray.rage.game.*;
import net.java.games.input.Event;


public class IncrementCounterAction extends AbstractInputAction
{
private MyGame game;
private IncrementAmountModifierAction incAmtModAct;
public IncrementCounterAction(MyGame g, IncrementAmountModifierAction modAct)
{ game = g;
incAmtModAct = modAct;
}

public void performAction(float time, Event e)
{ System.out.println("counter action initiated");
int incAmt = incAmtModAct.getIncAmt();
//game.incrementCounter(incAmt);
}
}
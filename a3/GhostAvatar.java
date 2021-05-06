package a3;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import ray.rage.*;
import ray.rage.game.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.scene.*;
import ray.rage.scene.Camera.Frustum.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.rage.rendersystem.states.*;
import ray.rage.asset.texture.*;
import ray.input.*;
import ray.input.action.*;
import ray.rage.util.BufferUtil;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import ray.rage.asset.texture.Texture;
import ray.rage.rendersystem.shader.*;
import java.util.Random;
import java.util.ArrayList;
import net.java.games.input.Controller;
import ray.rage.asset.material.Material;
import ray.rage.rendersystem.states.*;
import ray.rage.asset.texture.*;
import ray.rage.util.*;
import java.awt.geom.*;
import java.util.UUID;

public class GhostAvatar{
	private UUID id;
	private SceneNode node;
	private Entity entity;
	private Vector3 position;
	private float rotation;
	private int round;
	
	public GhostAvatar(UUID id, Vector3 position){ 
		this.id = id;
		this.position = position;
		this.rotation = 0;
		this.round = 0;
		
	}
	// accessors and setters for id, node, entity, and position
	public void setID(UUID id){
		this.id = id;
	}
	public UUID getID() {
		return id;
	}
	
	public void setNode(SceneNode node) {
		this.node = node;
	}
	public SceneNode getNode() {
		return node;
	}
	
	public void setEntity(Entity entity){
		this.entity = entity;
	} 
	public Entity getEntity() {
		return entity;
	}
	
	public void setPosition(Vector3 position) {
		this.position = position;
	}
	public Vector3 getPosition() {
		return position;
	}
	public void rotateGhost(float rot){
		Angle rotAmt = Degreef.createFrom(rot);
		node.yaw(rotAmt);
	}
	public void increaseRound(){
		round++;
	}
	
	

}
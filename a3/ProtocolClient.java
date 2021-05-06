package a3;

import Server.*;
import ray.rml.*;
import ray.rage.*;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import ray.networking.server.GameConnectionServer;
import ray.networking.server.IClientInfo;
import ray.networking.IGameConnection.ProtocolType;
import ray.networking.*;
import ray.networking.client.*;
import java.util.*;
import ray.rage.scene.*;


public class ProtocolClient extends GameConnectionClient{
	private MyGame game;
	private UUID id;
	private Vector<GhostAvatar> ghostAvatars;
	private Vector<GhostNPC> ghostNPCs;
	
	public ProtocolClient(InetAddress remAddr, int remPort,
						  ProtocolType pType, MyGame game) throws IOException{ 
		super(remAddr, remPort, pType);
		this.game = game;
		this.id = UUID.randomUUID();
		this.ghostAvatars = new Vector<GhostAvatar>();
		this.ghostNPCs = new Vector<GhostNPC>();
	}
	
	private void createGhostNPC(int id, Vector3 position) throws IOException{
		GhostNPC NPC = new GhostNPC(id,position);
		ghostNPCs.add(NPC);
		game.addGhostNPCtoGameWorld(NPC);
	}
	private void createBossNPC(int id, Vector3 position) throws IOException{
		GhostNPC NPC = new GhostNPC(id,position);
		ghostNPCs.add(NPC);
		game.addBossNPCtoGameWorld(NPC);

		
	}
	
	private void updateGhostNPC(int id, Vector3 position) {
		//ghostNPCs.get(id).setPosition(position);
		
	}
	

	
	@Override
	protected void processPacket(Object msg){
		String strMessage = (String)msg;
		String[] messageTokens = strMessage.split(",");
		if(messageTokens.length > 0){
			if(messageTokens[0].compareTo("join") == 0) {
				// receive join
				// format: join, success or join, failure
				if(messageTokens[1].compareTo("success") == 0){ 
					game.setIsConnected(true);
					//sendNeedNPCMessage();
					//sendCreateNPCMessage();
					//sendCreateMessage(game.getPlayerPosition());
					
					
				}
				if(messageTokens[1].compareTo("failure") == 0){ 
					game.setIsConnected(false);
				} 
			}
			if(messageTokens[0].compareTo("bye") == 0){ 
			 // receive bye
			 // format: bye, remoteId
				UUID ghostID = UUID.fromString(messageTokens[1]);
				removeGhostAvatar(ghostID);
			}
			/* if ((messageTokens[0].compareTo("sdsm") == 0 ) {
				
				System.out.println("got into create of protClient");
				UUID ghostID = UUID.fromString(messageTokens[1]);
				Vector3 ghostPosition = Vector3f.createFrom(
				Float.parseFloat(messageTokens[2]),
				Float.parseFloat(messageTokens[3]),
				Float.parseFloat(messageTokens[4]));
				String choice;
				for(int i = 0; i < ghostAvatars.size(); i++) {
					if(ghostAvatars.get(i).getID().compareTo(UUID.fromString(messageTokens[1]) == 0) {
						choice = ghostAvatars.get(i).getChoice();
					}
				}
				
				try{ 
					createGhostAvatar(ghostID, ghostPosition,choice);
				}catch (NullPointerException e){ 
					//e.printStackTrace();
					System.out.println("error creating ghost avatar");
				} 
				
			} */
			
			if( (messageTokens[0].compareTo("create")==0) 
				|| ((messageTokens[0].compareTo("sdsm") == 0 ) )){ 
				UUID ghostID = UUID.fromString(messageTokens[2]);
				Vector3 ghostPosition = Vector3f.createFrom(
				Float.parseFloat(messageTokens[3]),
				Float.parseFloat(messageTokens[4]),
				Float.parseFloat(messageTokens[5]));
				try{ 
					createGhostAvatar(ghostID, ghostPosition,messageTokens[1]);
				}catch (NullPointerException e){ 
					//e.printStackTrace();
					System.out.println("error creating ghost avatar");
				} 
				
			}
			if(messageTokens[0].compareTo("createGhost") == 0){ // rec. create
			
				System.out.println("create ghost packet arrived to client");
				UUID ghostID=UUID.fromString(messageTokens[1]);
				Vector3 ghostPosition = Vector3f.createFrom(
				Float.parseFloat(messageTokens[2]),
				Float.parseFloat(messageTokens[3]),
				Float.parseFloat(messageTokens[4]));
				//createGhostAvatar(ghostID,ghostPosition);
			} 
			if(messageTokens[0].compareTo("wsds") == 0){ // rec. wants
				UUID ghostID=UUID.fromString(messageTokens[1]);
				Vector3 ghostPosition = game.getPlayerPosition();
				sendDetailsForMessage(ghostID, ghostPosition);
				
			}
			if(messageTokens[0].compareTo("move") == 0) // rec. move
			{
				
				UUID ghostID=UUID.fromString(messageTokens[1]);
				Vector3 ghostPosition = Vector3f.createFrom(
				Float.parseFloat(messageTokens[2]),
				Float.parseFloat(messageTokens[3]),
				Float.parseFloat(messageTokens[4]));
				//move position of the ghostID
				updateGhost(ghostID,ghostPosition);
				

			}
			
			if(messageTokens[0].compareTo("mnpc") == 0){
				int ghostID = Integer.parseInt(messageTokens[1]);
				int ghostCounter= game.getGhostNum();
				int start=game.getStartGhost();
				//System.out.println(ghostCounter);
				SceneNode ghostN;
				Vector3 ghostPosition = Vector3f.createFrom(
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3]),
					Float.parseFloat(messageTokens[4]));
				updateGhostNPC(ghostID, ghostPosition);
				
				
						
				if(game.getNpcCreated() == true){
					for (int i=start;i<=ghostCounter;i++){
						//System.out.println(ghostCounter);
						ghostN = game.sm.getSceneNode("myNPC"+i+"Node");
						if (ghostN!=null && game.getDead() == false){
							ghostN.lookAt(game.avatarN);
							ghostN.moveForward(.05f+(i%3f)*.02f);
						}
					}
				}
				
			}
			if(messageTokens[0].compareTo("cboss") == 0){
				int ghostID = Integer.parseInt(messageTokens[1]);
				Vector3 ghostPosition = Vector3f.createFrom(
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3]),
					Float.parseFloat(messageTokens[4]));
				try {

					createBossNPC(ghostID, ghostPosition);
				}catch (IOException e){

					e.printStackTrace();
				}				
			}
			if(messageTokens[0].compareTo("cnpc") == 0){
				int ghostID = Integer.parseInt(messageTokens[1]);
				Vector3 ghostPosition = Vector3f.createFrom(
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3]),
					Float.parseFloat(messageTokens[4]));
				try {

					createGhostNPC(ghostID, ghostPosition);
				}catch (IOException e){

					e.printStackTrace();
				}				
			}
			if(messageTokens[0].compareTo("rotate") == 0) {
				UUID ghostID=UUID.fromString(messageTokens[1]);
				float rotAmt = Float.parseFloat(messageTokens[2]);
				//move position of the ghostID
				updateGhostRotate(ghostID,rotAmt);
			}
		} 
	}
	
	public void askForNPCinfo() {
		try{
			sendPacket(new String("needNPC," + id.toString()));
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void updateGhost(UUID ghostID, Vector3 position){
		for (GhostAvatar ghost: ghostAvatars){
			if (ghost.getID().equals(ghostID)){
				ghost.setPosition(position);
				game.avatar2N.setLocalPosition(position);
			}	
		}
		
	}
	public void updateGhostRotate(UUID ghostID, float rot){
		for (GhostAvatar ghost: ghostAvatars){
			if (ghost.getID().equals(ghostID)){
				ghost.setNode(game.avatar2N);
				ghost.rotateGhost(rot);
			}	
		}
		
	}
	public void sendCreateNPCMessage(){
		try{
			int round=game.getRound();
			sendPacket(new String("createNPC,"+round+","+id.toString()));
		} catch (IOException e){
			e.printStackTrace();
		}
		
	}
	
	private void sendNeedNPCMessage(){
		try{
			sendPacket(new String("needNPC"));
		} catch (IOException e){
			e.printStackTrace();
		}
		
	}
	
	public void sendJoinMessage(){ // format: join, localId
		try{ 
			sendPacket(new String("join," + id.toString()));
			System.out.println(id + " joined");
		}catch (IOException e) { 
			e.printStackTrace();
		} 
	}
	
	public void sendCreateMessage(Vector3 pos,String choice) { // format: (create, localId, x,y,z)

		
		try{ 
			String message = new String("create,"+choice+"," + id.toString());
			message += "," + pos.x()+"," + pos.y() + "," + pos.z();
			sendPacket(message);
		}catch (IOException e) { 
			e.printStackTrace();
		} 
	}
	public void sendByeMessage(){
	 	try{ 
			sendPacket(new String("bye," + id.toString()));
			System.out.println(id + "Left");
		}catch (IOException e) { 
			e.printStackTrace();
		}  
	}
	
	public void sendDetailsForMessage(UUID remId, Vector3 pos){ 
		String choice = game.getChoiceString();
		try{ 
			String message = new String("dsfr," +choice+","+ id.toString()+","+remId.toString());
			message += "," + pos.x()+"," + pos.y() + "," + pos.z();
			sendPacket(message);
		}catch (IOException e) { 
			e.printStackTrace();
		} 
	}
	
	public void sendMoveMessage(Vector3 pos){
		try{ 
			String message = new String("move," + id.toString());
			message += "," + pos.x()+"," + pos.y() + "," + pos.z();
			sendPacket(message);
		}catch (IOException e) { 
			e.printStackTrace();
		}  
	}
	
	public void sendRotationMessage(float rot) {
		try{ 
			String message = new String("rotate," + id.toString());
			message += "," + rot;
			sendPacket(message);
		}catch (IOException e) { 
			e.printStackTrace();
		}  
	}
	
	public void sendIncreaseRound(){
		try{ 
			String message = new String("increase," + id.toString());
			sendPacket(message);
		}catch (IOException e) { 
			e.printStackTrace();
		}  
	}
	
	public void sendHelp(UUID clientID){
		
	}
	
	public void createGhostAvatar(UUID ghostID,Vector3 ghostPosition, String choice) {
		
		try{
			GhostAvatar newGhost = new GhostAvatar(ghostID, ghostPosition);
			ghostAvatars.add(newGhost);
			game.addGhostAvatarToGameWorld(newGhost,ghostPosition, choice);
			
		}catch(IOException e) {
			System.out.println("");
		}
		
	}
	public void removeGhostAvatar(UUID ghostID) {
		
		for (GhostAvatar ghost: ghostAvatars){
			if (ghost.getID().equals(ghostID)){
				game.removeGhostAvatarFromGameWorld(ghost);
			}	
		}
		
		ghostAvatars.remove(ghostID);
		
	}
}

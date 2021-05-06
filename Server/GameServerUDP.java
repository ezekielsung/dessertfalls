
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import ray.networking.server.GameConnectionServer;
import ray.networking.server.IClientInfo;
import ray.networking.server.UDPClientInfo;

public class GameServerUDP extends GameConnectionServer<UUID>{
	private NPCcontroller npcCtrl;
	private NPC[] npcList;
	
	public GameServerUDP(int localPort) throws IOException{ 
		super(localPort, ProtocolType.UDP); 
		
		
	}
	@Override
	public void processPacket(Object o, InetAddress senderIP, int sndPort){ 
		String message = (String) o;
		String[] msgTokens = message.split(",");
		if(msgTokens.length > 0){
			// case where server receives a JOIN message
			// format: join,localid
			if(msgTokens[0].compareTo("join") == 0){ 
				try{ 
					IClientInfo ci;
					ci = getServerSocket().createClientInfo(senderIP, sndPort);
					UUID clientID = UUID.fromString(msgTokens[1]);
					addClient(ci, clientID);
					sendJoinedMessage(clientID, true);
					System.out.println(senderIP + " joined");
				}catch (IOException e){ 
					e.printStackTrace();
				} 
				//sendCreateMessages(clientID, 
			}
			// case where server receives a CREATE message
			// format: create,localid,x,y,z
			if(msgTokens[0].compareTo("create") == 0){ 
				UUID clientID = UUID.fromString(msgTokens[2]);
				String[] pos = {msgTokens[3], msgTokens[3], msgTokens[5]};
				sendCreateMessages(clientID, pos,msgTokens[1]);
				sendWantsDetailsMessages(clientID);
				//sendCreateGhost(clientID,"0,3,0");
			}
			// case where server receives a BYE message
			// format: bye,localid
			if(msgTokens[0].compareTo("bye") == 0){ 
				UUID clientID = UUID.fromString(msgTokens[1]);
				sendByeMessages(clientID);
				removeClient(clientID);
			}
			// case where server receives a DETAILS-FOR message
			if(msgTokens[0].compareTo("dsfr") == 0){ 
				UUID targetID=UUID.fromString(msgTokens[3]);
				UUID clientID=UUID.fromString(msgTokens[2]);
				String[] pos = {msgTokens[4], msgTokens[5], msgTokens[6]};
				sndDetailsMsg(clientID,targetID,pos,msgTokens[1]);
				
			}
			// case where server receives a MOVE message
			if(msgTokens[0].compareTo("move") == 0){ 
				UUID clientID=UUID.fromString(msgTokens[1]);
				String[] pos = {msgTokens[2], msgTokens[3], msgTokens[4]};
				sendMoveMessages(clientID,pos);
			}
			if(msgTokens[0].compareTo("needNPC") == 0){
				sendNPCinfo(npcCtrl);
				System.out.println("send NPC info");
			}
			if(msgTokens[0].compareTo("collide") == 0){
				
			}
			if(msgTokens[0].compareTo("createNPC") == 0){
				sendCreateNPCInfo(msgTokens[1]);
			}
			if(msgTokens[0].compareTo("rotate") == 0){
				UUID clientID=UUID.fromString(msgTokens[1]);
				sendRotateMessage(clientID, msgTokens[2]);
			}
			
		} 
	}
	public void sendRotateMessage(UUID clientID, String rot){
		try{ 
			String message = new String("rotate," + clientID.toString());
			message += "," + rot;
			forwardPacketToAll(message, clientID);
		}catch (IOException e) { 
			e.printStackTrace();
		} 
	}
	public void sendJoinedMessage(UUID clientID, boolean success){ 
		// format: join, success or join, failure
		try{ 
			String message = new String("join,");
			if (success) message += "success";
			else message += "failure";
			sendPacket(message, clientID);
		}catch (IOException e) { 
			e.printStackTrace(); 
		}
	}
	
	public void sendCreateMessages(UUID clientID, String[] position,String choice){ 
		// format: create, remoteId, x, y, z
		try{ 
			String message = new String("create,"+choice+"," + clientID.toString());
			message += "," + position[0];
			message += "," + position[1];
			message += "," + position[2];
			forwardPacketToAll(message, clientID);
		}catch (IOException e) { 
			e.printStackTrace();
		} 
	}
	public void sendCreateGhost(UUID clientID, String position){
		
		try {
		String message = new String("createGhost,"+clientID.toString());
		message +=","+position;
		forwardPacketToAll(message,clientID);
		}catch (IOException e){
			
			e.printStackTrace();
			
		}
	}
		
	public void sendCreateNPCInfo(String round){
		if(round.equals("2")) {
			for (int i=0; i<3; i++){
				try{ 
					String message = new String("cnpc," + Integer.toString(i));
					message += "," + npcList[i].getX();
					message += "," + npcList[i].getY();
					message += "," + npcList[i].getZ();
					System.out.println(message);
					sendPacketToAll(message);
				}catch (IOException e){
						
						e.printStackTrace();
						
				}
			}
	
		}else if(round.equals("3")) {
			for (int i=3; i<6; i++){
				try{ 
					String message = new String("cnpc," + Integer.toString(i));
					message += "," + npcList[i].getX();
					message += "," + npcList[i].getY();
					message += "," + npcList[i].getZ();
					System.out.println(message);
					sendPacketToAll(message);
				}catch (IOException e){
						
						e.printStackTrace();
						
				}
	
			}
		}else if(round.equals("4")) {
			for (int i=6; i<9; i++){
				try{ 
					String message = new String("cnpc," + Integer.toString(i));
					message += "," + npcList[i].getX();
					message += "," + npcList[i].getY();
					message += "," + npcList[i].getZ();
					sendPacketToAll(message);
				}catch (IOException e){
						
						e.printStackTrace();
						
				}
	
			}
		}
		else if(round.equals("5")) {
			for (int i=9; i<10; i++){
				try{ 
					String message = new String("cboss," + Integer.toString(i));
					message += "," + npcList[i].getX();
					message += "," + npcList[i].getY();
					message += "," + npcList[i].getZ();
					sendPacketToAll(message);
				}catch (IOException e){
						
						e.printStackTrace();
						
				}
	
			}
		}
		
		
		
		
		
	}
	
	
	
	public void sndDetailsMsg(UUID clientID, UUID remoteID, String[] position,String choice){
		try {
		String message = new String("sdsm,"+choice+","+clientID.toString());
		message += "," + position[0];
			message += "," + position[1];
			message += "," + position[2];
		sendPacket(message,remoteID);
		}catch (IOException e){
			
			e.printStackTrace();
			
		} 
	
	
	}
	public void sendWantsDetailsMessages(UUID clientID){
	  try {
		String message = new String("wsds,"+clientID.toString());
		
		forwardPacketToAll(message,clientID);
		}catch (IOException e){
			
			e.printStackTrace();
			
		}
	}
	public void sendMoveMessages(UUID clientID, String[] position){
		try{ 
			String message = new String("move," + clientID.toString());
			message += "," + position[0];
			message += "," + position[1];
			message += "," + position[2];
			forwardPacketToAll(message, clientID);
		}catch (IOException e) { 
			e.printStackTrace();
		} 
	}
	public void sendByeMessages(UUID clientID){
		try {
			String message = new String("bye,"+clientID.toString());
			forwardPacketToAll(message,clientID);
		}catch (IOException e){
				
				e.printStackTrace();
				
		} 
	}
	
	public void sendNPCinfo(NPCcontroller npcCtrl) {// informs clients of new NPC positions
		this.npcCtrl = npcCtrl;
		npcList = npcCtrl.getNPCList(); 
		for (int i=0; i<3; i++){
			try{ 
				String message = new String("mnpc," + Integer.toString(i));
				message += "," + (npcCtrl.getNPC(i)).getX();
				message += "," + (npcCtrl.getNPC(i)).getY();
				message += "," + (npcCtrl.getNPC(i)).getZ();
				sendPacketToAll(message);
			}catch (IOException e){
					
					e.printStackTrace();
					
			} 
		
		}
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
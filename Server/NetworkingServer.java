
import java.io.IOException;
import ray.networking.IGameConnection.ProtocolType;

public class NetworkingServer{
	private GameServerUDP thisUDPServer;
	//private GameServerTCP thisTCPServer;
	private NPCcontroller npcCtrl;
	
	long startTime;
	long lastUpdateTime;
	
	public NetworkingServer(){ // constructor
		
	}
	
	public NetworkingServer(int serverPort, String protocol){
		try{ 
			if(protocol.toUpperCase().compareTo("TCP") == 0){
				//thisTCPServer = new GameServerTCP(serverPort);
			}else{
				thisUDPServer = new GameServerUDP(serverPort);
				startTime = System.nanoTime();
				lastUpdateTime = startTime;
				npcCtrl = new NPCcontroller();

				// start networking TCP server (as before)
				 
				// start NPC control loop
				npcCtrl.setupNPCs();
				npcLoop(npcCtrl);
				System.out.println("created server");
			}
		}catch (IOException e){
			e.printStackTrace();
		} 
	}
	public void npcLoop(NPCcontroller npcCtrl) {// NPC control loop
		
		while (true){
			long frameStartTime = System.nanoTime();
			float elapMilSecs = (frameStartTime-lastUpdateTime)/(1000000.0f);
			if (elapMilSecs >= 50.0f){
				lastUpdateTime = frameStartTime;
				npcCtrl.updateNPCs();
				thisUDPServer.sendNPCinfo(npcCtrl);
			}
			Thread.yield();
		}
	}
	public static void main(String[] args){
		if(args.length > 1){ 
			NetworkingServer app =
			new NetworkingServer(Integer.parseInt(args[0]), args[1]);
		} 
	} 
}
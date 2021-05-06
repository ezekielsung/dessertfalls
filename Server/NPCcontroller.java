
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import ray.networking.server.GameConnectionServer;
import ray.networking.server.IClientInfo;
import ray.networking.server.UDPClientInfo;



public class NPCcontroller{
	private NPC[] NPClist = new NPC[10];
	static int test=1;
	
	public void setupNPCs(){
		for(int i = 0; i < 10; i++) {
			NPClist[i] = new NPC(i);
		}
	}

	public void updateNPCs() { 
		for (int i=0; i<10; i++){
			NPClist[i].updateLocation();
		}
		
		
	}
	 
	public int getNumOfNPCs() {
		return 3;
	}
	public NPC[] getNPCList() {
		return NPClist;
	}
		 
	
	public NPC getNPC(int num){
		return NPClist[num];
	}

}
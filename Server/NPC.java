

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import ray.networking.server.GameConnectionServer;
import ray.networking.server.IClientInfo;
import ray.networking.server.UDPClientInfo;

public class NPC{

	 double locX, locY, locZ;	 // other state info goes here (FSM)
	 int counter=0,health=10;
	 double [][]posArr;
	 
	 
	 public NPC(int ghostNum){
		if(ghostNum == 0){
			posArr = new double[1][3];
			posArr[0][0]=33.523;
			posArr[0][1]= 0.0;
			posArr[0][2]= -36.3179;
			
		}
		
		else if(ghostNum == 1){
			posArr = new double[1][3];
			posArr[0][0]=42.523;
			posArr[0][1]= 0.0;
			posArr[0][2]= -28.3179;
			
		}
		else if(ghostNum == 2){
			posArr = new double[1][3];
			posArr[0][0]=40.523;
			posArr[0][1]= 0.0;
			posArr[0][2]= -38.3179;
			
		} 	
		else if(ghostNum == 3){
			posArr = new double[1][3];
			posArr[0][0]=20.843;
			posArr[0][1]= 0.0;
			posArr[0][2]= 40.1179;
			
		} 	
		else if(ghostNum == 4){
			posArr = new double[1][3];
			posArr[0][0]=15.9379;
			posArr[0][1]= 0.0;
			posArr[0][2]= 35.7679;
			
		} 
		else if(ghostNum == 5){
			posArr = new double[1][3];
			posArr[0][0]=16.7163;
			posArr[0][1]= 0.0;
			posArr[0][2]= 27.7279;
			
		} 	
		else if(ghostNum == 6){
			posArr = new double[1][3];
			posArr[0][0]=-39.4623;
			posArr[0][1]= 0.0;
			posArr[0][2]= 19.787;
			
		} 
		else if(ghostNum == 7){
			posArr = new double[1][3];
			posArr[0][0]=-35.07;
			posArr[0][1]= 0.0;
			posArr[0][2]= 16.6625;
			
		}
		else if(ghostNum == 8){
			posArr = new double[1][3];
			posArr[0][0]=-29.2293;
			posArr[0][1]= 0.0;
			posArr[0][2]= 12.8088;
			
		}
		else if(ghostNum == 9){
			posArr = new double[1][3];
			posArr[0][0]=-28.59;
			posArr[0][1]= 0.0;
			posArr[0][2]= -37.7058;
			
		} 					

	
	 }
	 
	public double getX() { return locX; }
	public double getY() { return locY; }
	public double getZ() { return locZ; }



	public void updateLocation() {
		
		locX = posArr[counter][0];
		locY = posArr[counter][1];
		locZ = posArr[counter][2]; 
		
	 }
}
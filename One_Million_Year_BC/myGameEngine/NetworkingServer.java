package myGameEngine;

import a1.*;
import java.io.IOException;
import ray.networking.IGameConnection.ProtocolType;

public class NetworkingServer
{
	private GameServerUDP thisUDPServer;
	private NPCcontroller npcControl;
	private long lastUpdateTime;
	private long startTime;
	
	public NetworkingServer(int serverPort, String protocol)
	{ 
		startTime = System.nanoTime();
		lastUpdateTime = startTime;
		npcControl = new NPCcontroller();
		try
		{
			if (protocol.equals("UDP"))
			{
				thisUDPServer = new GameServerUDP(serverPort);
			}
			else if (protocol.equals("TCP"))
			{
				//thisTCPServer = new GameServerTCP(serverPort);
			}
		}
		catch (IOException e)
		{ 
			e.printStackTrace();
		} 
		//npcControl.setupNPC();
		//npcLoop();
	}
	
	public void npcLoop()
	{
		while (true)
		{
			long frameStartTime = System.nanoTime();
			float elapMilSecs = (frameStartTime - lastUpdateTime) / (1000000.0f);
			if (elapMilSecs >= 50.0f)
			{
				lastUpdateTime = frameStartTime;
				npcControl.updateNPCs();
				//thisUDPServer.sendNPCInfo();
			}
		}
	}
	
	public static void main(String[] args)
	{ 
		if(args.length > 1)
		{ 
			NetworkingServer app = new NetworkingServer(Integer.parseInt(args[0]), args[1]);
		} 
	}
}
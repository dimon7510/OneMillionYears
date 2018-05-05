package myGameEngine;

import java.util.Random;

import ray.ai.behaviortrees.BTCompositeType;
import ray.ai.behaviortrees.BTSequence;
import ray.ai.behaviortrees.BehaviorTree;

public class NPCcontroller 
{
	private Random rand = new Random();
	private NPC NPC;
	private NPC[] NPClist = new NPC[5];
	private BehaviorTree bt = new BehaviorTree(BTCompositeType.SELECTOR);
	private float startThinkTime, tickStartTime, lastThinkUpdate, lastTickUpdate;
	
	public void start()
	{
		startThinkTime = System.nanoTime();
		tickStartTime = System.nanoTime();
		lastTickUpdate = tickStartTime;
		lastThinkUpdate = startThinkTime;
		setupNPC();
		//setupBehaviorTree();
		npcLoop();
	}
	/*
	public void setupBehaviorTree()
	{
		bt.insertAtRoot(new BTSequence(10));
		bt.insertAtRoot(new BTSequence(20));
		bt.insert(10, new OneSecPassed(this, NPC, false)));
		bt.insert(10, new GetSmall(npc));
		bt.insert(20,  new AvvatarNear(server, this, NPC, false));
		bt.insert(20, new GetBig(NPC));
		
	}
	*/
	public void setupNPC()
	{
		for (int i = 0; i < NPClist.length; i++)
		{
			double randX = rand.nextInt(40) - 20;
			double randY = rand.nextInt(40) - 20;
			NPC = new NPC(randX, randY);
		}
	}
	public void npcLoop()
	{
		while (true)
		{
			long currentTime = System.nanoTime();
			float elapsedThinkMilliSecs = (currentTime - lastThinkUpdate)/(1000000.0f);
			float elapsedTickMilliSecs = (currentTime - lastTickUpdate)/(1000000.0f);
			if (elapsedTickMilliSecs >= 50.0f) // “TICK”
			{ 
				lastTickUpdate = currentTime;
				NPC.updateLocation();
				//server.sendNPCinfo();
			}
			if (elapsedThinkMilliSecs >= 500.0f) // “THINK”
			{ 
				//lastThinkUpdateTime = currentTime;
				//bt.update(elapsedMilliSecs);
			}
			Thread.yield();
		}
	}
	
	public void updateNPCs()
	{
		for (int i = 0; i < NPClist.length; i++)
		{
			NPClist[i].updateLocation();
		}
	}
	
	public int getNumberOfNPCs()
	{
		return NPClist.length;
	}
}
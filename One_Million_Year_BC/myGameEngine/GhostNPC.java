package myGameEngine;

import java.util.UUID;

import ray.rage.scene.Entity;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;

public class GhostNPC 
{
	private UUID ghostID;
	private SceneNode node;
	private Entity entity;
	
	public GhostNPC(UUID id, Vector3 pos)
	{
		this.ghostID = id;
		node.setLocalPosition(pos);
	}
	
	public void setPosition(Vector3 pos)
	{
		node.setLocalPosition(pos);
	}
	public void setSceneNode(SceneNode n)
	{
		this.node = n;
	}
	
	public SceneNode getSceneNode()
	{
		return node;
	}
	public Vector3 getPosition()
	{
		return node.getLocalPosition();
	}
	public UUID getID()
	{
		return ghostID;
	}
}
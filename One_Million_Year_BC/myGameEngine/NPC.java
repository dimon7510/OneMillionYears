package myGameEngine;

public class NPC 
{
	private double localX, localY, localZ;
	private int life;
	private String behavior;
	
//~~~~~~~~~~~~~~~~~// INITIALIZATION //~~~~~~~~~~~~~~~~~//
	public NPC(double X, double Y)
	{
		this.localX = X;
		this.localY = Y;
		this.life = 100;
		this.behavior = "IDLE";
	}
	
//~~~~~~~~~~~~~~~~~// SETTERS //~~~~~~~~~~~~~~~~~//
	public void setBehavior(String b)
	{
		behavior = b;
	}
	
//~~~~~~~~~~~~~~~~~// GETTERS //~~~~~~~~~~~~~~~~~//	
	public double getX()
	{
		return localX;
	}
	public double getY()
	{
		return localY;
	}
	public double getZ()
	{
		return localZ;
	}
	public String getBehavior()
	{
		return behavior;
	}
	public int getLife()
	{
		return life;
	}

//~~~~~~~~~~~~~~~~~// UPDATE //~~~~~~~~~~~~~~~~~//
	public void updateLocation()
	{
		
	}
}

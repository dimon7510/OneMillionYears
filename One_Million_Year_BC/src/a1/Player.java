package a1;

import ray.rml.Vector3f;

public class Player extends GameObject{
	
	private int life;
	private int score;
	private int strength;
	private float speed;
	private int spears;
	private int stones;
	private String statusStr;
	private String dispStr;
	private boolean alive;
	private boolean male;
	private boolean walking;
	private boolean throwing;
	private int activeWeapon;
	
	//variables for scripting
	public static int Life ;
	public static float MaleSpeed ;
	public static float FemaleSpeed;
	public static int MaleStrength;
	public static int FemaleStrength;
	public static float size =1.0f;
	
	
	public Player (String s, boolean male, int activeWeapon)
	{
		setLocation();
		setMale(male);
		if (male) setSpeed(MaleSpeed);
			else setSpeed(FemaleSpeed);
		setObjectName(s);
		setLife(Life);
		setScore(0);
		setAlive(true);
		setStatusStr(".  Find weapon(stones and spears) and kill rivals.");
		setDispStr();
		if (male )setStrength(MaleStrength);
			else setStrength(FemaleStrength);
		setSpears(0);
		setStones(0);
		setActiveWeapon(activeWeapon);
		setWalking(false);
		setThrowing(false);
	}
   
   public Player(Vector3f positionVect, String s, boolean male, int activeWeapon)
	{
	   	setLocation(positionVect);
		if (male) setSpeed(MaleSpeed);
			else setSpeed(FemaleSpeed);
		setObjectName(s);
		setLife(Life);
		setScore(0);
		setAlive(true);
		setStatusStr(".  Find weapon(stones and spears) and kill rivals.");
		setDispStr();
		if (male )setStrength(MaleStrength);
			else setStrength(FemaleStrength);
		setMale(male);
		setSpears(0);
		setStones(0);
		setActiveWeapon(activeWeapon);
		setWalking(false);
		setThrowing(false);
	}

   

	public Player(float x, float y, float z, int speed, int size, String s, boolean male, int aciveWeapon)
	{
		setLocation(x,y,z);
		if (male) setSpeed(MaleSpeed);
			else setSpeed(FemaleSpeed);
		setObjectName(s);
		setLife(Life);
		setScore(0);
		setAlive(true);
		setStatusStr(".  Find weapon(stones and spears) and kill rivals.");
		setDispStr();
		if (male )setStrength(MaleStrength);
			else setStrength(FemaleStrength);
		setMale(male);
		setSpears(0);
		setStones(0);
		setActiveWeapon(activeWeapon);
		setWalking(false);
		setThrowing(false);
	}

   
   public void setSize(float size)
	{
		Player.size = size;
		return;
	}

   public void setLife(int life2) {
		this.life = life2;
		return;
	}
   
   public void setScore(int score) {
		this.score = score;
	}
   
   public int getLife()
   {return life;}

   	
	public float getSize ()
	{	return size;}

	public int getScore() {
		return score;
	}

	

	public String getDispStr() {
		return dispStr;
	}

	public void setDispStr(String dispStr) {
		this.dispStr = dispStr;
	}
	
	public void setDispStr() {
		String scoreStr = Integer.toString (score);
		String lifeStr = Integer. toString(life);
		String spearsStr = Integer. toString(spears);
		String stonesStr = Integer. toString(stones);
		String weaponStr;
		switch (activeWeapon) {
         case 1: weaponStr = "Stone";
                  break;
         case 2: weaponStr = "Spear";
                  break;
         default: weaponStr = "Hand";
         break;
		}
		this.dispStr = ".  Score = "+scoreStr+".  Life = "+lifeStr+". Spears = "+spearsStr+". Stones = "+stonesStr+". Weapon: "+weaponStr+"\n     "+statusStr;
	}
		
	public String getStatusStr ()
	{return statusStr;}
	
	public void setStatusStr (String statusStr)
	{
		this.statusStr = statusStr;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public void incScore(int i)
	{this.score+=i;}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public boolean isMale() {
		return male;
	}

	public void setMale(boolean male) {
		this.male = male;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float f) {
		this.speed = f;
	}

	public int getSpears() {
		return spears;
	}

	public void setSpears(int spears) {
		this.spears = spears;
	}

	public int getStones() {
		return stones;
	}

	public void setStones(int stones) {
		this.stones = stones;
	}

	public int getActiveWeapon() {
		return activeWeapon;
	}

	public void setActiveWeapon(int activeWeapon) {
		this.activeWeapon = activeWeapon;
	}

	public boolean isWalking() {
		return walking;
	}

	public void setWalking(boolean walking) {
		this.walking = walking;
	}

	public boolean isThrowing() {
		return throwing;
	}

	public void setThrowing(boolean throwing) {
		this.throwing = throwing;
	}

}

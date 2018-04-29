package a1;

public class Spear extends GameObject {

	private float speed;
	private boolean picked;
	public static float size = 1.0f;
	
	public Spear(String spearfile) {
		setLocation();
		setObjectName(spearfile);
		speed = 0.0f;
		picked = false;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getSize()
	{return size;}
	
	public void setSize (float size)
	{Stone.size = size;}

	public boolean isPicked() {
		return picked;
	}

	public void setPicked(boolean picked) {
		this.picked = picked;
	}

}

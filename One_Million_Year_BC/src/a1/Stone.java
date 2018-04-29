package a1;

public class Stone extends GameObject {
	
	private float speed;
	private boolean picked;
	public static float size = 1.0f;
	

	public Stone(String stonefile) {
		setLocation();
		setObjectName(stonefile);
		speed = 0.0f;
		picked = false;
		setSize(size);
	}


	public float getSpeed() {
		return speed;
	}
	
	public float getSize()
	{return size;}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public void setSize (float size)
	{Stone.size = size;}


	public boolean isPicked() {
		return picked;
	}


	public void setPicked(boolean picked) {
		this.picked = picked;
	}

}

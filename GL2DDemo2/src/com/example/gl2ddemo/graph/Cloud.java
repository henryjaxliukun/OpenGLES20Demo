package com.example.gl2ddemo.graph;

public class Cloud extends Image_2D implements IMovable{

	public Cloud(float x, float z, TextureRect rect) {
		super(x, z, 0, rect);
		// TODO Auto-generated constructor stub
	}

	int moveDirection=1;
	float speed_unit=0.01f;
	int speed=1;
	public static int border=60;
	
	@Override
	public void moveToNextPosition() {
		// TODO Auto-generated method stub
		if(x>border){
			x=-border;
		}
		x+=speed_unit*speed*moveDirection;
	}

	@Override
	public void setSpeed(int speed) {
		// TODO Auto-generated method stub
		this.speed=speed;
	}

	@Override
	public void setDirection(boolean isRight) {
		// TODO Auto-generated method stub
		if(isRight){
			moveDirection=1;
		}else{
			moveDirection=-1;
		}
	}
}

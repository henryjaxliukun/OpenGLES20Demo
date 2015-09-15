package com.example.gl2ddemo.graph.two_d;

import com.example.gl2ddemo.graph.IMovable;
import static com.example.gl2ddemo.surfaceview.MySurfaceView.*;

public class Cloud extends Image_2D implements IMovable{

	public Cloud(float x, float z, TextureRect rect,int texId) {
		super(x, z, 0, rect ,texId);
		// TODO Auto-generated constructor stub
	}

	int moveDirection=1;
	float speed_unit=0.01f;
	int speed=1;
	
	@Override
	public void moveToNextPosition() {
		// TODO Auto-generated method stub
		if(x>SURFACE_BORDER_X){
			x=-SURFACE_BORDER_X;
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

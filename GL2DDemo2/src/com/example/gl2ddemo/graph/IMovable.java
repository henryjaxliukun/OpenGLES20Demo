package com.example.gl2ddemo.graph;

public interface IMovable {

	/**
	 * 移动到下一个位置
	 */
	public void moveToNextPosition();
	
	/**
	 * 设置速度
	 * @param speed
	 */
	public void setSpeed(int speed);
	
	/**
	 * 设置方向
	 * @param isRight
	 */
	public void setDirection(boolean isRight);
}

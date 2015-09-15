package com.example.gl2ddemo.graph.two_d;

import static com.example.gl2ddemo.surfaceview.MySurfaceView.cx;
import static com.example.gl2ddemo.surfaceview.MySurfaceView.cz;

import com.example.gl2ddemo.util.MatrixState;

//单个的图片类
public class Image_2D implements Comparable<Image_2D>{
	public float x;
	public float z;
	public float yAngle;
	TextureRect rect;
	int texId;

	public Image_2D(float x, float z, float yAngle, TextureRect rect,int texId) {
		this.x = x;
		this.z = z;
		this.yAngle = yAngle;
		this.rect = rect;
		setTextureId(texId);
	}
	
	public void setTextureId(int texId){
		this.texId=texId;
	}
	

	public void drawSelf() {
		MatrixState.pushMatrix();
		MatrixState.translate(x, 0, z);
		MatrixState.rotate(yAngle, 0, 1, 0);
		rect.drawSelf(texId);
		MatrixState.popMatrix();
	}

	// 把图片朝向摄像机位置
	public void calculateBillboardDirection() {
		float xspan = x - cx;
		float zspan = z - cz;

		if (zspan <= 0) {
			yAngle = (float) Math.toDegrees(Math.atan(xspan / zspan));
		} else {
			yAngle = 180 + (float) Math.toDegrees(Math.atan(xspan / zspan));
		}
	}

	@Override
	public int compareTo(Image_2D another) {
		// 重写的比较两个树木离摄像机距离的方法
		float xs = x - cx;
		float zs = z - cz;

		float xo = another.x - cx;
		float zo = another.z - cz;

		float disA = (float) Math.sqrt(xs * xs + zs * zs);
		float disB = (float) Math.sqrt(xo * xo + zo * zo);

		return ((disA - disB) == 0) ? 0 : ((disA - disB) > 0) ? -1 : 1;
	}
}

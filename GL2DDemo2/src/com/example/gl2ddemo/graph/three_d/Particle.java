package com.example.gl2ddemo.graph.three_d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.example.gl2ddemo.graph.IGraph;
import com.example.gl2ddemo.util.MatrixState;
import com.example.gl2ddemo.util.ShaderUtil;
import static com.example.gl2ddemo.surfaceview.MySurfaceView.*;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

public class Particle implements IGraph{

	private FloatBuffer mVelocityBuffer;//顶点速度数据缓冲
    float scale;	//点尺寸
    
    String mVertexShader;	//顶点着色器    	 
    String mFragmentShader;	//片元着色器
    
    int mProgram;			//自定义渲染管线着色器程序id
    int muMVPMatrixHandle;	//总变换矩阵引用   
    int uPointSizeHandle;	//顶点尺寸参数引用
    int uColorHandle;		//顶点颜色参数引用
    int uTimeHandle;		//顶点颜色参数引用
    int vCount=1;

    int maVelocityHandle; 	//顶点速度属性引用 
    
    //用于计算时间
    float timeLive=0;
    long timeStamp=0;
	boolean isRandom;
	
    
    public Particle(GLSurfaceView mv,float scale,int count,boolean isRandom) {
    	this.scale=scale;
    	this.vCount=count;
    	this.isRandom=isRandom;
    	initVertexData();
    	initShader(mv);
	}
    
	@Override
	public void initVertexData() {
		float[] velocity=new float[vCount*3];
		if(isRandom){
			//随机生成坐标点
			for(int i=0;i<vCount;i++){
	        	velocity[i*3]=(float) (Math.random()*SURFACE_BORDER_X*3)-SURFACE_BORDER_X;
	        	velocity[i*3+1]=(float) (Math.random()*SURFACE_BORDER_Y*3-SURFACE_BORDER_Y);
	        	velocity[i*3+2]=(float)(Math.random()*SURFACE_BORDER_Z);
	        }
		}
        //创建顶点速度数据缓冲
        ByteBuffer vbb = ByteBuffer.allocateDirect(velocity.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVelocityBuffer = vbb.asFloatBuffer();//转换为int型缓冲
        mVelocityBuffer.put(velocity);//向缓冲区中放入顶点坐标数据
        mVelocityBuffer.position(0);//设置缓冲区起始位置
	}

	@Override
	public void initShader(GLSurfaceView mv) {
		//加载顶点着色器的脚本内容       
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_yh.sh", mv.getResources());
        //加载片元着色器的脚本内容
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_yh.sh", mv.getResources());  
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点速度属性引用id  
        maVelocityHandle = GLES20.glGetAttribLocation(mProgram, "aVelocity");        
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix"); 
        //获取顶点尺寸参数引用
        uPointSizeHandle = GLES20.glGetUniformLocation(mProgram, "uPointSize"); 
        //获取顶点颜色参数引用
        uColorHandle = GLES20.glGetUniformLocation(mProgram, "uColor"); 
        //获取顶点颜色参数引用
        uTimeHandle=GLES20.glGetUniformLocation(mProgram, "uTime"); 
	}

	public void drawSelf(){
		drawSelf(0);
	}
	
	@Override
	public void drawSelf(int texId) {
		long currTimeStamp=System.nanoTime()/1000000;
    	if(currTimeStamp-timeStamp>=10){
    		timeLive+=0.02f;
    		timeStamp=currTimeStamp;
    	}
		
		//制定使用某套着色器程序
   	    GLES20.glUseProgram(mProgram);
        //将最终变换矩阵传入着色器程序
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);  
        //将顶点尺寸传入着色器程序
        GLES20.glUniform1f(uPointSizeHandle, scale);
        //将时间传入着色器程序
        GLES20.glUniform1f(uTimeHandle, timeLive);    
        //将顶点颜色传入着色器程序
        GLES20.glUniform3fv(uColorHandle, 1, new float[]{0.5647f,0.5412f,0.4784f}, 0);
        //传入顶点速度数据    
        GLES20.glVertexAttribPointer(
        		maVelocityHandle,   
        		3, 
        		GLES20.GL_FLOAT, 
        		false,
                3*4, 
                mVelocityBuffer   
        );
        //允许顶点位置数据数组
        GLES20.glEnableVertexAttribArray(maVelocityHandle);         
        //绘制点    
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, vCount); 
	}

}

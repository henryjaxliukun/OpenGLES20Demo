package com.example.gl2ddemo.surfaceview;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.gl2ddemo.R;
import com.example.gl2ddemo.graph.Cloud;
import com.example.gl2ddemo.graph.CloudGroup;
import com.example.gl2ddemo.graph.Image_2D;
import com.example.gl2ddemo.graph.TextureRect;
import com.example.gl2ddemo.util.MatrixState;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

public class MySurfaceView extends GLSurfaceView
{
	public static final float UNIT_SIZE=1f;
	static float direction=0;//视线方向
    public static float cx=0;//摄像机x坐标
    public static float cz=30;//摄像机z坐标
    
	SceneRenderer mRender;
	
	public MySurfaceView(Context context)
	{
		super(context);
		this.setEGLContextClientVersion(2); //设置使用OPENGL ES2.0
        mRender = new SceneRenderer();	//创建场景渲染器
        setRenderer(mRender);				//设置渲染器		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染 
	}
	
	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
		Cloud originalImage;
		int cloudTextureId;
		
		Image_2D background;
		int backgroundTextureId;
		
		CloudGroup group;
		
		MoveCloudThread moveThread;
		
		@Override
		public void onDrawFrame(GL10 gl)
		{
			//清除深度缓冲与颜色缓冲
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            
            MatrixState.pushMatrix();
            MatrixState.translate(0, -2, 0);
            background.drawSelf();
            MatrixState.popMatrix();
            
            //开启混合
            GLES20.glEnable(GLES20.GL_BLEND);
            //设置混合因子
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            
            MatrixState.pushMatrix();
            MatrixState.translate(0, -2, 0);

            originalImage.drawSelf();
            group.drawSelf(cloudTextureId);
            
            MatrixState.popMatrix();
            //关闭混合
            GLES20.glDisable(GLES20.GL_BLEND);    
		}
		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height)
		{
			//设置视窗大小及位置 
        	GLES20.glViewport(0, 0, width, height); 
        	//计算GLSurfaceView的宽高比
            float ratio = (float) width / height;
            //调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 100);
            //调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(cx,0,cz,0,0,0,0f,1.0f,0.0f);
		}
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config)
		{
			//设置屏幕背景色RGBA
            GLES20.glClearColor(0.0f,0.0f,0.0f,1.0f);
            //打开深度检测
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            MatrixState.setInitStack();
            
            cloudTextureId=initTexture(R.drawable.cloud);
            backgroundTextureId=initTexture(R.drawable.bg);
            
            originalImage=new Cloud(0, 0, new TextureRect(MySurfaceView.this,5,5,20),cloudTextureId);
            background=new Image_2D(0, -50, 0, new TextureRect(MySurfaceView.this, 200, 100,-100),backgroundTextureId);
            group=new CloudGroup(getCloudsList(cloudTextureId));
            
            moveThread=new MoveCloudThread();
            moveThread.start();
		}
    }
	
	//生成纹理的id
	public int initTexture(int drawableId)
	{
		//生成纹理ID
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          //产生的纹理id的数量
				textures,   //纹理id的数组
				0           //偏移量
		);      
		int textureId=textures[0];    
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);
        
        //通过输入流加载图片
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally 
        {
            try 
            {
                is.close();
            }
            catch(IOException e) 
            {
                e.printStackTrace();
            }
        }
        
        //实际加载纹理
        GLUtils.texImage2D
        (
        		GLES20.GL_TEXTURE_2D,   //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
        		0, 					  //纹理的层次，0表示基本图像层，可以理解为直接贴图
        		bitmapTmp, 			  //纹理图像
        		0					  //纹理边框尺寸
        );
        bitmapTmp.recycle(); 		  //纹理加载成功后释放图片
        return textureId;
	}

	public List<Cloud> getCloudsList(int texId) {
		// TODO Auto-generated method stub
		TextureRect rect=new TextureRect(this, 5, 5, 15);
		List<Cloud> list=new ArrayList<Cloud>();
		//随机生成速度和位置
		Random random=new Random();
		for(int i=0;i<10;i++){
			Cloud c=new Cloud(random.nextInt()%Cloud.border, i, rect,texId);
			int s;
			do{
				s=Math.abs(random.nextInt()%3);
			}while(s==0);
			c.setSpeed(s);
			list.add(c);
		}
		return list;
	}
	
	class MoveCloudThread extends Thread{
		boolean moveFlag=true;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(moveFlag){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mRender.originalImage.moveToNextPosition();
				mRender.group.moveToNextPosition();
			}
		}
		public void stopMoving(){
			moveFlag=false;
		};
	}
	
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		mRender.moveThread.stopMoving();
		super.onDetachedFromWindow();
	}
}
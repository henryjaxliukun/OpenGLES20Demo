package com.example.gl2ddemo.graph.two_d;

import static com.example.gl2ddemo.surfaceview.MySurfaceView.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.example.gl2ddemo.graph.IGraph;
import com.example.gl2ddemo.util.MatrixState;
import com.example.gl2ddemo.util.ShaderUtil;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

/**
 * 纹理矩形
 * @author Jax
 *
 */
public class TextureRect implements IGraph 
{
	
	int mProgram;//自定义渲染管线程序id
    int muMVPMatrixHandle;//总变换矩阵引用id
    int maPositionHandle; //顶点位置属性引用id  
    int maTexCoorHandle; //顶点纹理坐标属性引用id  
    
	FloatBuffer   mVertexBuffer;//顶点坐标数据缓冲
	FloatBuffer   mTexCoorBuffer;//顶点纹理坐标数据缓冲
    int vCount=0;   
    
    int width=1;
    int height=1;
    float bottom=0;
    
    public TextureRect(GLSurfaceView mv,int width,int height,float bottom)
    {
    	this.width=width;
    	this.height=height*2;
    	this.bottom=bottom;
    	initVertexData();
    	initShader(mv);
    }
    //初始化顶点数据的方法
    public void initVertexData()
    {
    	vCount=6;
        float vertices[]=new float[]
        {
        	-UNIT_SIZE*width,bottom,0,
            UNIT_SIZE*width,bottom,0,
            UNIT_SIZE*width,UNIT_SIZE*height+bottom,0,
            
            UNIT_SIZE*width,UNIT_SIZE*height+bottom,0,
            -UNIT_SIZE*width,UNIT_SIZE*height+bottom,0,
            -UNIT_SIZE*width,bottom,0,
        };
        //创建顶点坐标数据缓冲
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        
        float[] texcoor=new float[]
        {
        	0,1,    1,1,   
        	1,0,	1,0,   
        	0,0,    0,1
        };
        ByteBuffer tbb = ByteBuffer.allocateDirect(texcoor.length*4);
        tbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTexCoorBuffer = tbb.asFloatBuffer();//转换为Float型缓冲
        mTexCoorBuffer.put(texcoor);//向缓冲区中放入顶点坐标数据
        mTexCoorBuffer.position(0);//设置缓冲区起始位置        
    }
    //初始化shader
    public void initShader(GLSurfaceView mv)
    {
    	//加载顶点着色器的脚本内容
        String mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_2d.sh", mv.getResources());
        //加载片元着色器的脚本内容
        String mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_2d.sh", mv.getResources());  
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点纹理坐标属性引用id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
    }
    //主绘制方法
    public void drawSelf(int texId)    
    {
    	//指定使用某套shader程序
   	 	GLES20.glUseProgram(mProgram); 
        //将最终变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
        //传送顶点位置数据
        GLES20.glVertexAttribPointer  
        (
        	maPositionHandle,   
        	3, 
        	GLES20.GL_FLOAT, 
        	false,
            3*4,   
            mVertexBuffer
        );       
        //传送顶点纹理坐标数据
        GLES20.glVertexAttribPointer  
        (
       		maTexCoorHandle, 
        	2, 
        	GLES20.GL_FLOAT, 
        	false,
            2*4,   
            mTexCoorBuffer
        );   
        //允许顶点位置、纹理坐标数据数组
        GLES20.glEnableVertexAttribArray(maPositionHandle);  
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
        
        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        
        //绘制纹理矩形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    }
    
}

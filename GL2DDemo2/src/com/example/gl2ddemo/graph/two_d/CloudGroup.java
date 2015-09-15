package com.example.gl2ddemo.graph.two_d;

import static com.example.gl2ddemo.surfaceview.MySurfaceView.SURFACE_BORDER_X;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.example.gl2ddemo.util.TextureUtil;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * 一组云，方便统一调用
 * @author Jax
 *
 */
public class CloudGroup {

	List<Cloud> imageList;
	
	public CloudGroup(Context ctx,GLSurfaceView sv){
		imageList=new ArrayList<Cloud>();
		int[] cloudTextureIds = new int[4];
		for(int i=0;i<4;i++){
			int id=ctx.getResources().getIdentifier(ctx.getPackageName()+":drawable/"+"cloud"+(i+1), null, null);
			cloudTextureIds[i]=TextureUtil.initTexture(id, ctx);
        }
		Random random=new Random();
		TextureRect rect=new TextureRect(sv,60,40,-20);
		for(int i=0;i<cloudTextureIds.length*2;i++){
			Cloud c=new Cloud(random.nextInt()%SURFACE_BORDER_X, i-4, rect,cloudTextureIds[i%cloudTextureIds.length]);
			int s;
			do{
				s=Math.abs(random.nextInt()%5+2);
			}while(s==0);
			c.setSpeed(s);
			imageList.add(c);
		}
	}
	
	public CloudGroup(List<Cloud> list){
		if(list!=null){
			imageList=list;
		}else{
			imageList=new ArrayList<Cloud>();
		}
	}
	
	public void addImage(Cloud i){
		imageList.add(i);
	}
	
	public void addAllImage(List<Cloud> list){
		imageList.addAll(list);
	}
	
	public void removeImage(Cloud i){
		imageList.remove(i);
	}
	
	public void removeAllImage(){
		imageList.clear();
	}
	
	public void calculateBillboardDirection()
    {
    	//计算列表中每个树木的朝向
    	for(int i=0;i<imageList.size();i++)
    	{
    		imageList.get(i).calculateBillboardDirection();
    	}
    }
    
    public void drawSelf()
    {//绘制列表中的每个树木
    	for(int i=0;i<imageList.size();i++)
    	{
    		imageList.get(i).drawSelf();
    	}
    }
    
    public void sortList(){
    	Collections.sort(imageList);
    }
    
    public void moveToNextPosition(){
    	for(Cloud c:imageList){
    		c.moveToNextPosition();
    	}
    }
}

package com.example.gl2ddemo.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 一组云，方便统一调用
 * @author Jax
 *
 */
public class CloudGroup {

	List<Cloud> imageList;
	
	public CloudGroup(){
		imageList=new ArrayList<Cloud>();
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
    
    public void drawSelf(int texId)
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

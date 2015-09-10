package com.example.gl2ddemo.graph;

import android.opengl.GLSurfaceView;

public interface IGraph {

	/**
	 * 初始化坐标点数据
	 * @param vertices 顶点坐标
	 * @param normals 法向量
	 * @param texCoors 纹理坐标
	 */
	public void initVertexData();
	
	/**
	 * 初始化着色器
	 * @param mv
	 */
	public void initShader(GLSurfaceView mv);
	
	/**
	 * 绘制自身
	 * @param texId
	 */
	public void drawSelf(int texId);
}

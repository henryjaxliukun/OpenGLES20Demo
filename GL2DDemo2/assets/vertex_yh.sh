uniform mat4 uMVPMatrix; //总变换矩阵
uniform float uPointSize;//点尺寸
uniform float uTime;
attribute vec3 aVelocity;  //顶点速度
void main()     
{
   float currTime=mod(uTime,6);
   float px=aVelocity.x;
   float py=aVelocity.y-currTime*60;
   float pz=aVelocity.z;
   //根据总变换矩阵计算此次绘制此顶点位置                         		
   gl_Position = uMVPMatrix * vec4(px,py,pz,1); 
   //设置粒子尺寸
   gl_PointSize=uPointSize;  
}
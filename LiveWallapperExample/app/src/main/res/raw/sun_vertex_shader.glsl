uniform mat4 u_Matrix;      
uniform float u_size;
uniform float u_Angle;  
uniform float u_reverse;  


attribute vec4 a_Position;   
attribute vec3 a_Color;
attribute float a_Time;
attribute float a_Reverse;
varying vec3 v_Color;

vec3 astroid(){
        float t=u_Angle;
        float a=30.0;
        float xB=a_Position.x;
        float yB=a_Position.y;
        float zB=a_Position.z;
        yB=a*pow(sin(t),3.0)+yB;
         return vec3(xB,yB,zB);   
}

vec3 cycloid(){
     float t=u_Angle-a_Time;
        float a=0.5;
        float h=0.5;
        float xB= ((a*t - h*sin(t))*-0.7 +1.0)* a_Position.x;
        float yB=(a-h*cos(t))+a_Position.y;
     
        return vec3(xB,yB,a_Position.z);   
}

mat4 rotationX( in float angle ) {
	return mat4(    1.0,    0,              0,              0,
		        0,      cos(angle),     -sin(angle),    0,
	                0,      sin(angle),     cos(angle),     0,
		        0,      0,              0,              1);
}

mat4 rotationY( in float angle ) {
	return mat4(	cos(angle),	0,	sin(angle),	0,
			0,		1.0,	0,	        0,
			-sin(angle),	0,	cos(angle),	0,
			0, 		0,	0,	        1);
}


vec4 getPos(){
        return vec4(cycloid(),1.0);
}

void main()                  
{                            
    v_Color = a_Color;
    gl_Position = u_Matrix *getPos(); 
    gl_PointSize = u_size;    
}                            
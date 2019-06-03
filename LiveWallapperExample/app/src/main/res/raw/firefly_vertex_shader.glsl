const float DEG_TO_RAD = 3.141592653589793 / 180.0;
const float PI = 3.141592653589793;

uniform mat4 u_Matrix;      
uniform float u_Time;
uniform float u_Angle;
uniform float u_Increase;

attribute vec4 a_Position;   
attribute vec3 a_Color;
attribute vec3 a_DirectionVector;
attribute float a_size;     
attribute float a_Time;
attribute float a_Phi;
attribute float a_Speed;
attribute float a_Type;

//  attribute vec2 a_p2;
//  attribute vec2 a_p3;
//  attribute vec2 a_p4;


varying vec3 v_Color;

// vec3 calulatorpointBezier(){
//     float t=u_Time;
//     float xB =
//             a_p1.x + 3.0 * t * (a_p2.x - a_p1.x) + 3.0 * (t * t) * (a_p1.x + a_p3.x - (2.0 * a_p2.x)) + (t * t * t) * (a_p4.x - a_p1.x + 3.0 * a_p2.x - 3.0 * a_p3.x);
//     float yB =
//             a_p1.y + 3.0 * t * (a_p2.y - a_p1.y) + 3.0 * (t * t) * (a_p1.y + a_p3.y - (2.0 * a_p2.y)) + (t * t * t) * (a_p4.y - a_p1.y + 3.0 * a_p2.y - 3.0 * a_p3.y);

//     return vec3(xB,yB,a_Position.z);
// }

vec3 hinhnonla(){
        float t=u_Time;
        float a=15.0;
        float xB=a_Position.x;
        float yB=a_Position.y;
        float zB=a_Position.z;
        xB= (3.0 *a*t)/(1.0+t*t*t)+xB;
         if(a_Type==0.0){
                yB=(3.0 * a *t*t)/(1.0+ t*t*t)+yB;
         }
         else{
                zB=(3.0 * a *t*t)/(1.0+ t*t*t)+zB;
         }
        return vec3(xB,yB,zB);
}

vec3 elip(){
        float t=u_Angle*a_Speed;
        float a=1.0;
        float b=2.5;
        float xB=a_Position.x;
        float yB=a_Position.y;
        float zB=a_Position.z;
        xB= a*cos(t)+xB;
        if(a_Type==0.0){
                yB=b*sin(t)+yB;
        }else{
                zB=b*sin(t)+zB;
         }
        return vec3(xB,yB,zB);   
}

vec3 astroid(){
        float t=u_Angle;
        float a=2.0;
        float xB=a_Position.x;
        float yB=a_Position.y;
        float zB=a_Position.z;
        xB=a*pow(cos(t),3.0)+xB;
          if(a_Type==0.0){
                yB=a*pow(sin(t),3.0)+yB;
        }else{
                zB=a*pow(sin(t),3.0)+zB;
        }
         return vec3(xB,yB,zB);   
}

vec3 cardioid(){
        float t=u_Angle;
        float a=2.0;
        float xB=a_Position.x;
        float yB=a_Position.y;
        float zB=a_Position.z;
        xB=a*(2.0*cos(t)-cos(2.0*t))+xB;
        // if(a_Type==0.0){
                yB=a*(2.0*sin(t)-sin(2.0*t))+yB;
        // }else{
                // zB=a*pow(sin(t),3.0)+zB;
        // }
         return vec3(xB,yB,0.0);   
}

vec3 epitrochoid(){
     float t=u_Time;
        float a=5.0;
        float b=3.0;
        float c=5.0;
        float xB= (a+b)*cos(a/b+1.0)*t+a_Position.x;
        float yB=(a+b)*sin(t)-c*sin(a/b+1.0)*t+a_Position.y;
        return vec3(xB,yB,a_Position.z);   
}

vec3 cycloid(){
     float t=u_Time;
        float a=5.0;
        float h=5.0;
        float xB= a*t - h*sin(t)+a_Position.x;
        float yB=a-h*cos(t)+a_Position.y;
        return vec3(xB,yB,a_Position.z);   
}

vec3 polynomial(){
     float t=u_Time;
        float xB= a_Position.x;
        float yB=xB*xB*xB*xB-8.0*xB*xB;
        return vec3(xB,yB,a_Position.z);   
}

vec3 getPos(){
        // if(a_TypeCurve==0.0){
        //         return hinhnonla();
        // }
        return elip();
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

mat4 rotationZ( in float angle ) {
	return mat4(	cos(angle),	-sin(angle),	0,	0,
			sin(angle),	cos(angle),	0,	0,
			0,		0,		1,	0,
			0,		0,		0,	1);
}

float roundNumber(float n1, float n2){
        float result = fract(n1) * fract(n2);
        return result;
}

float sizePoint(){
        float check = a_Type + u_Increase;
        float s=0.0;
        if(a_Type==0.0){
                s= a_size*(u_Time-a_Time);
        } else{
                s=a_size*(3.0-(u_Time-a_Time))-20.0;
        }
        return s;
}

void main()                  
{                            
        v_Color = a_Color;
        vec4 currentPos = vec4(getPos(),1.0);
        float phi=a_Phi;
        // gl_Position= u_Matrix * a_Position;
        gl_Position = u_Matrix * rotationX(phi) * rotationY(phi) * currentPos;
        float s=(u_Time-a_Time)*a_size;
        gl_PointSize =sizePoint();    
       
}                            
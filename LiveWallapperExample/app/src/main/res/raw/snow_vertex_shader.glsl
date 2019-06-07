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

varying vec3 v_Color;

vec3 elip(){
    float t=u_Angle*a_Speed;
    float a=1.0;
    float b=2.5;
    float xB=a_Position.x;
    float yB=a_Position.y;
    float zB=a_Position.z;
    xB= a*cos(t)+xB;
    if (a_Type==0.0){
        yB=b*sin(t)+yB;
    } else {
        zB=b*sin(t)+zB;
    }
    return vec3(xB, yB, zB);
}

vec3 dropVertical() {
    float t=u_Angle*a_Speed;
    float speed = a_Speed;
    float xB=a_Position.x;
    float yB=a_Position.y;
    float zB=a_Position.z;

    yB = yB + speed*t;
    return vec3(xB, yB, zB);
}

vec3 getPos(){
    return elip();
}

mat4 rotationX(in float angle) {
    return mat4(1.0, 0, 0, 0,
    0, cos(angle), -sin(angle), 0,
    0, sin(angle), cos(angle), 0,
    0, 0, 0, 1);
}

mat4 rotationY(in float angle) {
    return mat4(cos(angle), 0, sin(angle), 0,
    0, 1.0, 0, 0,
    -sin(angle), 0, cos(angle), 0,
    0, 0, 0, 1);
}

mat4 rotationZ(in float angle) {
    return mat4(cos(angle), -sin(angle), 0, 0,
    sin(angle), cos(angle), 0, 0,
    0, 0, 1, 0,
    0, 0, 0, 1);
}

float roundNumber(float n1, float n2){
    float result = fract(n1) * fract(n2);
    return result;
}

float sizePoint(){
    float check = a_Type + u_Increase;
    float s=0.0;
    if (a_Type==0.0){
        s= a_size*(u_Time-a_Time);
    } else {
        s=a_size*(2.0-(u_Time-a_Time))-20.0;
    }
    return s;
}

void main()
{
    v_Color = a_Color;
    vec4 currentPos = vec4(getPos(), 1.0);
    float phi = a_Phi;
    // gl_Position= u_Matrix * a_Position;
    gl_Position = u_Matrix * rotationX(phi) * rotationY(phi) * currentPos;
    float s = (u_Time-a_Time)*a_size;
    gl_PointSize = sizePoint();

}

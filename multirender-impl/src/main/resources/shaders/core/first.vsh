#version 330 core

layout (location = 0) in vec3 aPos;   // the position variable has attribute position 0
layout (location = 1) in vec3 aColor; // the color variable has attribute position 1

uniform float offset;

out vec4 pos; // output a color to the fragment shader

void main() {
    gl_Position = vec4(-aPos.x + offset, -aPos.y, -aPos.z, 1.0);
//    ourColorOut = mix(aColor, ourColor.xyz, .7f); // set ourColor to the input color we got from the vertex data
    pos = gl_Position;
}
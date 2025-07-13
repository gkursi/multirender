#version 330 core

layout (location = 0) in vec3 aPos;   // the position variable has attribute position 0
layout (location = 1) in vec3 aColor; // the color variable has attribute position 1
layout (location = 2) in vec2 aTexCoord;

uniform float offset;
uniform sampler2D texture1;
uniform sampler2D texture2;

out vec3 color; // output a color to the fragment shader
out vec2 TexCoord;

void main() {
    gl_Position = vec4(aPos, 1.0);
//    ourColorOut = mix(aColor, ourColor.xyz, .7f); // set ourColor to the input color we got from the vertex data
    color = aColor;
    TexCoord = aTexCoord;
}
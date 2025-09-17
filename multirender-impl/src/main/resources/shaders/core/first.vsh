#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;

out vec2 TexCoord;

uniform mat4 transform;
uniform sampler2D texture1;
uniform sampler2D texture2;

// coordinate system
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    gl_Position = view * model * transform * vec4(aPos, 1.0f);
//    gl_Position = gl_Position * 0.0000001 + vec4(aPos, 1.0f);
    TexCoord = vec2(aTexCoord.x, aTexCoord.y);
}
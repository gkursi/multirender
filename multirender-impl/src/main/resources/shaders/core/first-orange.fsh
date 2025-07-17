#version 330 core

in vec3 color;
in vec2 TexCoord;

out vec4 FragColor;

uniform float offset;
uniform sampler2D texture1;
uniform sampler2D texture2;

void main()
{
    FragColor = mix(texture(texture1, TexCoord), texture(texture2, vec2(-TexCoord.x, -TexCoord.y)), 0.5f);
//    FragColor = texture(texture1, TexCoord);
}
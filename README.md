# MultiRender
OpenGL renderer written in kotlin

### What is this for
This is a way for me to learn kotlin syntax, I might
turn it in to something more, but not any time soon. 
Since I'm still quite new to Kotlin, the code will
probably be bad for a while. 

This is also a way to hopefully avoid having to
rewrite almost all render code in my mods every time Mojang
changes their rendering system.
### Structure
This project is currently split up in to 2 parts:
- **Multirender-API** - the api
- **Multirender-impl** - an implementation of the api using LWJGL/GLFW/OpenGL

Eventually I'll add a second and third implementation, 
which will be tweaked versions of the first
implementation made specifically for Minecraft 
1.20.4 (?) - 1.21.1 and 1.21.3 - 1.21.7 (or 
whichever is the latest, unless mojang decides 
to rewrite their renderer again)

### Credit
I'm writing this as I'm following the awesome tutorial at https://learnopengl.com/ 
(really, it's very good, even with it being at least 11 years old)
# MultiRender
OpenGL renderer written in kotlin,
compatible with Minecraft.

## Structure
This project is currently split up in to 2 parts:
- **Multirender-API** - the api
- **Multirender-impl** - an (incomplete) implementation of the api using just LWJGL/GLFW/OpenGL
- **Multirender-mc<version>** - an implementation of the api for minecraft

## Usage
- Include both `multirender-api` and the implementation you want to use in your project 
- Use the `API` object to access the api
   - `API.base` - base class for the api
   - `Api.mousehandler` / `API.keyboardHandler` - receive/spoof user input
   - `API.textureHandler` - create and manage textures (currently broken)
   - `API.context2d` - 2d rendering context
- Refrain from using your own implementation of the api together with the provided implementations

## Roadmap
- Add basic elements to 2d context (quads, rounded quads, circles, lines, ...)
- Fix textures
- Add 3d context

## Credit
Base implementation made with https://learnopengl.com/
# MultiRender
OpenGL renderer written in kotlin, compatible with Minecraft.

## Structure
- `multirender-api` - the api
- `multirender-base-..` - full api implementation for the specified version
- `multirender-extra-..` - optional extra dependencies for more features

## Usage
- Include both `multirender-api` and the implementation you want to use in your project 
- Use the `API` object to access the api
   - `base` - base class for the api
   - `events` - event bus, for usage [see this](https://github.com/gkursi/basalt)
   - `mousehandler` / `keyboardHandler` - receive/spoof user input
   - `textureHandler` - create and manage textures (currently broken)
   - `context2d` - 2d rendering context
- Refrain from using your own implementation of the api together with the provided implementations

## Roadmap
- Add basic elements to 2d context (quads, rounded quads, circles, lines, ...)
- Fix textures
- Add 3d context

## Credit
Base implementation made with https://learnopengl.com/
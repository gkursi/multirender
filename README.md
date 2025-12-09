# MultiRender
Minecraft-focused rendering library

## Structure
- `multirender-api` - the api
- `multirender-<version>` - implementation of the api in the specified minecraft version
  (e.g. `multirender-1-21-8`)
- `multirender-nanovg` - nanovg with [Kotlin DSL](https://kotlinlang.org/docs/type-safe-builders.html) syntax
  (not standalone, an api implementation is required)

## Usage
- Include both `multirender-api` and the implementation you want to use in your project 
- Use the `API` object to access the api
   - `base` - base class for the api
   - `events` - event bus, for usage [see this](https://github.com/gkursi/basalt)
   - `mousehandler` / `keyboardHandler` - receive/spoof user input
   - `textureHandler` - create and manage textures (currently broken)
   - `context2d` - 2d rendering context

## Roadmap
- clean up api
- 3d rendering

## Credit
- https://learnopengl.com/
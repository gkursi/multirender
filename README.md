# MultiRender
Minecraft-focused rendering utilities

## Structure
- `multirender-api` - the api
- `multirender-<version>` - implementation of the api in the specified minecraft version
- `multirender-nanovg` - nanovg with [Kotlin DSL](https://kotlinlang.org/docs/type-safe-builders.html) syntax
- `multirender-twm` - a tiling window manager for some reason

## Usage
- Include both `multirender-api` and the implementation you want to use in your project 
- Use the `API` object to access the api
   - `base` - base class for the api
   - `events` - event bus, for usage [see this](https://github.com/gkursi/basalt)
   - `mousehandler` / `keyboardHandler` - receive/spoof user input
   - `textureHandler` - create and manage textures
   - `context2d` - 2d rendering context
- All submodules require some api implementation to function properly
- [NanoVG usage](multirender-nanovg/README.md), [TWM usage](multirender-twm/README.md) 
## Roadmap
- 3d rendering

## Credit
- https://learnopengl.com/
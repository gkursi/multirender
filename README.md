# MultiRender
Minecraft-focused rendering utilities

> [!WARNING]
> This project is still in early development.
> Breaking changes may be introduced at any time without prior notice.

## Structure
- `multirender-api` - the api
- `multirender-<version>` - an implementation of the api
- `multirender-twm` - a tiling window manager
- `multirender-widget`
- `multirender-nanovg` - "kotlinified" nanovg

## Usage
- Include both `multirender-api` and the implementation you want to use in your project 
- Use the `API` object to access the api
- All submodules require some api implementation to function properly
- [NanoVG docs](multirender-nanovg/README.md), [TWM docs](multirender-twm/README.md)
## Roadmap
- 3d rendering
- fix textures
- fix shaders

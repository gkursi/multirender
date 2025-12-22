# MultiRender TWM
Abstract tiling window manager

## Usage
This module doesn't directly draw anything. Instead,
it delegates rendering to "backend" interfaces (`WMBackend`, `WindowBackend`, etc.).
However, it still depends on the multirender api to reduce
the amount of code required for implementing said interfaces.

All windows are managed by the `WindowManager`
object. Use `WindowManager#setBackend`
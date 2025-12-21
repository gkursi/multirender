# MultiRender NanoVG
This fully wraps the lwjgl bindings for NanoVG to add
Kotlin DSL-like syntax.
## Usage

### Obtaining the context
You can either use the global nanovg instance (recommended)
or create your own (see `NanoContext`)
- Create a handler for the `NanoRenderEvent`
```kotlin
object MyObject {
    @Handler
    fun onRender(event: NanoRenderEvent) {

    }
}
```
This event is fired every frame. The gl state is backed up before firing it and restore afterward.
- Initialize the manager and subscribe the handler
```kotlin
NanoState.init()
API.events.subscribe(MyObject)
```
This should be called before the window is created.
### Drawing shapes
To create a shape, add a `shape` block:
```kotlin
@Handler
fun onRender(event: NanoRenderEvent) {
    event.context.apply {
        shape {

        }
    }
}
```
This represents one NanoVG path with optional fill and stroke.
- Define the path inside a `path` block:
```kotlin
shape {
    path {
        rectangle(Vec2f.absolute(20f, 20f), Vec2f.absolute(200f, 200f))
        ellipse(Vec2f.absolute(50f, 50f), Vec2f.absolute(10f, 10f))
    }
}
```
- Define the fill/stroke with their respective blocks:
```kotlin
shape {
    path { 
        // ...
    }
    
    fill {
        paint = linearGradient(Vec2f.TOP_LEFT, Vec2f.BOTTOM_RIGHT, Color.red, Color.green)
    }
    
    stroke {
        paint = solid(Color.blue)
        width = 7f
    }
}
```
Omitting either of these will prevent them from being drawn.

### Coordinate system
There are 2 types of coordinates: relative and absolute.
Absolute coordinates are regular world space coordinates.
Relative coordinates are values between 0 and 1 that change 
based on the window size. Both types can be accessed trough `Vec2f`:
`Vec2f#absolute` and `Vec2f#relative`.
Paints and paths share coordinates.

### Paints
To reduce complexity, paints and colors have been merged.
Paints can either be a single solid color or one of three gradients:
`boxGradient`, `linearGradient`, `radialGradient`. 

### Minimal API implementation
Technically, if you avoid using relative coordinates and manage a context yourself,
you don't need to use an api implementation at all. However, a minimal api implementation
while still having all the features would require:
- `API.events`
- `API.base.getWindow()`
- `Window.getWidth()`
- `Window.getHeight()`
- `WindowCreateEvent`
- `WindowSizeChangeEvent`
- `PostRenderEvent`

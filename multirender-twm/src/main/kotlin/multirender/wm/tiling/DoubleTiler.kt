package multirender.wm.tiling

import multirender.wm.manager.Context
import multirender.wm.window.Window

object DoubleTiler : Tiler {
    override fun tile(
        windows: List<Window>,
        width: Float,
        height: Float,
        context: Context
    ) {
        val wm = context.backend
        val count = windows.size
        val even = count % 2 == 0
        val rows = if (count <= 2) 1 else 2
        val cols = Math.floorDiv(count, rows)

        val width = wm.getRemainingWidth() / cols
        val totalHeight = wm.getRemainingHeight()

        var offsetX = 0f

        for (col in 1..cols) { // x
            var offsetY = 0f
            val rows = if (even || col != cols || rows == 1) rows else (rows + 1)
            val height = totalHeight / rows

            for (row in 1..rows) { // y
                val window = windows[mapTile(row, col, count)]
                window.set(context, offsetX, offsetY, width, height)
                offsetY += height
            }

            offsetX += width
        }
    }

    /**
     * @return window index
     */
    fun mapTile(row: Int, col: Int, count: Int): Int =
        if (count <= 2) {
            col - 1
        } else {
            col * 2 + row - 3
        }
}
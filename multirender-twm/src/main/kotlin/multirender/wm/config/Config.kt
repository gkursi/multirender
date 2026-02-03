package multirender.wm.config

import multirender.wm.animation.Curve
import multirender.wm.tiling.Tiler
import multirender.wm.util.Alignment

interface Config {
    val moveCurve: Curve
    val resizeCurve: Curve
    val moveTime: Int
    val resizeTime: Int

    val barAlign: Alignment
    val barWidth: Float

    val outerGap: Float
    val innerGap: Float
    val tiler: Tiler
    val forceTile: Boolean // force all floating windows to tile
    val masterWidth: Float // width of the master window
}
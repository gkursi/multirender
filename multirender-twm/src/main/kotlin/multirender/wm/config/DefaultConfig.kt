package multirender.wm.config

import multirender.wm.util.Alignment
import multirender.wm.animation.curve.CubicBezier1d
import multirender.wm.tiling.MasterTiler
import multirender.wm.tiling.Tiler

object DefaultConfig : Config {
    // curves
    val myCurve = CubicBezier1d(0f, 1f) // linear

    // gaps
    override var innerGap = 2.5f
    override var outerGap = 4.0f

    // animation
    override var moveTime = 400
    override var moveCurve = myCurve

    override var resizeTime = 300
    override var resizeCurve = myCurve

    // bar
    override var barAlign = Alignment.TOP
    override var barWidth = 20f

    // tiling
    override var tiler: Tiler = MasterTiler
    override var forceTile = false // force all floating windows to tile
    override var masterWidth = 0.7f // width of the master window
}
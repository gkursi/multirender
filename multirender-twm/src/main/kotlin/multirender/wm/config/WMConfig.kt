package multirender.wm.config

import multirender.wm.util.Alignment
import multirender.wm.animation.curve.CubicBezier1d
import multirender.wm.tiling.DoubleTiler
import multirender.wm.tiling.MasterTiler

object WMConfig {
    // curves
    val myCurve = CubicBezier1d(0.3f, 1f)

    // gaps
    var windowGap = 5.0f
    var screenGap = 6.0f

    // animation
    var moveTime = 200
    var moveCurve = myCurve

    var resizeTime = 150
    var resizeCurve = myCurve

    // bar
    var barAlignment = Alignment.TOP
    var barWidth = 20f

    // tiling
    var tiler = MasterTiler
    var forceTile = false // force all floating windows to tile
    var masterWidth = 0.6f // width of the master window
}
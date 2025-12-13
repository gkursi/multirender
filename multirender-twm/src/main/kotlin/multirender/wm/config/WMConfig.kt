package multirender.wm.config

import multirender.wm.animation.Animation
import multirender.wm.animation.Ease

object WMConfig {
    // gaps
    var windowGap = 10.0f
    var screenGap = 6.0f

    // animation
    var openTime = 300
    var openAnimation = Animation.SCALE
    var openEase = Ease.EASE_OUT

    var closeTime = 300
    var closeAnimation = Animation.FADE
    var closeEase = Ease.EASE_OUT

    var resizeTime = 300
    var resizeAnimation = Animation.FADE
    var resizeEase = Ease.EASE_OUT
}
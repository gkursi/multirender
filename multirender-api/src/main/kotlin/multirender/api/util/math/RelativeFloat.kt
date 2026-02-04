package xyz.qweru.multirender.api.util.math

import kotlin.reflect.KProperty

data class RelativeFloat(val f: Float, val supplier: () -> Float) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Float =
        f * supplier()
}
package xyz.qweru.multirender.api.render.dim3

import xyz.qweru.multirender.api.util.color.Color
import xyz.qweru.multirender.api.util.math.Vec3d

interface Context3d {

    fun line(from: Vec3d, to: Vec3d, fromColor: Color, toColor: Color)

    fun quad(q1: Vec3d, q2: Vec3d, q3: Vec3d, q4: Vec3d)

    fun cube(q: Vec3d, dimensions: Vec3d)

}
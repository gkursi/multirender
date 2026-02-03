package xyz.qweru.multirender.api.render.dim3

import xyz.qweru.multirender.api.math.Vec3d

interface Context3d {

    fun line(from: Vec3d, to: Vec3d)

    fun quad(q1: Vec3d, q2: Vec3d, q3: Vec3d, q4: Vec3d)

    fun cube(q: Vec3d, dimensions: Vec3d)

}